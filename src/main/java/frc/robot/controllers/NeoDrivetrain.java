package frc.robot.controllers;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.util.*;

public class NeoDrivetrain extends Drivetrain {
    public CANSparkMax leftMotor1, leftMotor2, rightMotor1, rightMotor2;
    private static PIDF leftDrivePIDF = new PIDF(0.1, 0, 0, 0.18);
    private static PIDF rightDrivePIDF = new PIDF(0.1, 0, 0, 0.18);
    
    public NeoDrivetrain() {
        super(leftDrivePIDF, rightDrivePIDF);

        leftMotor1 = new CANSparkMax(Context.leftMotor1ID, MotorType.kBrushless);
        leftMotor2 = new CANSparkMax(Context.leftMotor2ID, MotorType.kBrushless);
        rightMotor1 = new CANSparkMax(Context.rightMotor1ID, MotorType.kBrushless);
        rightMotor2 = new CANSparkMax(Context.rightMotor2ID, MotorType.kBrushless);
    }

    public void tankDrive(double leftPower, double rightPower) {
        leftMotor1.set(leftPower);
        leftMotor2.set(leftPower);
        rightMotor1.set(-rightPower);
        rightMotor2.set(-rightPower);
    }

    protected double getLeftTicks() {
        return leftMotor1.getEncoder().getPosition();
    }

    protected double getRightTicks() {
        return -rightMotor1.getEncoder().getPosition();
    }

    public double getLeftDist() {
        double rawCount = getLeftTicks() - startPosLeft;
        return rawCount / Context.neoDriveTicksPerMeter;
    }

    public double getRightDist() {
        double rawCount = getRightTicks() - startPosRight;
        return rawCount / Context.neoDriveTicksPerMeter;
    }

    public double getLeftVel() {
        // Because it gives speed in RPM we need to convert minutes to seconds
        return leftMotor1.getEncoder().getVelocity()/60.0 / Context.neoDriveTicksPerMeter;
    }

    public double getRightVel() {
        // Because it gives speed in RPM we need to convert minutes to seconds
        return -rightMotor1.getEncoder().getVelocity()/60.0 / Context.neoDriveTicksPerMeter;
    }
}