package frc.robot.controllers;

import java.sql.Time;

import frc.robot.util.AdditionalMath;
import frc.robot.util.Context;
import frc.robot.util.PID;

public class VisionAllignment
{
    public double tx;
    public double ty;
    public PID headingPID = new PID(0.05, 0.0,0.0);

    double pastTime = System.currentTimeMillis()-20;
    double currentTime = System.currentTimeMillis();
    public void loop()
    {
        grabLimelightData();

        double offset = headingPID.update(0.0, tx, currentTime-pastTime); // integrating the PID for allignment

        double drivePower = AdditionalMath.Clamp(offset, -1, 1); // clamping the value for safety reasons and concerns

        System.out.println("offset: " + offset + " power: " + drivePower + "; tx:" + tx);

        // dt.tankDrive(drivePower, -drivePower);
        //dt.tankDrive(drivePower, drivePower);

        Context.robotController.drivetrain.arcadeDrive(0, drivePower);
        //NOTE: The other places calling this function have to be disabled when calling it here.
        pastTime = currentTime;
        currentTime = System.currentTimeMillis();
    }

    public void grabLimelightData()
    {
        tx = Context.robotController.ntInterface.tx;
        ty = Context.robotController.ntInterface.ty;
    }

    public boolean isAlligned()
    {
        return false;
    }
}