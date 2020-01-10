package frc.robot.util;

import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;

import frc.robot.controllers.RobotController;

// import static org.firstinspires.ftc.teamcode.drive.DriveConstants.BASE_CONSTRAINTS;
// import static org.firstinspires.ftc.teamcode.drive.DriveConstants.TRACK_WIDTH;
// import static org.firstinspires.ftc.teamcode.drive.DriveConstants.kA;
// import static org.firstinspires.ftc.teamcode.drive.DriveConstants.kStatic;
// import static org.firstinspires.ftc.teamcode.drive.DriveConstants.kV;

public class Context {
    //----- Drivetrain Values -----
    public static final int leftMotor1ID = 1;
    public static final int leftMotor2ID = 2;
    public static final int rightMotor1ID = 3;
    public static final int rightMotor2ID = 4;

    // Diameter of wheel (10 cm) * Pi (3.14) * Clicks per rev (0.2)
    public static final double driveClickToCm = 2*Math.PI;
    
    //----- Human Input Device Values -----
    public static final int joystickID = 0;
    public static final double joystickMaxDeadband = 0.05;

    //----- Robotcontroller Static Reference -----
    public static RobotController robotController;

    //----- Acme Robotics Tank Drive -----
    // maxVel, maxAccel, maxJerk, maxAngVel, maxAngAccel, maxAngJerk
    public static final DriveConstraints BASE_CONSTRAINTS = new DriveConstraints(
        20.0, 40.0, 80.0, 1.0, 2.0, 4.0
    );
    public static final double TRACK_WIDTH = 5;
    public static final double kA = 5;
    public static final double kStatic = 5;
    public static final double kV = 5;
}