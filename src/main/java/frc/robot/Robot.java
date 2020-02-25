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
import frc.robot.util.Context;


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
    testCon1.startShooting();
    testCon2.startShooting();
    revUp = false;
    startTime = System.currentTimeMillis();
  }

  boolean revUp;
  double THR = 0.05;
  long startTime;
  double revTime;

  int testNeen;
  double setSpeed = 10.5;
  double speedStep = 0.1;
  double kPStep = 0.01;
  double kTStep = 0.01;
  double kFStep = 0.0001;
  double kIStep = 0.01;
  double kLoadRatioStep = 0.01;
  boolean xHeld;
  boolean bHeld;
  boolean lBumpHeld;

  enum gain {
    SPEED, P, T, F, I, LOAD
  }

  gain goose = gain.SPEED;

  @Override
  public void teleopPeriodic() {
    
    //System.out.println(testNeen);
    /*System.out.println("dubstepdogneenergoose");
    if(joy.getRawButton(1)) {
      motor1.set(ControlMode.PercentOutput, 0.2);
      motor2.set(ControlMode.PercentOutput, -0.2);
    } else {
      motor1.set(ControlMode.PercentOutput, 0);
      motor2.set(ControlMode.PercentOutput, 0);
    }*/

    if(joy.getRawButton(5)) {
      if(!lBumpHeld) {
        testNeen++;
        lBumpHeld = true;
      }
    } else {
      lBumpHeld = false;
    }

    testNeen = testNeen%6;

    switch(testNeen) {
      case 0:
        goose = gain.SPEED;
        break;
      case 1:
        goose = gain.P;
        break;
      case 2:
        goose = gain.T;
        break;
      case 3:
        goose = gain.F;
        break;
      case 4:
        goose = gain.I;
        break;
      case 5:
        goose = gain.LOAD;
        break;
    }

    gainAdjust(goose);

    testCon2.loop(setSpeed);
    testCon1.loop(setSpeed);
    JPT.JRADStatTest(joy.getRawButton(1), false);
    double loadRatio2 = testCon2.kLoadRatio * (testCon2.loadRatioConstant + testCon2.loadRatioRate * Math.abs(testCon2.getDesiredVelocity()));
    System.out.println("Set: " + setSpeed);
    System.out.println("Real set speed: " + loadRatio2 * Math.abs(testCon2.getDesiredVelocity()));
    double error = loadRatio2*testCon2.getDesiredVelocity() - testCon2.flywheelVelocity();
    System.out.println(error);
    if(Math.abs(error) < THR && !revUp) {
      revTime = Context.getRelativeTime(startTime);
      revUp = true;
    } else if(revUp) {
      System.out.println("REV TIME: " + revTime);
    }
    //System.out.println(testCon2.flywheelRPM());
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

  public void gainAdjust(gain M) {
    System.out.println("Adjusting " + M);
    switch(M) {
      case SPEED:
        if(joy.getRawButton(3)) {
          if(!xHeld) {
            setSpeed += speedStep;
            xHeld = true;
          }
        } else {
          xHeld = false;
        }
      
        if(joy.getRawButton(2)) {
          if(!bHeld) {
            setSpeed -= speedStep;
            bHeld = true;
          }
        } else {
          bHeld = false;
        }
        System.out.println(M + ": " + setSpeed);
        break;
      case P:
        if(joy.getRawButton(3)) {
          if(!xHeld) {
            testCon2.velocityJRADD.setP(kPStep);
            testCon1.velocityJRADD.setP(kPStep);
            xHeld = true;
          }
        } else {
          xHeld = false;
        }
      
        if(joy.getRawButton(2)) {
          if(!bHeld) {
            testCon2.velocityJRADD.setP(-kPStep);
            testCon1.velocityJRADD.setP(-kPStep);
            bHeld = true;
          }
        } else {
          bHeld = false;
        }
        System.out.println(M + ": " + testCon2.velocityJRADD.kP);
        break;
      case T:
        if(joy.getRawButton(3)) {
          if(!xHeld) {
            testCon2.velocityJRADD.setT(kTStep);
            testCon1.velocityJRADD.setT(kTStep);
            xHeld = true;
          }
        } else {
          xHeld = false;
        }
      
        if(joy.getRawButton(2)) {
          if(!bHeld) {
            testCon2.velocityJRADD.setT(-kTStep);
            testCon1.velocityJRADD.setT(-kTStep);
            bHeld = true;
          }
        } else {
          bHeld = false;
        }
        System.out.println(M + ": " + testCon2.velocityJRADD.kT);
        break;
      case F:
        if(joy.getRawButton(3)) {
          if(!xHeld) {
            testCon2.velocityJRADD.setF(kFStep);
            testCon1.velocityJRADD.setF(kFStep);
            xHeld = true;
          }
        } else {
          xHeld = false;
        }
      
        if(joy.getRawButton(2)) {
          if(!bHeld) {
            testCon2.velocityJRADD.setF(-kFStep);
            testCon1.velocityJRADD.setF(-kFStep);
            bHeld = true;
          }
        } else {
          bHeld = false;
        }
        System.out.println(M + ": " + testCon2.velocityJRADD.kF);
        break;
      case I:
        if(joy.getRawButton(3)) {
          if(!xHeld) {
            testCon2.velocityJRADD.setI(kIStep);
            testCon1.velocityJRADD.setI(kIStep);
            xHeld = true;
          }
        } else {
          xHeld = false;
        }
      
        if(joy.getRawButton(2)) {
          if(!bHeld) {
            testCon2.velocityJRADD.setI(-kIStep);
            testCon1.velocityJRADD.setI(-kIStep);
            bHeld = true;
          }
        } else {
          bHeld = false;
        }
        System.out.println(M + ": " + testCon2.velocityJRADD.kI);
        break;
      case LOAD:
        if(joy.getRawButton(3)) {
          if(!xHeld) {
            testCon2.velocityJRADD.setLoadRatio(kLoadRatioStep);
            testCon1.velocityJRADD.setLoadRatio(kLoadRatioStep);
            xHeld = true;
          }
        } else {
          xHeld = false;
        }
      
        if(joy.getRawButton(2)) {
          if(!bHeld) {
            testCon2.velocityJRADD.setLoadRatio(-kLoadRatioStep);
            testCon1.velocityJRADD.setLoadRatio(-kLoadRatioStep);
            bHeld = true;
          }
        } else {
          bHeld = false;
        }
        System.out.println(M + ": " + testCon2.velocityJRADD.kLoadRatio);
        break;
    }
  }

  @Override
  public void testPeriodic() {
  }
}
