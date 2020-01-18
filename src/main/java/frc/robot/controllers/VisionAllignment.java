package frc.robot.controllers;

import frc.robot.util.AdditionalMath;
import frc.robot.util.Context;
import frc.robot.util.PID;

public class VisionAllignment
{
    private final double headingP = 0.1;
    private final double headingI = 0.0;
    private final double headingD = 2.0;

    private final double navXYawP = 0.1;
    private final double navXYawI = 0.0;
    private final double navXYawD = 2.0;

    public enum StatusEnum { IDLE, IN_PROGRESS, ALIGNED, FAILED };
    public StatusEnum alignmentStatus = StatusEnum.IDLE;

    public double tx = 0;
    public double ty = 0;

    public double oldTx = 0.0;
    public double newTx = 0.0;

    /////////////////////////////////////IMPORTANT////////////////////////////////////////////// oh, you're changing these during a competition?
    public PID headingPID = new PID(headingP, headingI, headingD); // PID used when optical aquisition is avalilable
    public PID navXYawPID = new PID(navXYawP, navXYawI, navXYawD); // PID used when no optical aquisition is avalilable and we need to rely on NavX data
    /////////////////////////////////////IMPORTANT//////////////////////////////////////////////

    public double pastTime = System.currentTimeMillis()-20;
    public double currentTime = System.currentTimeMillis();

    public double frameAngleDelta = 0.0;

    public double timeoutCounter = 0.0;

    public boolean timedOut = false;

    public double rotationLocalized;
    public double navXYawOffset = 0.0;

    public boolean targetFound= false;

    public void loop()
    {
        grabLimelightData();

        System.out.println("Alignment status: " + alignmentStatus.toString());
        System.out.println(timeoutCounter);

        if(alignmentStatus == StatusEnum.IN_PROGRESS || alignmentStatus == StatusEnum.IDLE)
        {
            System.out.println("Running Alignment");
            alignmentStatus = StatusEnum.IN_PROGRESS;

            double offset = AdditionalMath.OvercomeFriction(headingPID.update(0.0, tx, currentTime-pastTime), Context.ckStatic); // integrating the PID for allignment
            double drivePower = AdditionalMath.Clamp(offset, -Context.maxTurnPower, Context.maxTurnPower); // clamping the value for safety reasons and concerns

            System.out.println("offset: " + offset + " power: " + drivePower + " tx:" + tx);

            Context.robotController.drivetrain.arcadeDrive(0, drivePower); // drive the robot

            if(Math.abs(frameAngleDelta) <= 0.01) timeoutCounter+=currentTime-pastTime; //if the angle does not change more than 0.01, start counting time
            else timeoutCounter = 0.0; // if the angle delta is greater than that, reset the timer

            if(timeoutCounter >= Context.alignmentTimeout) timedOut = true;
            //NOTE: The other places calling this function have to be disabled when calling it here. // WHAT DOES THIS MEAN???

            if(Math.abs(tx) <= Context.alignmentThreshold && targetFound) // if the robot is close enough to the target, stop aligning
            {
                alignmentStatus = StatusEnum.ALIGNED;
            }
            if(Math.abs(tx) >= Context.alignmentThreshold && timedOut) // if the robot times out and is not within acceptable, fail alignment 
            {// use cases: robot gets pinned in such a way that the camera still sees the target, but can not move, therefore the tx does not change
                alignmentStatus = StatusEnum.FAILED;
            }
        }
        if(alignmentStatus == StatusEnum.ALIGNED)
        {
            keepTrack(); // once the target is aquired, keep track of it, in case we get nudged around
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
            double offset = AdditionalMath.OvercomeFriction(headingPID.update(0.0, tx, currentTime-pastTime), Context.ckStatic);
            double drivePower = AdditionalMath.Clamp(offset, -Context.maxTurnPower, Context.maxTurnPower);
            Context.robotController.drivetrain.arcadeDrive(0, drivePower);
        }
        else // Target tracked with NavX
        {
            System.out.println("Target Not Found");
            double offset = AdditionalMath.OvercomeFriction(navXYawPID.update(0.0, rotationLocalized, currentTime-pastTime), Context.ckStatic);
            double drivePower = AdditionalMath.Clamp(offset, -Context.maxTurnPower, Context.maxTurnPower);
            Context.robotController.drivetrain.arcadeDrive(0, -drivePower);
        }
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

    public void RESET() // resets the class, ready for the next alignment
    {
        alignmentStatus = StatusEnum.IDLE;
        rotationLocalized = 0.0;
        pastTime = System.currentTimeMillis()-20;
        currentTime = System.currentTimeMillis();
        timeoutCounter = 0.0;
        timedOut = false;

        headingPID = new PID(headingP, headingI, headingD); // PID used when optical aquisition is avalilable
        navXYawPID = new PID(navXYawP, navXYawI, navXYawD); // PID used when no optical aquisition is avalilable and we need to rely on NavX data
    }
}