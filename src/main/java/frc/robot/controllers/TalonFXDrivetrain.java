package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

import frc.robot.util.*;

public class TalonFXDrivetrain extends Drivetrain {
    public TalonFX leftMotor1, leftMotor2, rightMotor1, rightMotor2;
    private DoubleSolenoid gearShifterSolenoid;
    private TalonSRX leftEncoderInterface, rightEncoderInterface;
    public Gear gear = Gear.LOW;
    
    public enum Gear {
        LOW, HIGH;
    }

    private static PIDF leftDrivePIDF = new PIDF(0.0, 0, 0, 1.0);
    private static PIDF rightDrivePIDF = new PIDF(0.0, 0, 0, 1.0);
    
    public TalonFXDrivetrain(TalonSRX leftEncoderInterface_, TalonSRX rightEncoderInterface_) {
        super(leftDrivePIDF, rightDrivePIDF);

        leftMotor1 = new TalonFX(Context.leftMotor1ID);
        leftMotor1.configFactoryDefault();
        leftMotor1.setNeutralMode(NeutralMode.Coast);

        leftMotor2 = new TalonFX(Context.leftMotor2ID);
        leftMotor2.configFactoryDefault();
        leftMotor2.setNeutralMode(NeutralMode.Coast);

        rightMotor1 = new TalonFX(Context.rightMotor1ID);
        rightMotor1.configFactoryDefault();
        rightMotor1.setNeutralMode(NeutralMode.Coast);

        rightMotor2 = new TalonFX(Context.rightMotor2ID);
        rightMotor2.configFactoryDefault();
        rightMotor2.setNeutralMode(NeutralMode.Coast);

        gearShifterSolenoid = new DoubleSolenoid(Context.gearShifterChannelA, Context.gearShifterChannelB);

        leftEncoderInterface = leftEncoderInterface_;
        rightEncoderInterface = leftEncoderInterface_;
    }

    public void tankDrive(double leftPower, double rightPower) {
        leftMotor1.set(ControlMode.PercentOutput, -leftPower);
        leftMotor2.set(ControlMode.PercentOutput, -leftPower);
        rightMotor1.set(ControlMode.PercentOutput, rightPower);
        rightMotor2.set(ControlMode.PercentOutput, rightPower);
    }

     protected double getLeftTicks() {
        return leftEncoderInterface.getSelectedSensorPosition();
    }

    protected double getRightTicks() {
        return rightEncoderInterface.getSelectedSensorPosition();
    }

    public double getLeftDist() {
        double rawCount = getLeftTicks() - startPosLeft;
        return rawCount / Context.falconFXDriveTicksPerMeter;
    }

    public double getRightDist() {
        double rawCount = getRightTicks() - startPosRight;
        return rawCount / Context.falconFXDriveTicksPerMeter;
    }

    public double getLeftVel() {
        double unitsPerSec = leftEncoderInterface.getSelectedSensorVelocity() * 10.0;
        return unitsPerSec / Context.falconFXDriveTicksPerMeter;
    }

    public double getRightVel() {
        double unitsPerSec = rightEncoderInterface.getSelectedSensorVelocity() * 10.0;
        return unitsPerSec / Context.falconFXDriveTicksPerMeter;
    }

    /**
     * Toggles low to high gears
     */
    public void shiftGears() {
        switch(gear) {
        case LOW:
            shiftGears(Gear.HIGH);
            break;
        case HIGH:
            shiftGears(Gear.LOW);
            break;
        }
    }

    /**
     * Shifts to specified position
     */
    public void shiftGears(Gear desiredGear) {
        switch(desiredGear) {
        case LOW:
            gearShifterSolenoid.set(Value.kForward);
            break;
        case HIGH:
            gearShifterSolenoid.set(Value.kReverse);
            break;
        }
        gear = desiredGear;
    }

}