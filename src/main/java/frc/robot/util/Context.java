package frc.robot.util;

import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;

import java.util.*;

import frc.robot.controllers.RobotController;

public class Context {
    //----- Static References -----
    public static RobotController robotController;

    //----- Drivetrain Values -----
    public static final double maxDrivingSpeed = 2.0; // m/s
    public static final double maxTheoreticalDrivingSpeed = 3.0; // m/s
    public static final int leftMotor1ID = 0;
    public static final int leftMotor2ID = 1;
    public static final int rightMotor1ID = 14;
    public static final int rightMotor2ID = 15;
  
    //----- Flywheel Shooter Values -----
    public static final int shooterMotorID = 2;
    public static final int FALCON_ENCODER_CPR = 2048;
  
    /* TalonSRX and Encoder Drivetrain Values */
    public static final int leftEncoderChannelA = 0;
    public static final int leftEncoderChannelB = 1;
    public static final int rightEncoderChannelA = 2;
    public static final int rightEncoderChannelB = 3;

    // Falcon Drivetrain Values
    public static final int leftEncoderInterfaceID = 8;
    public static final int rightEncoderInterfaceID = 10;
    public static final int gearShifterChannelA = 0;
    public static final int gearShifterChannelB = 1;
    public static final int driveTrainCooling = 5;

    //----- Human Input Device Values -----
    public static final int driverJoystickID = 0;
    public static final int operatorJoystickID = 1;
    public static final double joystickMaxDeadband = 0.05;
    public static final int inUseLengthMillis = 1000;

    // Joystick Axis
    public static final int yawAxisID = 4;
    public static final int throttleAxisID = 1;

    // Button IDs
    public static final int climbButtonDown = 2;
    public static final int climbButtonUp = 4;
    public static final int shoot = 3; 
    public static final int toggleTrack = 6; //will be one right bumper of driver joystick
    public static final int shiftGearsButtonID = 1;
    public static final int quickTurnLeftTriggerID = 2;
    public static final int quickTurnRightTriggerID = 3;
    public static final int loopyLoopBreak = 7;

    public static final int flipOutIntakeButtonID = 6;
    public static final int flipInIntakeTriggerID = 2;
    public static final int reverseNMFDirectionDpadID = 3;
    public static final int reverseIntakeDirectionDpadID = 4;
    public static final int spinNMFToggleButtonID = 2;



    //----- Vision Alignment System -----
    public static final double alignmentTimeout = 3000; //after how many milliseconds stop the alignment loop and abort
    public static final double alignmentThreshold = 0.5; //within how many degrees can we say "good enough" aligning the robot
    public static final double ckStatic = 0.15;
    public static final double maxTurnPower = 2.0; // SAFETY

    //----- Intake System -----
    public static final int intakeFlipChannelA = 2;
    public static final int intakeFlipChannelB = 3;
    public static final int intakeMotorId = 8;

    // NMF Values
    public static final int nmfSparkID = 4;
    public static final int omniSparkID = 5;

    //----- Climbing System -----
    public static final int climberMotorID = 6;
    public static final int coilMotor1ID = 2;
    public static final int coilMotor2ID = 3;
    public static final double coilSpeed = 0.5;
    public static final double uncoilSpeed = -0.5;

    //----- Acme Robotics Tank Drive -----
    // maxVel, maxAccel, maxJerk, maxAngVel, maxAngAccel, maxAngJerk
    public static final double maxAutoDrivingSpeed = 2.0; // m/s
    public static final double maxDrivingAcceleration = 2.0; // m/s/s
    public static final double maxDrivingJerk = 3.0; // m/s/s/s
    public static final double maxTurningSpeed = 1.5; // radians/s
    public static final double TRACK_WIDTH = 0.675; // meters
    /* Not used in trajectories */
    public static final double maxTurningAcceleration = 0;
    public static final double maxTurningJerk = 0;
    /* Also not useful anywhere */
    public static final double kA = 0.0; // m/s/s
    public static final double kStatic = 0.0;
    public static final double kV = 0.0;

    //----- Superstructure/Game Measurements -----
    public static final double M_FLYWHEEL_RADIUS = 0.0508;
    public static final double M_BALL_DIAMETER = 0.0762;

    //----- Time Function -----
    public static double getRelativeTimeSeconds(double relativePoint) {
        return System.currentTimeMillis()/1000 - relativePoint;
    }

    //----- Camera Constants -----
    public static final int cameraWidth = 1; //To be changed
    public static final int cameraHeight = 1;
    public static final int cameraFPS = 1;

    //----- WOF Colors -----
    public static char WOFTargetColor = 'N'; //N for none. B,G,R,Y for other colors.

	
    public static final Map<Character, String> WOFColors = Map.of( //Map of Driver Station Colors
        'B', "#0000FF",
        'G', "#00FF00",
        'R', "#FF0000",
        'Y', "#FFFF00",
        'N', "#DDDDDD");
    public static void setWOFTargetColor() { //Sets WOFTargetColor based on Driver Station
        if(robotController.driverStation.getGameSpecificMessage().length() > 0) {
            System.out.println(robotController.driverStation.getGameSpecificMessage().charAt(0));
            char color = robotController.driverStation.getGameSpecificMessage().charAt(0);
            if (WOFColors.containsKey(color)) {
                WOFTargetColor = color;
            } else { //Corrupted
                WOFTargetColor = 'N';
            }
        } 
        else {
            WOFTargetColor = 'N';
        }
    }
}
