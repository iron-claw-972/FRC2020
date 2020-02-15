package frc.robot.util;

import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;

import frc.robot.controllers.RobotController;

public class Context {
    //----- Drivetrain Values -----
    public static final int leftMotor1ID = 0;
    public static final int leftMotor2ID = 1;
    public static final int rightMotor1ID = 2;
    public static final int rightMotor2ID = 3;

    /* TalonSRX and Encoder Drivetrain Values */
    public static final int leftEncoderChannelA = 0;
    public static final int leftEncoderChannelB = 1;
    public static final int rightEncoderChannelA = 2;
    public static final int rightEncoderChannelB = 3;
    public static final double amtTicksPerRotation = 2048;
    public static final double basicDriveWheelDiameter = 0.1; // meters
    public static final double basicDriveMotorGearRatio = (1/12.0) * (50.0) * (1/34.0) * (40.0) * amtTicksPerRotation; // amt ticks / wheel revs
    public static final double basicDriveTicksPerMeter = basicDriveMotorGearRatio / (basicDriveWheelDiameter * Math.PI);

    /* Neo Drivetrain values */
    public static final double neoDriveWheelDiameter = 0.1; // meters
    public static final double neoDriveMotorGearRatio = (1/12.0) * (50.0) * (1/34.0) * (40.0); // neo revs / wheel revs
    public static final double neoDriveTicksPerMeter = neoDriveMotorGearRatio / (neoDriveWheelDiameter * Math.PI);

    /* Falcon Drivetrain Values */
    public static final double falconFXTicksPerRotation = 4096;
    public static final double falconFXDriveWheelDiameter = 0.1524; // meters
    public static final double falconFXDriveTicksPerMeter = falconFXTicksPerRotation / (falconFXDriveWheelDiameter * Math.PI);
    public static final int leftEncoderInterfaceID = 11;
    public static final int rightEncoderInterfaceID = 10;
    
    //----- Human Input Device Values -----
    public static final int joystickID = 0;
    public static final int throttleAxisID = 1;
    public static final int shiftGearsButtonID = 6;
    public static final int yawAxisID = 2;
    public static final double joystickMaxDeadband = 0.05;
    public static final int inUseLengthMillis = 1000;
    public static final int climbButtonUp = 2;
    public static final int climbButtonDown = 1; 
    public static final int shoot = 3; 
    public static final int loopyLoopBreak = 7;
    public static final int toggleTrack = 4;

    /* Pneumatics Values */
    // public static final int compressorID = 40;
    public static final int gearShifterChannelA = 0;
    public static final int gearShifterChannelB = 1;

    //----- Robotcontroller Static Reference -----
    public static RobotController robotController;

    //----- Vision Alignment System -----
    public static final double alignmentTimeout = 3000; //after how many milliseconds stop the alignment loop and abort
    public static final double alignmentThreshold = 0.5; //within how many degrees can we say "good enough" aligning the robot
    public static final double ckStatic = 0.15;

    public static final double maxTurnPower = 2.0; // SAFETY

    //----- Climbing System -----
    public static final int climberMotorID = 10;
    
    public static final double coilSpeed = 0.5;
    public static final double uncoilSpeed = -0.5;

    //----- Acme Robotics Tank Drive -----
    // maxVel, maxAccel, maxJerk, maxAngVel, maxAngAccel, maxAngJerk
    public static final double maxDrivingSpeed = 2.0; // m/s
    public static final double maxDrivingAcceleration = 2.0; // m/s/s
    public static final double maxDrivingJerk = 4.0; // m/s/s/s
    public static final double maxTurningSpeed = 1.0; // radians/s
    /* Not used in trajectories */
    public static final double maxTurningAcceleration = 0;
    public static final double maxTurningJerk = 0;

    public static final DriveConstraints BASE_CONSTRAINTS = new DriveConstraints(
        maxDrivingSpeed, maxDrivingAcceleration, maxDrivingJerk, maxTurningSpeed, maxTurningAcceleration, maxTurningJerk
    );
    public static final double TRACK_WIDTH = 0.675; // meters
    /* Also not useful anywhere */
    public static final double kA = 0.0; // m/s/s
    public static final double kStatic = 0.0;
    public static final double kV = 0.0;

    //----- Time Function -----
    public static double getRelativeTime(double relativePoint) {
        return System.currentTimeMillis() - relativePoint;
    }
}
