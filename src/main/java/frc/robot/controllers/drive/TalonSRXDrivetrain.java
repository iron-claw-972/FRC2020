package frc.robot.controllers.drive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import frc.robot.util.*;

public class TalonSRXDrivetrain extends Drivetrain {
    private TalonSRX leftMotor1, leftMotor2, rightMotor1, rightMotor2;
    private Encoder leftEncoder, rightEncoder;
    private static PIDF leftDrivePIDF = new PIDF(0.2, 0, 0, 0.4);
    private static PIDF rightDrivePIDF = new PIDF(0.2, 0, 0, 0.4);

    // Gearbox Calculations
    private static final double amtTicksPerRotation = 2048;
    private static final double basicDriveWheelDiameter = 0.1; // meters
    private static final double basicDriveMotorGearRatio = (1/12.0) * (50.0) * (1/34.0) * (40.0) * amtTicksPerRotation; // amt ticks / wheel revs
    private static final double basicDriveTicksPerMeter = basicDriveMotorGearRatio / (basicDriveWheelDiameter * Math.PI);
    
    public TalonSRXDrivetrain() {
        super(leftDrivePIDF, rightDrivePIDF);

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

    protected double getLeftTicks() {
        return leftEncoder.get();
    }

    protected double getRightTicks() {
        return rightEncoder.get();
    }

    public double getLeftDist() {
        double rawCount = getLeftTicks() - startPosLeft;
        return rawCount / basicDriveTicksPerMeter;
    }

    public double getRightDist() {
        double rawCount = getRightTicks() - startPosRight;
        return rawCount / basicDriveTicksPerMeter;
    }
}