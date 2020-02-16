package frc.robot.controllers.drive;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

import frc.robot.util.*;

public class TalonFXDrivetrain extends Drivetrain {
    private TalonFX leftMotor1, leftMotor2, rightMotor1, rightMotor2;
    private WPI_TalonFX WPI_leftMotor1, WPI_leftMotor2, WPI_rightMotor1, WPI_rightMotor2;
    private SpeedControllerGroup leftSide, rightSide;
    private DifferentialDrive differentialDrive;
    private TalonSRX leftEncoderInterface, rightEncoderInterface;
    private DoubleSolenoid gearShifterSolenoid;
    public Gear gear = Gear.LOW;
    
    public enum Gear {
        LOW, HIGH;
    }

    private static PIDF leftDrivePIDF = new PIDF(0.2, 0, 0, 0.4);
    private static PIDF rightDrivePIDF = new PIDF(0.2, 0, 0, 0.4);
    
    public TalonFXDrivetrain(TalonFX leftMotor1_, TalonFX leftMotor2_, TalonFX rightMotor1_, TalonFX rightMotor2_, TalonSRX leftEncoderInterface_, TalonSRX rightEncoderInterface_) {
        super(leftDrivePIDF, rightDrivePIDF);

        leftMotor1 = leftMotor1_;
        leftMotor1.configFactoryDefault();
        leftMotor1.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        leftMotor1.setNeutralMode(NeutralMode.Coast);

        leftMotor2 = leftMotor2_;
        leftMotor2.configFactoryDefault();
        leftMotor2.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        leftMotor2.setNeutralMode(NeutralMode.Coast);

        rightMotor1 = rightMotor1_;
        rightMotor1.configFactoryDefault();
        rightMotor1.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        rightMotor1.setNeutralMode(NeutralMode.Coast);

        rightMotor2 = rightMotor2_;
        rightMotor2.configFactoryDefault();
        rightMotor2.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        rightMotor2.setNeutralMode(NeutralMode.Coast);

        gearShifterSolenoid = new DoubleSolenoid(Context.gearShifterChannelA, Context.gearShifterChannelB);

        leftEncoderInterface = leftEncoderInterface_;
        leftEncoderInterface.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

        rightEncoderInterface = rightEncoderInterface_;
        leftEncoderInterface.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

        WPI_leftMotor1 = new WPI_TalonFX(Context.leftMotor1ID);
        WPI_leftMotor2 = new WPI_TalonFX(Context.leftMotor2ID);
        WPI_rightMotor1 = new WPI_TalonFX(Context.rightMotor1ID);
        WPI_rightMotor2 = new WPI_TalonFX(Context.rightMotor2ID);

        leftSide = new SpeedControllerGroup(WPI_leftMotor1, WPI_leftMotor2);
        rightSide = new SpeedControllerGroup(WPI_rightMotor1, WPI_rightMotor2);

        differentialDrive = new DifferentialDrive(leftSide, rightSide);
    }

    public void tankDrive(double leftPower, double rightPower) {
        leftMotor1.set(ControlMode.PercentOutput, -leftPower);
        leftMotor2.set(ControlMode.PercentOutput, -leftPower);
        rightMotor1.set(ControlMode.PercentOutput, rightPower);
        rightMotor2.set(ControlMode.PercentOutput, rightPower);
    }

    public void curvatureDrive(double power, double turn, boolean isQuickTurn) {
        super.curvatureDrive(differentialDrive, power, turn, isQuickTurn);
    }

     protected double getLeftTicks() {
        return leftEncoderInterface.getSelectedSensorPosition();
    }

    protected double getRightTicks() {
        return -rightEncoderInterface.getSelectedSensorPosition();
    }

    public double getLeftDist() {
        return (getLeftTicks() - startPosLeft) / Context.falconFXDriveTicksPerMeter;
    }

    public double getRightDist() {
        return (getRightTicks() - startPosRight) / Context.falconFXDriveTicksPerMeter;
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
            gearShifterSolenoid.set(Value.kReverse);
            break;
        case HIGH:
            gearShifterSolenoid.set(Value.kForward);
            break;
        }
        gear = desiredGear;
        System.out.println("Gear: " + gear);
    }

}