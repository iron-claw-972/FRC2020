package frc.robot.controllers;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.util.*;

public class NeoDrivetrain extends Drivetrain {
    public CANSparkMax leftMotor1, leftMotor2, rightMotor1, rightMotor2;
    private static PID leftDrivePID = new PID(0.09, 0, 0);
    private static PID rightDrivePID = new PID(0.09, 0, 0);
    
    public NeoDrivetrain(CANSparkMax leftMotor1_, CANSparkMax leftMotor2_, CANSparkMax rightMotor1_, CANSparkMax rightMotor2_) {
        super(leftDrivePID, rightDrivePID);

        leftMotor1 = leftMotor1_;
        leftMotor2 = leftMotor2_;
        rightMotor1 = rightMotor1_;
        rightMotor2 = rightMotor2_;
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