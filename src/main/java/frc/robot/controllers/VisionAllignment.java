package frc.robot.controllers;

import frc.robot.util.AdditionalMath;
import frc.robot.util.Context;
import frc.robot.util.PID;

public class VisionAllignment
{
    private final double headingP = 0.1;
    private final double headingI = 0.0;
    private final double headingD = 2.0;

    public PID headingPID = new PID(headingP, headingI, headingD);
    
    public enum StatusEnum { IDLE, IN_PROGRESS, ALIGNED, FAILED };
    public StatusEnum alignmentStatus = StatusEnum.IDLE;

    public double tx = 0.0, ty = 0.0, oldTx = 0.0, newTx = 0.0;

    public double pastTime = System.currentTimeMillis()-20;
    public double currentTime = System.currentTimeMillis();

    public double timeoutCounter = 0.0;

    public double rotationLocalized = 0.0;
    public double navXYawOffset = 0.0;

    public boolean targetFound = false;

    public void loop()
    {
        currentTime = System.currentTimeMillis();

        grabLimelightData();
        localizeRotation();

        switch(alignmentStatus)
        {
            case IDLE:
            {
                break;
            }
            case IN_PROGRESS:
            {
                if(targetFound) {
                    Context.robotController.drivetrain.arcadeDrive(0, loopHeadingPID(tx));
                } else {
                    Context.robotController.drivetrain.arcadeDrive(0, -loopHeadingPID(rotationLocalized));
                }

                if(Math.abs(newTx - oldTx) <= 0.01) {
                    timeoutCounter += (currentTime - pastTime); //if the angle does not change more than 0.01, start counting time
                } else {
                    timeoutCounter = 0.0; // if the angle delta is greater than that, reset the timer
                }
                
                if(Math.abs(tx) <= Context.alignmentThreshold && targetFound) // if the robot is close enough to the target, stop aligning
                {
                    alignmentStatus = StatusEnum.ALIGNED;
                }
                if(Math.abs(tx) >= Context.alignmentThreshold && timeoutCounter >= Context.alignmentTimeout) // if the robot times out and is not within acceptable, fail alignment 
                {
                    alignmentStatus = StatusEnum.FAILED;
                }

                break;
            }
            case ALIGNED:
            {
                if(tx >= Context.alignmentThreshold)
                {
                    alignmentStatus = StatusEnum.IN_PROGRESS;
                }

                break;
            }
            case FAILED:
            {
                break;
            }
        }

        System.out.println("alignmentStatus: " + alignmentStatus.toString() + ", visionTrack: " + targetFound + ", timeoutCount: " + timeoutCounter + ", localRot: " + localizedRotation);

        pastTime = currentTime;        
    }

    private void grabLimelightData()
    {
        tx = Context.robotController.ntInterface.tx;
        ty = Context.robotController.ntInterface.ty;
        targetFound = Context.robotController.ntInterface.targetAcquired;
        oldTx = newTx;
        newTx = tx;
    }
    
    private double loopHeadingPID(double actualAngle)
    {
        double rawPIDOutput = AdditionalMath.OvercomeFriction(headingPID.update(0.0, rotationLocalized, currentTime-pastTime), Context.ckStatic);
        double drivePower = AdditionalMath.Clamp(rawPIDOutput, -Context.maxTurnPower, Context.maxTurnPower);
        System.out.println("rawPIDOutput: " + rawPIDOutput + ", driverPower: " + drivePower + ", actualAngle:" + actualAngle);

        return drivePower;
    }

    private void localizeRotation()
    {
        if(Math.abs(tx) <= Context.alignmentThreshold && targetFound)
        {
            navXYawOffset = Context.robotController.navX.getAngle();
        }

        rotationLocalized = (Context.robotController.navX.getAngle() - navXYawOffset) % 360;
        
        if(rotationLocalized >= 180) {
            rotationLocalized -= 360;
        } else if (rotationLocalized <= -180) {
            rotationLocalized += 360;
        }
    }

    public boolean isAligned()
    {
        return alignmentStatus == StatusEnum.ALIGNED;
    }

    public boolean isInProgress()
    {
        return alignmentStatus == StatusEnum.IN_PROGRESS;
    }

    public boolean isActive()
    {
        return alignmentStatus == StatusEnum.IN_PROGRESS || alignmentStatus == StatusEnum.ALIGNED;
    }

    public StatusEnum getAlignmentStatus()
    {
        return alignmentStatus;
    }

    public void startTrack()
    {
        alignmentStatus = StatusEnum.IN_PROGRESS;

        timeoutCounter = 0.0;

        headingPID = new PID(headingP, headingI, headingD);        
    }

    public void stopTrack()
    {
        alignmentStatus = StatusEnum.IDLE;
    }
}