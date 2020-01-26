package frc.robot.controllers;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.util.*;

public class NeoDrivetrain extends Drivetrain {
    public CANSparkMax leftMotor1, leftMotor2, rightMotor1, rightMotor2;
    
    public NeoDrivetrain () {
        super();

        leftMotor1 = new CANSparkMax(Context.leftMotor1ID, MotorType.kBrushless);
        leftMotor2 = new CANSparkMax(Context.leftMotor2ID, MotorType.kBrushless);
        rightMotor1 = new CANSparkMax(Context.rightMotor1ID, MotorType.kBrushless);
        rightMotor2 = new CANSparkMax(Context.rightMotor2ID, MotorType.kBrushless);
    }

    public void tankDrive(double leftPower, double rightPower) {
        leftMotor1.set(leftPower);
        leftMotor2.set(leftPower);
        rightMotor1.set(rightPower);
        rightMotor2.set(rightPower);
    }

    public void resetEncoders() {
        startPosLeft = leftMotor1.getEncoder().getPosition();
        startPosRight = rightMotor1.getEncoder().getPosition();
    }

    public double getLeftDist() {
        double rawCount = leftMotor1.getEncoder().getPosition() - startPosLeft;
        return rawCount / Context.neoDriveTicksPerMeter;
    }

    public double getRightDist() {
        double rawCount = rightMotor1.getEncoder().getPosition() - startPosRight;
        return rawCount / Context.neoDriveTicksPerMeter;
    }
}