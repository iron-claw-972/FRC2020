package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import com.ctre.phoenix.motorcontrol.*;

import frc.robot.util.*;

public class TalonFXDrivetrain extends Drivetrain {
    public TalonFX leftMotor1, leftMotor2, rightMotor1, rightMotor2;
    private DoubleSolenoid gearShifterSolenoid;
    public Gear gear = Gear.LOW;
    // To keep track of how far we've traveled since last shifting gears (in meters)
    private double leftDistTraveled = 0;
    private double rightDistTraveled = 0;
    
    public enum Gear {
        LOW, HIGH;
    }
    
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

        gearShifterSolenoid = new DoubleSolenoid(Context.gearShifterChannelA, Context.gearShifterChannelB);
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
        switch(gear) {
        case LOW:
            return (rawCount / Context.falconFXDriveTicksPerMeterLow) + leftDistTraveled;
        case HIGH:
            return (rawCount / Context.falconFXDriveTicksPerMeterHigh) + leftDistTraveled;
        default:
            System.out.println("Gear is not high or low (This should never happen");
            return -1.0;
        }
    }

    public double getRightDist() {
        double rawCount = rightMotor1.getSelectedSensorPosition() - startPosRight;
        switch(gear) {
        case LOW:
            return (rawCount / Context.falconFXDriveTicksPerMeterLow) + rightDistTraveled;
        case HIGH:
            return (rawCount / Context.falconFXDriveTicksPerMeterHigh) + rightDistTraveled;
        default:
            System.out.println("Gear is not high or low (This should never happen");
            return -1.0;
        }
    }

    /**
     * Toggles low to high gears
     */
    public void switchGears() {
        switch(gear) {
        case LOW:
            gearShifterSolenoid.set(Value.kForward);
            gear = Gear.HIGH;
            resetEncoders();
            leftDistTraveled = getLeftDist();
            rightDistTraveled = getRightDist();
            break;
        case HIGH:
            gearShifterSolenoid.set(Value.kReverse);
            gear = Gear.LOW;
            resetEncoders();
            leftDistTraveled = getLeftDist();
            rightDistTraveled = getRightDist();
            break;
        }
    }

}