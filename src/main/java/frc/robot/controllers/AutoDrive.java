package frc.robot.controllers;

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
                Context.robotController.drivetrain.tankDrive(leftPower, -rightPower);
            }
        
            @Override
            public List<Double> getWheelPositions() {
                return Context.robotController.drivetrain.getWheelPositions();
            }
        };

        /* Disables the use of external heading */
        tankDrive.setLocalizer(new TankLocalizer(tankDrive, false));

        // profile = MotionProfileGenerator.generateSimpleMotionProfile(
        //     new MotionState(0.0, 0.0, 0.0),
        //     new MotionState(200.0, 0.0, 0.0),
        //     50.0,
        //     40.0,
        //     100.0
        // );

        path = new PathBuilder(new Pose2d(0, 0, 0))
            .splineTo(new Pose2d(1.5, 1.5, 0))
            // .lineTo(new Vector2d(3.0, 1.5))
            .build();

        trajectory = TrajectoryGenerator.INSTANCE.generateTrajectory(path, Context.BASE_CONSTRAINTS);

        translationalPid = new PIDCoefficients(5.0, 0.0, 0.0);
        headingPid = new PIDCoefficients(2.0, 0.0, 0.0);
        follower = new TankPIDVAFollower(translationalPid, headingPid);
    }

    public void startSpline() {
        follower.followTrajectory(trajectory);
    }

    public void loop(double t) {
        tankDrive.updatePoseEstimate();
        poseEstimate = tankDrive.getPoseEstimate();

        DriveSignal signal = follower.update(poseEstimate);
        
        List<Double> velocities = TankKinematics.robotToWheelVelocities(signal.getVel(), Context.TRACK_WIDTH);
        List<Double> accelerations = TankKinematics.robotToWheelAccelerations(signal.getAccel(), Context.TRACK_WIDTH);
        List<Double> powers = Kinematics.calculateMotorFeedforward(velocities, accelerations, Context.kV, Context.kA, Context.kStatic);

        // tankDrive.setDriveSignal(signal);

        System.out.println("Drive Powers: (" + powers.get(0) + ", " + powers.get(1) + ")");

        Context.robotController.drivetrain.tankDrive(powers.get(0), powers.get(1));
        // Context.robotController.drivetrain.tankDrivePID(velocities.get(0), velocities.get(1));
    }

}