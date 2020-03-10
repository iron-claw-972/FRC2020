package frc.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import frc.robot.controllers.RobotController;
import frc.robot.util.*;
import frc.robot.shuffleboard.*;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import frc.robot.actions.*;

public class Robot extends TimedRobot {
  public RobotController robotController;

  public double origTime;
  public double robotStartTime;

  // private UsbCamera camera;

  @Override
  public void robotInit() {
    LiveWindow.disableAllTelemetry();
    Context.robotController = new RobotController();
    robotStartTime = System.currentTimeMillis()/1000.0;
    Context.robotController.compressor.start();

    // camera = edu.wpi.first.cameraserver.CameraServer.getInstance().startAutomaticCapture();
    // camera.setVideoMode(PixelFormat.kMJPEG, Context.cameraWidth, Context.cameraHeight, Context.cameraFPS);
    // Dashboard.init(camera);
  }

  @Override
  public void robotPeriodic() {
    // Dashboard.update();
  }

  @Override
  public void autonomousInit() {
    Context.robotController.drivetrain.resetEncoders();
    origTime = System.currentTimeMillis();
    // Context.robotController.sequentialScheduler.add(action);
  }

  @Override
  public void autonomousPeriodic() {
    // Context.robotController.sequentialScheduler.loop();
  }

  @Override
  public void teleopInit() {
    //Context.robotController.drivetrain.resetEncoders();
    Context.robotController.initAll();
    Context.robotController.parallelScheduler.currentActions.clear();
  }
  
  @Override

  public void teleopPeriodic() {
    Context.robotController.shooterController.setDesiredVelocity(1.5);
    
    Context.robotController.loopAll();
    
    double driverThrottle = Context.robotController.driverJoystick.getThrottle();
    double driverYaw = Context.robotController.driverJoystick.getYaw();
    
    Context.robotController.drivetrain.arcadeDrive(driverYaw, driverThrottle);

    
    if((Context.robotController.opticalLocalization.LeftMovementX != 0) || (Context.robotController.opticalLocalization.LeftMovementY !=0))
    {
      System.out.println("X: " + Context.robotController.opticalLocalization.LeftMovementX + " Y: " + Context.robotController.opticalLocalization.LeftMovementY);
    }
    //System.out.println(String.format("X: 0x%08X, Y:  0x%08X",Context.robotController.opticalLocalization.LeftMovementX, Context.robotController.opticalLocalization.LeftMovementY));
 
    Context.setWOFTargetColor();
    
  }
}
