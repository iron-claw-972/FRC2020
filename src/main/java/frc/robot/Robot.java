package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.controllers.RobotController;
import frc.robot.util.*;

public class Robot extends TimedRobot {
  public RobotController robotController;

  public double origTime;
  public double robotStartTime;

  @Override
  public void robotInit()
  {
    Context.robotController = new RobotController();
    robotStartTime = System.currentTimeMillis()/1000.0;
    Context.robotController.compressor.start();
  }

  @Override
  public void robotPeriodic() {

  }

  @Override
  public void autonomousInit()
  {
    origTime = System.currentTimeMillis();
    //Context.robotController.autoDrive.startSpline();
  }

  @Override
  public void autonomousPeriodic() {
    Context.robotController.autoDrive.loop((System.currentTimeMillis() - origTime)/1000);
  }

  @Override
  public void teleopInit()
  {
    //Context.robotController.drivetrain.resetEncoders();
  }
  
  @Override

  public void teleopPeriodic()
  {
    Context.robotController.loopAll();

  public void teleopPeriodic() {
    double driverThrottle = -Context.robotController.driverJoystick.getThrottle();
    double driverYaw = -Context.robotController.driverJoystick.getYaw();

    if (Context.robotController.driverJoystick.shiftGears()) {
      Context.robotController.drivetrain.shiftGears();
    }
    
    if (Context.robotController.driverJoystick.getToggleTrack()) {
      if (Context.robotController.visionAllignment.isActive()) {
        Context.robotController.visionAllignment.stopTrack();
      } else {
        Context.robotController.visionAllignment.startTrack();
      }
    }

    if (Context.robotController.driverJoystick.isInUse() || !Context.robotController.visionAllignment.isActive()) {
      Context.robotController.visionAllignment.stopTrack();
      Context.robotController.drivetrain.arcadeDrive(driverYaw, driverThrottle);
    }
    */
    if((Context.robotController.opticalLocalization.LeftMovementX != 0) || (Context.robotController.opticalLocalization.LeftMovementY !=0))
    {
      System.out.println("X: " + Context.robotController.opticalLocalization.LeftMovementX + " Y: " + Context.robotController.opticalLocalization.LeftMovementY);
    }
    //System.out.println(String.format("X: 0x%08X, Y:  0x%08X",Context.robotController.opticalLocalization.LeftMovementX, Context.robotController.opticalLocalization.LeftMovementY));
  }
}
