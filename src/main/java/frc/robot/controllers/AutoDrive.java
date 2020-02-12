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
    Trajectory trajectory;
    PIDCoefficients translationalPid;
    PIDCoefficients headingPid;
    TankPIDVAFollower follower;
    TankDrive tankDrive;

    public AutoDrive() {

        tankDrive = new TankDrive(Context.kV, Context.kA, Context.kStatic, Context.TRACK_WIDTH) {
            @Override
            protected double getRawExternalHeading() {
                // Also not using external heading for localization
                return 0.0;
            }
        
            @Override
            public void setMotorPowers(double leftPower, double rightPower) {
                // Not using their code to control the motors
            }
        
            @Override
            public List<Double> getWheelPositions() {
                List<Double> output = new ArrayList<Double>();
                output.add(Context.robotController.drivetrain.getLeftDist());
                output.add(Context.robotController.drivetrain.getRightDist());
                
                return output;
            }
        };

        resetLocalization();

        trajectory = generateTrajectory();
    }

    public void resetLocalization() {
        /* Disables the use of external heading */
        tankDrive.setLocalizer(new TankLocalizer(tankDrive, false));
    }

    public Trajectory generateTrajectory() {
        Path path = new PathBuilder(new Pose2d(0, 0, 0))
                .splineTo(new Pose2d(1, 0, 0))
                .build();

        return TrajectoryGenerator.INSTANCE.generateTrajectory(path, Context.BASE_CONSTRAINTS);
    }

    public Pose2d getPoseEstimate() {
        return tankDrive.getPoseEstimate();
    }

    public void updatePoseEstimate() {
        tankDrive.updatePoseEstimate();
        poseEstimate = tankDrive.getPoseEstimate();
    }

    public Pose2d getTankVelocityProfile(double time) {
        // Transforms the mecanum velocities to tank
        double robotVelocity = trajectory.getProfile().get(time).getV();
        return new Pose2d(robotVelocity, 0, trajectory.get(time).getHeading());
    }

    public void loop(double time) {
        updatePoseEstimate();

        Pose2d tank = getTankVelocityProfile(time);

        // System.out.println("goal: " + trajectory.get(time));
        List<Double> wheelVelocities = TankKinematics.robotToWheelVelocities(tank, Context.TRACK_WIDTH);

        System.out.println("Left: " + wheelVelocities.get(0) + ", Right: " + wheelVelocities.get(1));

        Context.robotController.drivetrain.tankDrivePID(wheelVelocities.get(1), wheelVelocities.get(0));
    }

}