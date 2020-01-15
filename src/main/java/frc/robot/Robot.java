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
    robotController = new RobotController();
    robotStartTime = System.currentTimeMillis()/1000.0;
  }

  @Override
  public void robotPeriodic() {

  }

  @Override
  public void autonomousInit() {
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
    Context.robotController.ntInterface.run();
    Context.visionAllignment.loop();
  }
}
