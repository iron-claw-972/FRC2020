package frc.robot.subsystems.shooter;

import edu.wpi.first.networktables.*;
import frc.robot.util.PID;

public class VisionAllignment
{
    public boolean GetAllignmentState()
    {
        return false;
    }
    PID pid = new PID(0.05, 0.0,0.0);
    public void AllignPeriodic()
    {
        NetworkTable limelight = NetworkTableInstance.getDefault().getTable("limelight");
        double tx = limelight.getEntry("tx").getDouble(0);
        
    }
}