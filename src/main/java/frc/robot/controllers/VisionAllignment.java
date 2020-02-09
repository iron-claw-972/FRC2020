package frc.robot.controllers;

import frc.robot.util.AdditionalMath;
import frc.robot.util.Context;
import frc.robot.util.PID;

public class VisionAllignment
{
    // Heading PID parameters, declaration and initialization
    private final double headingP = 0.1;
    private final double headingI = 0.0;
    private final double headingD = 2.0;

    public PID headingPID = new PID(headingP, headingI, headingD);
    
    // Different modes of VisionAllignment system, their state determines actions that system takes
    public enum StatusEnum { IDLE, IN_PROGRESS, ALIGNED, FAILED };
    public StatusEnum alignmentStatus = StatusEnum.IDLE;

    // Parameters from Limelight: tx = target X (deg), ty ...
    public double tx = 0.0, ty = 0.0, oldTx = 0.0, newTx = 0.0;
    // TargetFound is if the Limelight can see the target
    public boolean targetFound = false;

    // Time managment for calculation of deltaTime and such
    public double pastTime = System.currentTimeMillis()-20;
    public double currentTime = System.currentTimeMillis();

    // Counts up until threshold is reached and allignment is aborted
    public double timeoutCounter = 0.0;

    // NavX data for visionless tracking
    public double rotationLocalized = 0.0;
    public double navXYawOffset = 0.0;

    

    public void loop()
    {
        currentTime = System.currentTimeMillis();

        grabLimelightData();
        localizeRotation();
        
        switch(alignmentStatus)
        {
            case IDLE:
            {
                // System awaits command to start tracking target
                break;
            }
            case IN_PROGRESS:
            {
                if(targetFound) {
                    // While the target is in the view of the Limelight, the PID will follow the Limelight data
                    Context.robotController.drivetrain.arcadeDrive(0, loopHeadingPID(tx));
                } else {
                    // While the target is out of view, the PID will follow the NavX data
                    Context.robotController.drivetrain.arcadeDrive(0, -loopHeadingPID(rotationLocalized));
                }

                if(Math.abs(newTx - oldTx) <= 0.01) {
                    // While the angle changes less than 0.01, increment abort time
                    timeoutCounter += (currentTime - pastTime);
                } else {
                    // If the angle changes fast enough, the abort is aborted
                    timeoutCounter = 0.0;
                }
                
                if(Math.abs(tx) <= Context.alignmentThreshold && targetFound)
                {
                    // Once the robot aims close enough to the target, allignment will be paused
                    alignmentStatus = StatusEnum.ALIGNED;
                }
                if(Math.abs(tx) >= Context.alignmentThreshold && timeoutCounter >= Context.alignmentTimeout)
                {
                    // If the robot aborts due to timeout, the state will become FAILED
                    alignmentStatus = StatusEnum.FAILED;
                }

                break;
            }
            case ALIGNED:
            {
                // If the robot turns out of the acceptable range, the allignment will be unpaused
                if(Math.abs(tx) >= Context.alignmentThreshold)
                {
                    alignmentStatus = StatusEnum.IN_PROGRESS;
                }

                break;
            }
            case FAILED:
            {
                // Nothing happens in FAILED state
                break;
            }
        }

        // Print all important values for debugging
        System.out.println("alignmentStatus: " + alignmentStatus.toString() + ", visionTrack: " + targetFound + ", timeoutCount: " + timeoutCounter + ", localRot: " + rotationLocalized);

        pastTime = currentTime;        
    }

    private void grabLimelightData()
    {
        // Grabs the Limelight data from the NetworkTables interface
        tx = Context.robotController.ntInterface.tx;
        ty = Context.robotController.ntInterface.ty;
        targetFound = Context.robotController.ntInterface.targetAcquired;

        // Stores values for heading change calculation and aborting
        oldTx = newTx;
        newTx = tx;
    }
    
    private double loopHeadingPID(double actualAngle)
    {
        // Wrapper class for the heading PID, simplifies process of using it elsewhere, also includes logging
        double rawPIDOutput = AdditionalMath.OvercomeFriction(headingPID.update(0.0, rotationLocalized, currentTime-pastTime), Context.ckStatic);
        double drivePower = AdditionalMath.Clamp(rawPIDOutput, -Context.maxTurnPower, Context.maxTurnPower);
        System.out.println("rawPIDOutput: " + rawPIDOutput + ", driverPower: " + drivePower + ", actualAngle:" + actualAngle);

        return drivePower;
    }

    private void localizeRotation()
    {
        // When the robot is facing the target, the angle is recorded for later NavX based tracking
        if(Math.abs(tx) <= Context.alignmentThreshold && targetFound)
        {
            navXYawOffset = Context.robotController.navX.getRawHeading();
        }

        // Grabs the angle, subtracts the offset and does modulus of 360 to contain value between -360 and 360
        rotationLocalized = (Context.robotController.navX.getRawHeading() - navXYawOffset) % 360;
        
        // Fixes the angle between -180 and 180, with 0 being the target
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