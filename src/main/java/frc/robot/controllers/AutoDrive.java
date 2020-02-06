package frc.robot.controllers;

import java.util.ArrayList;
import java.util.List;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.drive.DriveSignal;
import com.acmerobotics.roadrunner.drive.TankDrive;
import com.acmerobotics.roadrunner.drive.TankDrive.TankLocalizer;
import com.acmerobotics.roadrunner.followers.TankPIDVAFollower;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.kinematics.*;
import com.acmerobotics.roadrunner.path.Path;
import com.acmerobotics.roadrunner.path.PathBuilder;
import com.acmerobotics.roadrunner.profile.MotionProfile;
import com.acmerobotics.roadrunner.profile.MotionState;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryGenerator;

import frc.robot.util.Context;

/**
 * To limit drive signal, change max drive velocity in Context
 */
public class AutoDrive {
    MotionProfile profile;
    Pose2d poseEstimate = new Pose2d(0, 0);
    DriveSignal signal;
    Path path;
    Trajectory trajectory;
    PIDCoefficients translationalPid;
    PIDCoefficients headingPid;
    TankPIDVAFollower follower;
    TankDrive tankDrive;

    public AutoDrive() {

        tankDrive = new TankDrive(Context.kV, Context.kA, Context.kStatic, Context.TRACK_WIDTH) {
            @Override
            protected double getRawExternalHeading() {
                return 0.0;
            }
        
            @Override
            public void setMotorPowers(double leftPower, double rightPower) {
                Context.robotController.drivetrain.tankDrive(leftPower, rightPower);
            }
        
            @Override
            public List<Double> getWheelPositions() {
                ArrayList<Double> output = new ArrayList<Double>();
                output.add(Context.robotController.drivetrain.getLeftDist());
                output.add(Context.robotController.drivetrain.getRightDist());
                
                return output;
            }
        };

        /* Disables the use of external heading */
        tankDrive.setLocalizer(new TankLocalizer(tankDrive, false));

        path = new PathBuilder(new Pose2d(0, 0, 0))
            .splineTo(new Pose2d(3, 0, 0))
            // .lineTo(new Vector2d(3.0, 1.5))
            .build();

        trajectory = TrajectoryGenerator.INSTANCE.generateTrajectory(path, Context.BASE_CONSTRAINTS);
    }

    public void loop(double t) {
        tankDrive.updatePoseEstimate();
        poseEstimate = tankDrive.getPoseEstimate();

        System.out.println("pose: " + poseEstimate);

        MotionState state = trajectory.getProfile().get(t);
        double robotVelocity = state.getV();
        Pose2d tank = new Pose2d(robotVelocity, 0, trajectory.get(t).getHeading());
        System.out.println("goal: " + trajectory.get(t));
        List<Double> wheelVelocities = TankKinematics.robotToWheelVelocities(tank, Context.TRACK_WIDTH);

        //System.out.println("Left: " + wheelVelocities.get(0) + ", Right: " + wheelVelocities.get(1));

        Context.robotController.drivetrain.tankDrivePID(wheelVelocities.get(1), wheelVelocities.get(0));
        // Context.robotController.drivetrain.tankDrivePID(velocities.get(0), velocities.get(1));
    }

}