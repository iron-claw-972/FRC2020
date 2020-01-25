package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Encoder;

import frc.robot.util.*;

public class TalonSRXDrivetrain extends Drivetrain {
    public TalonSRX leftMotor1, leftMotor2, rightMotor1, rightMotor2;
    public Encoder leftEncoder, rightEncoder;
    
    public TalonSRXDrivetrain () {
        super();

        leftMotor1 = new TalonSRX(Context.leftMotor1ID);
        leftMotor2 = new TalonSRX(Context.leftMotor2ID);
        rightMotor1 = new TalonSRX(Context.rightMotor1ID);
        rightMotor2 = new TalonSRX(Context.rightMotor2ID);

        leftEncoder = new Encoder(Context.leftEncoderChannelA, Context.leftEncoderChannelB);
        rightEncoder = new Encoder(Context.rightEncoderChannelA, Context.rightEncoderChannelB);
    }

    public void tankDrive(double leftPower, double rightPower) {
        leftMotor1.set(ControlMode.PercentOutput, leftPower);
        leftMotor2.set(ControlMode.PercentOutput, leftPower);
        rightMotor1.set(ControlMode.PercentOutput, rightPower);
        rightMotor2.set(ControlMode.PercentOutput, rightPower);
    }

    public void resetEncoders() {
        startPosLeft = leftEncoder.get();
        startPosRight = rightEncoder.get();
    }

    public double getLeftDist() {
        //10 cm wheel diameter
        double rawCount = leftEncoder.get() - startPosLeft;
        return Context.driveClickToCm * rawCount;
    }

    public double getRightDist() {
        double rawCount = leftEncoder.get() - startPosRight;
        return Context.driveClickToCm * rawCount;
    }
}