package frc.robot.controllers.drive;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

import frc.robot.util.*;

public class TalonFXDrivetrain extends Drivetrain {
    private TalonFX leftMotor1, leftMotor2, rightMotor1, rightMotor2;
    private TalonSRX leftEncoderInterface, rightEncoderInterface;
    private DoubleSolenoid gearShifterSolenoid;
    private final Gear defaultGear = Gear.LOW;
    public Gear gear;

    private final double lowGearCurrentLimit = 65;
    private final double highGearCurrentLimit = 85;
    
    public enum Gear {
        LOW, HIGH;
    }

    private static PIDF leftDrivePIDF = new PIDF(0.2, 0, 0, 0.4);
    private static PIDF rightDrivePIDF = new PIDF(0.2, 0, 0, 0.4);

    // Gearbox Calculations
    private final double falconFXTicksPerRotation = 4096;
    private final double falconFXDriveWheelDiameter = 0.1; // meters
    private final double falconFXDriveTicksPerMeter = falconFXTicksPerRotation / (falconFXDriveWheelDiameter * Math.PI);
    
    public TalonFXDrivetrain(TalonFX leftMotor1_, TalonFX leftMotor2_, TalonFX rightMotor1_, TalonFX rightMotor2_, TalonSRX leftEncoderInterface_, TalonSRX rightEncoderInterface_) {
        super(leftDrivePIDF, rightDrivePIDF);

        leftMotor1 = leftMotor1_;
        leftMotor1.configFactoryDefault();
        leftMotor1.setNeutralMode(NeutralMode.Coast);

        leftMotor2 = leftMotor2_;
        leftMotor2.configFactoryDefault();
        leftMotor2.setNeutralMode(NeutralMode.Coast);

        rightMotor1 = rightMotor1_;
        rightMotor1.configFactoryDefault();
        rightMotor1.setNeutralMode(NeutralMode.Coast);

        rightMotor2 = rightMotor2_;
        rightMotor2.configFactoryDefault();
        rightMotor2.setNeutralMode(NeutralMode.Coast);

        leftEncoderInterface = leftEncoderInterface_;
        leftEncoderInterface.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

        rightEncoderInterface = rightEncoderInterface_;
        leftEncoderInterface.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

        // Gear shifting setup
        gearShifterSolenoid = new DoubleSolenoid(Context.gearShifterChannelA, Context.gearShifterChannelB);
        gear = defaultGear;
        shiftGears(gear);
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
        return -rightEncoderInterface.getSelectedSensorPosition();
    }

    public double getLeftDist() {
        return (getLeftTicks() - startPosLeft) / falconFXDriveTicksPerMeter;
    }

    public double getRightDist() {
        return (getRightTicks() - startPosRight) / falconFXDriveTicksPerMeter;
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
            setCurrentLimiting(lowGearCurrentLimit, lowGearCurrentLimit, true);
            gearShifterSolenoid.set(Value.kReverse);
            break;
        case HIGH:
            setCurrentLimiting(highGearCurrentLimit, highGearCurrentLimit, true);
            gearShifterSolenoid.set(Value.kForward);
            break;
        }
        gear = desiredGear;
        System.out.println("Gear: " + gear);
    }

    public void setCurrentLimiting(double amps, double activationAmps, boolean enableCurrentLimiting) {
        SupplyCurrentLimitConfiguration currentLimitConfiguration = new SupplyCurrentLimitConfiguration(enableCurrentLimiting, amps, activationAmps, 100);
        leftMotor1.configGetSupplyCurrentLimit(currentLimitConfiguration);
        leftMotor2.configGetSupplyCurrentLimit(currentLimitConfiguration);
        rightMotor1.configGetSupplyCurrentLimit(currentLimitConfiguration);
        rightMotor1.configGetSupplyCurrentLimit(currentLimitConfiguration);
    }

}