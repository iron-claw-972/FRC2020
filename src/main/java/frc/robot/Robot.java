package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.controllers.RobotController;
import frc.robot.util.Context;

public class Robot extends TimedRobot
{

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

  boolean align = false;
  @Override
  public void teleopPeriodic()
  {
    Context.robotController.ntInterface.run();
    double joyX = -Context.ManagedJoystick.getAxisDeadBandManaged(0);
    double joyY = -Context.ManagedJoystick.getAxisDeadBandManaged(1);

    if(Context.ManagedJoystick.getRawButtonPressed(4))
    {
      Context.visionAllignment.RESET();
      align = !align;
    }
    if(joyX != 0 || joyY != 0)
    {
      align = false;
    }
    if(align)
    {
      Context.visionAllignment.loop();
    }
    if(!align)
    {
    Context.robotController.drivetrain.arcadeDrive(joyY, joyX);
    }
  }
}
