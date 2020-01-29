package frc.robot.controllers;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.util.*;

public class NeoDrivetrain extends Drivetrain {
    public CANSparkMax leftMotor1, leftMotor2, rightMotor1, rightMotor2;
    private static PID leftDrivePID = new PID(0.09, 0, 0);
    private static PID rightDrivePID = new PID(0.09, 0, 0);
    
    public NeoDrivetrain() {
        super(leftDrivePID, rightDrivePID);

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

    protected double getLeftTicks() {
        return leftMotor1.getEncoder().getPosition();
    }

    protected double getRightTicks() {
        return rightMotor1.getEncoder().getPosition();
    }

    public double getLeftDist() {
        double rawCount = getLeftTicks() - startPosLeft;
        return rawCount / Context.neoDriveTicksPerMeter;
    }

    public double getRightDist() {
        double rawCount = getRightTicks() - startPosRight;
        return rawCount / Context.neoDriveTicksPerMeter;
    }
}