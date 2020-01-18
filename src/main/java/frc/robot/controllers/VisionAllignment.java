package frc.robot.controllers;

import frc.robot.util.AdditionalMath;
import frc.robot.util.Context;
import frc.robot.util.PID;

public class VisionAllignment
{
    private final double headingP = 0.1;
    private final double headingI = 0.0;
    private final double headingD = 2.0;

    public enum StatusEnum { IDLE, IN_PROGRESS, ALIGNED, FAILED };
    public StatusEnum alignmentStatus = StatusEnum.IDLE;

    public double tx = 0;
    public double ty = 0;

    public double oldTx = 0.0;
    public double newTx = 0.0;

    /////////////////////////////////////IMPORTANT////////////////////////////////////////////// oh, you're changing these during a competition?
    public PID headingPID = new PID(headingP, headingI, headingD); // PID used for controlling heading
    /////////////////////////////////////IMPORTANT//////////////////////////////////////////////

    public double pastTime = System.currentTimeMillis()-20;
    public double currentTime = System.currentTimeMillis();
    public double deltaTime = currentTime - pastTime;

    public double frameAngleDelta = 0.0;

    public double timeoutCounter = 0.0;

    public double rotationLocalized;
    public double navXYawOffset = 0.0;

    public boolean targetFound= false;

    public void loop()
    {
        grabLimelightData();

        deltaTime = currentTime - pastTime;

        System.out.println("Alignment status: " + alignmentStatus.toString());
        System.out.println(timeoutCounter);

        switch(alignmentStatus)
        {
            case IDLE:
            {
                break;
            }
            case IN_PROGRESS:
            {
                Context.robotController.drivetrain.arcadeDrive(0, loopHeadingPID(tx)); // drive the robot

                if(Math.abs(frameAngleDelta) <= 0.01) {
                    timeoutCounter += deltaTime; //if the angle does not change more than 0.01, start counting time
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
                keepTrack(); // once the target is aquired, keep track of it, in case we get nudged around

                break;
            }
            case FAILED:
            {
                break;
            }
        }

        pastTime = currentTime;
        currentTime = System.currentTimeMillis();
    }

    public void grabLimelightData()
    {
        tx = Context.robotController.ntInterface.tx;
        ty = Context.robotController.ntInterface.ty;
        targetFound = Context.robotController.ntInterface.targetAcquired;
        oldTx = newTx;
        newTx = tx;
        frameAngleDelta = newTx-oldTx;
    }

    public boolean isAligned()
    {
        if(alignmentStatus == StatusEnum.ALIGNED) { 
            return true;
        }
        return false;
    }

    public boolean isInProgress()
    {
        if(alignmentStatus == StatusEnum.IN_PROGRESS) {
            return true;
        }
        return false;
    }

    public StatusEnum getAlignmentStatus()
    {
        return alignmentStatus;
    }

    private void keepTrack()
    {
        localizeRotation();
        if(targetFound) // Target tracked with vision
        {
            System.out.println("Target Found");
            Context.robotController.drivetrain.arcadeDrive(0, loopHeadingPID(tx));
        }
        else // Target tracked with NavX
        {
            System.out.println("Target Not Found");
            Context.robotController.drivetrain.arcadeDrive(0, -loopHeadingPID(rotationLocalized));
        }
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
            rotationLocalized = 0.0;
            navXYawOffset = Context.robotController.navX.getAngle(); // resetting the rotation once we align to the target
        }

        rotationLocalized = Context.robotController.navX.getAngle() - navXYawOffset;
        rotationLocalized %= 360;
        
        if(rotationLocalized >= 180)
        {
            rotationLocalized -= 360;
        }

        System.out.println("Localized Rotation: " + rotationLocalized);
    }

    public void startTrack()
    {
        rotationLocalized = 0.0;
        pastTime = System.currentTimeMillis()-20;
        currentTime = System.currentTimeMillis();
        timeoutCounter = 0.0;

        headingPID = new PID(headingP, headingI, headingD);

        alignmentStatus = StatusEnum.IN_PROGRESS;
    }

    public void stopTrack()
    {
        alignmentStatus = StatusEnum.IDLE;
    }
}