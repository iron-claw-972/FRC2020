package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.*;

import frc.robot.util.*;

public class TalonFXDrivetrain extends Drivetrain {
    public TalonFX leftMotor1, leftMotor2, rightMotor1, rightMotor2;
    
    public TalonFXDrivetrain () {
        super();

        leftMotor1 = new TalonFX(Context.leftMotor1ID);
        leftMotor1.configFactoryDefault();
        leftMotor1.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

        leftMotor2 = new TalonFX(Context.leftMotor2ID);
        leftMotor2.configFactoryDefault();
        leftMotor2.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

        rightMotor1 = new TalonFX(Context.rightMotor1ID);
        rightMotor1.configFactoryDefault();
        rightMotor1.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

        rightMotor2 = new TalonFX(Context.rightMotor2ID);
        rightMotor2.configFactoryDefault();
        rightMotor2.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    }

    public void tankDrive(double leftPower, double rightPower) {
        leftMotor1.set(ControlMode.PercentOutput, leftPower);
        leftMotor2.set(ControlMode.PercentOutput, leftPower);
        rightMotor1.set(ControlMode.PercentOutput, rightPower);
        rightMotor2.set(ControlMode.PercentOutput, rightPower);
    }

    public void resetEncoders() {
        startPosLeft = leftMotor1.getSelectedSensorPosition();
        startPosRight = rightMotor1.getSelectedSensorPosition();
    }

    public double getLeftDist() {
        double rawCount = leftMotor1.getSelectedSensorPosition() - startPosLeft;
        return rawCount / Context.falconFXDriveTicksPerMeter;
    }

    public double getRightDist() {
        double rawCount = rightMotor1.getSelectedSensorPosition() - startPosRight;
        return rawCount / Context.falconFXDriveTicksPerMeter;
    }
}