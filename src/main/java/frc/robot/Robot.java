package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.controllers.RobotController;
import frc.robot.util.*;

public class Robot extends TimedRobot {
  public double origTime;
  public double robotStartTime;

  @Override
  public void robotInit() {
    Context.robotController = new RobotController();
    robotStartTime = System.currentTimeMillis()/1000.0;
    Context.robotController.compressor.start();
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    Context.robotController.drivetrain.resetEncoders();
    Context.robotController.autoDrive.localizer.resetLocalization();
    origTime = System.currentTimeMillis();
  }

  @Override
  public void autonomousPeriodic() {
    Context.robotController.autoDrive.loop((System.currentTimeMillis() - origTime)/1000.0);
    // System.out.println(Context.robotController.autoDrive.localizer.getPoseEstimate());
    Context.robotController.drivetrain.printWheelVelocities();
  }

  @Override
  public void teleopInit() {
    Context.robotController.drivetrain.resetEncoders();
    Context.robotController.autoDrive.localizer.resetLocalization();
    Context.robotController.initAll();
  }

  double maxSpeed = 1; // m/s
  
  @Override
  public void teleopPeriodic() {
    // Context.robotController.loopAll();

    double driverThrottle = Context.robotController.driverJoystick.getThrottle()*maxSpeed;
    double driverYaw = -Context.robotController.driverJoystick.getYaw()*maxSpeed;

    // Context.robotController.autoDrive.updatePoseEstimate();
    // System.out.println(Context.robotController.autoDrive.getPoseEstimate());

    if (Context.robotController.driverJoystick.shiftGears()) {
      Context.robotController.drivetrain.shiftGears();
    }

    // Context.robotController.drivetrain.printWheelVelocities();
    
    // if (Context.robotController.driverJoystick.getToggleTrack()) {
    //   if (Context.robotController.visionAllignment.isActive()) {
    //     Context.robotController.visionAllignment.stopTrack();
    //   } else {
    //     Context.robotController.visionAllignment.startTrack();
    //   }
    // }

    // if (Context.robotController.driverJoystick.isInUse() || !Context.robotController.visionAllignment.isActive()) {
    //   Context.robotController.visionAllignment.stopTrack();
      Context.robotController.drivetrain.arcadeDrive(driverThrottle, driverYaw);
    // }
    
    // if((Context.robotController.opticalLocalization.LeftMovementX != 0) || (Context.robotController.opticalLocalization.LeftMovementY !=0))
    // {
    //   System.out.println("X: " + Context.robotController.opticalLocalization.LeftMovementX + " Y: " + Context.robotController.opticalLocalization.LeftMovementY);
    // }
    //System.out.println(String.format("X: 0x%08X, Y:  0x%08X",Context.robotController.opticalLocalization.LeftMovementX, Context.robotController.opticalLocalization.LeftMovementY));
  }

  public void testInit() {
    Context.robotController.drivetrain.resetEncoders();
    Context.robotController.autoDrive.localizer.resetLocalization();
    origTime = System.currentTimeMillis();
  }

  public void testPeriodic() {
    Context.robotController.drivetrain.tankDrivePID(1.0, 1.0);

    // Context.robotController.drivetrain.printWheelVelocities();
  }

}
