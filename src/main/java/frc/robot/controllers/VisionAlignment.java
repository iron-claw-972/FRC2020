package frc.robot.controllers;

import frc.robot.util.Context;
import frc.robot.util.PID;

public class VisionAlignment
{
    public double tx;
    public double ty;
    public PID headingPID = new PID(0.05, 0.0,0.0);    

    public void loop()
    {
        grabLimelightData();

        //Context.robotController.drivetrain.arcadeDrive(power, turn);
        //NOTE: The other places calling this function have to be disabled when calling it here.
    }

    public void grabLimelightData()
    {
        tx = Context.robotController.ntInterface.tx;
        ty = Context.robotController.ntInterface.ty;
    }

    public boolean isAligned()
    {
        return false;
    }
}