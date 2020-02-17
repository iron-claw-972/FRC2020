package frc.robot;

import java.util.ArrayList;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.RemoteFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.*;
import frc.robot.controllers.ShooterController;
import frc.robot.util.JRADParameterTest;
import frc.robot.util.MotorParameterTest;


public class Robot extends TimedRobot {

  TalonFX motor1;
  TalonFX motor2;
  ShooterController testCon1 = new ShooterController(17, false);
  ShooterController testCon2 = new ShooterController(18, true);
  Joystick joy;
  MotorParameterTest MPT = new MotorParameterTest();
  JRADParameterTest JPT = new JRADParameterTest(testCon1);

  @Override
  public void robotInit() {
    motor1 = new TalonFX(17);
    motor2 = new TalonFX(18);
    motor2.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    motor1.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    joy = new Joystick(0);
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
    testNeen++;
    testCon1.startShooting();
    testCon2.startShooting();
  }

  int testNeen;
  double setSpeed = 10.0;
  boolean xHeld;
  boolean bHeld;

  @Override
  public void teleopPeriodic() {
    if(joy.getRawButton(4)) {
      System.out.println("COLLECTING");
    }
    //System.out.println(testNeen);
    /*System.out.println("dubstepdogneenergoose");
    if(joy.getRawButton(1)) {
      motor1.set(ControlMode.PercentOutput, 0.2);
      motor2.set(ControlMode.PercentOutput, -0.2);
    } else {
      motor1.set(ControlMode.PercentOutput, 0);
      motor2.set(ControlMode.PercentOutput, 0);
    }*/

    if(joy.getRawButton(3)) {
      if(!xHeld) {
        setSpeed += 0.5;
        xHeld = true;
      }
    } else {
      xHeld = false;
    }

    if(joy.getRawButton(2)) {
      if(!bHeld) {
        setSpeed -= 0.5;
        bHeld = true;
      }
    } else {
      bHeld = false;
    }

    System.out.println("Set: " + setSpeed);

    testCon2.loop(setSpeed);
    testCon1.loop(setSpeed);
    JPT.JRADStatTest(joy.getRawButton(1), false);
    double loadRatio2 = testCon2.kLoadRatio * (testCon2.loadRatioConstant + testCon2.loadRatioRate * Math.abs(testCon2.getDesiredVelocity()));
    System.out.println((loadRatio2*testCon2.getDesiredVelocity() - testCon2.flywheelVelocity()/2));
    System.out.println(testCon2.flywheelRPM());
    //System.out.println("break");
    //System.out.println("2 - DESIRED VEL: " + 2*testCon2.getDesiredVelocity() + " SET VEL: " + testCon2.getSetVelocity() + " TRUE VEL: " + testCon2.flywheelVelocity());
    //System.out.println("1 - DESIRED VEL: " + 2*testCon1.getDesiredVelocity() + " SET VEL: " + testCon1.getSetVelocity() + " TRUE VEL: " + testCon1.flywheelVelocity());
    //JPT.JRADParameterTest(false, false);


    /*
    MPT.TalonParameterTest(motor2, true, 2048, 0.12065);
    if(!MPT.flood) {
      motor1.set(ControlMode.PercentOutput, -MPT.MAX_CURRENT);
    } else {
      motor1.set(ControlMode.PercentOutput, 0);
    }*/

    /*
    ArrayList<Double> dog1 = new ArrayList<>();
    dog1.add(10.0);
    dog1.add(11.0);
    ArrayList<Double> dog2 = new ArrayList<>();
    dog2.add(1.13);
    dog2.add(1.16);
    double[] test = JPT.linearRegressionDesiredLoadRatio(dog1, dog2);
    double[] test2 = MPT.linearRegressionVelCurrent(dog1, dog2);
    System.out.println("TEST: " + test[0] + "   " + test[1]);
    System.out.println("TEST2: " + test2[0] + "   " + test2[1]);
    */
  }

  @Override
  public void testPeriodic() {
  }
}
