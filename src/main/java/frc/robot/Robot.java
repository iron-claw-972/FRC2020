package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.controllers.RobotController;
import frc.robot.util.*;

public class Robot extends TimedRobot {
  public RobotController robotController;

  public double origTime;
  public double robotStartTime;

  @Override
  public void robotInit() {
    Context.robotController = new RobotController();
    robotStartTime = System.currentTimeMillis()/1000.0;
  }

  @Override
  public void robotPeriodic() {

  }

  @Override
  public void autonomousInit() {
    Context.robotController.drivetrain.resetEncoders();
    origTime = System.currentTimeMillis();
    Context.robotController.autoDrive.startSpline();
  }

  @Override
  public void autonomousPeriodic() {
    Context.robotController.autoDrive.loop((System.currentTimeMillis() - origTime)/1000);
  }

  @Override
  public void teleopInit() {
    Context.robotController.drivetrain.resetEncoders();
  }
  
  @Override
  public void teleopPeriodic()
  {
    double driverThrottle = -Context.robotController.driverJoystick.getThrottle();
    double driverYaw = -Context.robotController.driverJoystick.getYaw();

    if(Context.robotController.driverJoystick.getJoystick().getRawButtonPressed(4))
    {
      if(Context.robotController.visionAllignment.isActive()){
        Context.robotController.visionAllignment.stopTrack();
      } else {
        Context.robotController.visionAllignment.startTrack();
      }
    }

    if(Context.robotController.driverJoystick.isInUse() || !Context.robotController.visionAllignment.isActive())
    {
      Context.robotController.visionAllignment.stopTrack();
      Context.robotController.drivetrain.arcadeDrive(driverYaw, driverThrottle);
    }
  }
}
