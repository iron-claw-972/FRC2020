package frc.robot;

import java.util.ArrayList;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.RemoteFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.*;
import frc.robot.util.MotorParameterTest;


public class Robot extends TimedRobot {

  TalonFX motor1;
  TalonFX motor2;
  Joystick joy;
  MotorParameterTest MPT = new MotorParameterTest();

  @Override
  public void robotInit() {
    motor1 = new TalonFX(17);
    motor2 = new TalonFX(18);
    motor2.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    joy = new Joystick(1);
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
  public void teleopPeriodic() {
    /*System.out.println("dubstepdogneenergoose");
    if(joy.getRawButton(1)) {
      motor1.set(ControlMode.PercentOutput, 0.2);
      motor2.set(ControlMode.PercentOutput, -0.2);
    } else {
      motor1.set(ControlMode.PercentOutput, 0);
      motor2.set(ControlMode.PercentOutput, 0);
    }*/

    MPT.TalonVelToCurrentTest(motor2, true, 2048, 0.12065);
    motor1.set(ControlMode.PercentOutput, -MPT.input);

    /*
    ArrayList<Double> dog1 = new ArrayList<>();
    dog1.add(0.0);
    dog1.add(1.0);
    ArrayList<Double> dog2 = new ArrayList<>();
    dog2.add(1.0);
    dog2.add(2.0);
    double[] test = MPT.linearRegressionVelCurrent(dog1, dog2);
    System.out.println(test[0] + "   " + test[1]);*/
  }

  @Override
  public void testPeriodic() {
  }
}
