package frc.robot.util;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;

import frc.robot.controllers.RobotController;

public class Context {
    //----- Drivetrain Values -----
    public static final int leftMotor1ID = 1;
    public static final int leftMotor2ID = 2;
    public static final int rightMotor1ID = 3;
    public static final int rightMotor2ID = 4;

    public static final double driveWheelDiameter = 0.1; // meters
    public static final double driveMotorGearRatio = (1/12.0) * (50.0) * (1/34.0) * (40.0); // in neo revs / wheel revs
    public static final double driveTicksPerMeter = driveMotorGearRatio / (driveWheelDiameter * Math.PI);
    
    //----- Human Input Device Values -----
    public static final int joystickID = 0;
    public static final int throttleAxisID = 4;
    public static final int yawAxisID = 1;
    public static final double joystickMaxDeadband = 0.05;
    public static final int inUseLengthMillis = 1000;

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
    public static final double maxDrivingSpeed = 20.0; // cm/s
    public static final double maxturningSpeed = 1.0; // radians/s ?
    public static final DriveConstraints BASE_CONSTRAINTS = new DriveConstraints(
        maxDrivingSpeed, 40.0, 80.0, maxturningSpeed, 2.0, 4.0
    );
    public static final double TRACK_WIDTH = 0.675; // meters
    public static final double kA = 5;
    public static final double kStatic = 5;
    public static final double kV = 5;

    //----- Time Function -----
    public static double getRelativeTime(double relativePoint) {
        return System.currentTimeMillis() - relativePoint;
    }
}
