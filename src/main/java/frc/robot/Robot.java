package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.controllers.RobotController;
import frc.robot.util.Context;

public class Robot extends TimedRobot {
  public RobotController robotController;
  public Joystick joy = new Joystick(Context.joystickID);

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
  public void teleopPeriodic() {
    double maxPower = 0.5;
    double maxTurn = 0.5;
    double linearPower = 0;
    double turnPower = 0;
    boolean WKey = Context.robotController.zmqServer.unityPacket.WKey;
    boolean AKey = Context.robotController.zmqServer.unityPacket.AKey;
    boolean SKey = Context.robotController.zmqServer.unityPacket.SKey;
    boolean DKey = Context.robotController.zmqServer.unityPacket.DKey;

    if(WKey)
    {
      linearPower = maxPower;
    }
    else if (SKey)
    {
      linearPower = -maxPower;
    }

    // Turn Power from Keys
    if(AKey)
    {
      turnPower = maxTurn;
    }
    else if (DKey)
    {
      turnPower = -maxTurn;
    }

    // Context.robotController.drivetrain.arcadeDrive(linearPower, turnPower);
  }
}
