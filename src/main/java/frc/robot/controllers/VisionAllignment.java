package frc.robot.controllers;

import java.sql.Time;

import frc.robot.util.AdditionalMath;
import frc.robot.util.Context;
import frc.robot.util.PID;

public class VisionAllignment
{
    final double headingP = 0.1;
    final double headingI = 0.0;
    final double headingD = 2.0;

    final double NavXYawP = 0.1;
    final double NavXYawI = 0.0;
    final double NavXYawD = 2.0;

    public enum statusEnum { IDLE, IN_PROGRESS, ALIGNED, FAILED };
    public statusEnum AlignmentStatus = statusEnum.IDLE;

    public double tx;
    public double ty;

    /////////////////////////////////////IMPORTANT////////////////////////////////////////////// oh, you're changing these during a competition?
    public PID headingPID = new PID(headingP, headingI, headingD); // PID used when optical aquisition is avalilable
    public PID NavXYawPID = new PID(NavXYawP, NavXYawI, NavXYawD); // PID used when no optical aquisition is avalilable and we need to rely on NavX data
    /////////////////////////////////////IMPORTANT//////////////////////////////////////////////

    double pastTime = System.currentTimeMillis()-20;
    double currentTime = System.currentTimeMillis();

    double frameAngleDelta = 0.0;
    double oldTx = 0.0;
    double newTx = 0.0;

    double timeoutCounter = 0.0;

    boolean timedOut = false;

    double rotationLocalized;
    double navXYawOffset = 0.0;

    boolean targetFound= false;

    public void loop()
    {
        grabLimelightData();
        System.out.println("Alignment status: " + AlignmentStatus.toString());
        System.out.println(timeoutCounter);
        if(AlignmentStatus == statusEnum.IN_PROGRESS || AlignmentStatus == statusEnum.IDLE)
        {
            System.out.println("Running Alignment");
            AlignmentStatus = statusEnum.IN_PROGRESS;

            double offset = headingPID.update(0.0, tx, currentTime-pastTime); // integrating the PID for allignment
            double drivePower = AdditionalMath.Clamp(offset, -Context.maxTurnPower, Context.maxTurnPower); // clamping the value for safety reasons and concerns

            System.out.println("offset: " + offset + " power: " + drivePower + " tx:" + tx);

            Context.robotController.drivetrain.arcadeDrive(0, drivePower); // drive the robot

            if(Math.abs(frameAngleDelta) <= 0.01) timeoutCounter+=currentTime-pastTime; //if the angle does not change more than 0.01, start counting time
            else timeoutCounter = 0.0; // if the angle delta is greater than that, reset the timer

            if(timeoutCounter >= Context.alignmentTimeout) timedOut = true;
            //NOTE: The other places calling this function have to be disabled when calling it here. // WHAT DOES THIS MEAN???

            if(Math.abs(tx) <= Context.alignmentThreshold && targetFound) // if the robot is close enough to the target, stop aligning
            {
                AlignmentStatus = statusEnum.ALIGNED;
            }
            if(Math.abs(tx) >= Context.alignmentThreshold && timedOut) // if the robot times out and is not within acceptable, fail alignment 
            {// use cases: robot gets pinned in such a way that the camera still sees the target, but can not move, therefore the tx does not change
                AlignmentStatus = statusEnum.FAILED;
            }
        }
        if(AlignmentStatus == statusEnum.ALIGNED)
        {
            KeepTrack(); // once the target is aquired, keep track of it, in case we get nudged around
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
        if(AlignmentStatus == statusEnum.ALIGNED) return true;
        return false;
    }

    public statusEnum getAlignmentStatus()
    {
        return AlignmentStatus;
    }

    private void KeepTrack()
    {
        if(Math.abs(tx) <= Context.alignmentThreshold && targetFound)
        {
            rotationLocalized = 0.0;
            navXYawOffset = Context.robotController.navX.getAngle(); // resetting the rotation once we align to teh target
        }
        rotationLocalized = Context.robotController.navX.getAngle() - navXYawOffset;
        rotationLocalized = (rotationLocalized%360);
        if(rotationLocalized >= 180)
        {
            rotationLocalized -= 360;
        }

        System.out.println("Localized Rotation: " + rotationLocalized);
        if(targetFound)
        {
            System.out.println("Target Found");
        }
        else
        {
            System.out.println("Target Not Found");
        }

        if(!targetFound) // if there is no target found, turn based on NavX rotation data
        {
            double offset = NavXYawPID.update(0.0, rotationLocalized, currentTime-pastTime);
            double drivePower = AdditionalMath.Clamp(offset, -Context.maxTurnPower, Context.maxTurnPower);
            Context.robotController.drivetrain.arcadeDrive(0, -drivePower);
        }
        else // if target is found optically, use the limelight data
        {
            double offset = headingPID.update(0.0, tx, currentTime-pastTime);
            double drivePower = AdditionalMath.Clamp(offset, -Context.maxTurnPower, Context.maxTurnPower);
            Context.robotController.drivetrain.arcadeDrive(0, drivePower);
        }
    }

    public void RESET() // resets the class, ready for the next alignment
    {
        AlignmentStatus = statusEnum.IDLE;
        rotationLocalized = 0.0;
        pastTime = System.currentTimeMillis()-20;
        currentTime = System.currentTimeMillis();
        timeoutCounter = 0.0;
        timedOut = false;

        headingPID = new PID(headingP, headingI, headingD); // PID used when optical aquisition is avalilable
        NavXYawPID = new PID(NavXYawP, NavXYawI, NavXYawD); // PID used when no optical aquisition is avalilable and we need to rely on NavX data
    }
}