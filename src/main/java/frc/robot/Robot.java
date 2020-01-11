package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
<<<<<<< Updated upstream
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
=======
import frc.robot.controllers.RobotController;
import frc.robot.controllers.VisionAllignment;
import frc.robot.util.Context;
>>>>>>> Stashed changes

public class Robot extends TimedRobot {

  @Override
  public void robotInit() {

  }

  @Override
  public void robotPeriodic() {

  }

  @Override
  public void autonomousInit() {

  }

  @Override
  public void autonomousPeriodic() {

  }

  @Override
  public void teleopInit() {

  }

  
  VisionAllignment va = new VisionAllignment();
  @Override
<<<<<<< Updated upstream
  public void teleopPeriodic() {
    
=======
  public void teleopPeriodic()
  {
    Context.robotController.ntInterface.run();
    va.loop();
>>>>>>> Stashed changes
  }
}
