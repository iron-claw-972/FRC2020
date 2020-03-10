package frc.robot.controllers.drive;

import com.revrobotics.CANSparkMax;

import frc.robot.util.*;

public class NeoDrivetrain extends Drivetrain {
    private CANSparkMax leftMotor1, leftMotor2, rightMotor1, rightMotor2;
    private static PIDF leftDrivePIDF = new PIDF(0.1, 0, 0, 0.18);
    private static PIDF rightDrivePIDF = new PIDF(0.1, 0, 0, 0.18);

    // Gearbox Calculations
    private final double neoDriveWheelDiameter = 0.1; // meters
    private final double neoDriveMotorGearRatio = (1/12.0) * (50.0) * (1/34.0) * (40.0); // neo revs / wheel revs
    private final double neoDriveTicksPerMeter = neoDriveMotorGearRatio / (neoDriveWheelDiameter * Math.PI);
    
    public NeoDrivetrain(CANSparkMax leftMotor1_, CANSparkMax rightMotor1_) {
        super(leftDrivePIDF, rightDrivePIDF);

        leftMotor1 = leftMotor1_;
        leftMotor1.restoreFactoryDefaults();
        // leftMotor2 = leftMotor2_;
        // leftMotor2.restoreFactoryDefaults();
        rightMotor1 = rightMotor1_;
        rightMotor1.restoreFactoryDefaults();
        // rightMotor2 = rightMotor2_;
        // rightMotor2.restoreFactoryDefaults();
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
        return rawCount / neoDriveTicksPerMeter;
    }

    public double getRightDist() {
        double rawCount = getRightTicks() - startPosRight;
        return rawCount / neoDriveTicksPerMeter;
    }

    /**
     * Neos do not have current limiting capabilities!!
     * @param amps
     * @param activationAmps
     * @param enableCurrentLimiting
     */
    public void setCurrentLimiting(double amps, double activationAmps, boolean enableCurrentLimiting) {
        System.out.println("Neos are not capable of current limiting!");
    }
}