package frc.robot.controllers;

import java.util.List;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.drive.DriveSignal;
import com.acmerobotics.roadrunner.followers.TankPIDVAFollower;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.kinematics.Kinematics;
import com.acmerobotics.roadrunner.kinematics.TankKinematics;
import com.acmerobotics.roadrunner.path.Path;
import com.acmerobotics.roadrunner.path.PathBuilder;
import com.acmerobotics.roadrunner.profile.MotionProfile;
import com.acmerobotics.roadrunner.profile.MotionProfileGenerator;
import com.acmerobotics.roadrunner.profile.MotionState;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryGenerator;
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;

import frc.robot.util.Context;

public class AutoDrive
{
    public MotionProfile profile;
    public Pose2d poseEstimate;
    public DriveSignal signal;
    public Path path;
    public DriveConstraints constraints;
    public Trajectory trajectory;
    public PIDCoefficients translationalPid;
    public PIDCoefficients headingPid;
    public TankPIDVAFollower follower;

    public AutoDrive()
    {
        profile = MotionProfileGenerator.generateSimpleMotionProfile(
            new MotionState(0.0, 0.0, 0.0),
            new MotionState(200.0, 0.0, 0.0),
            50.0,
            40.0,
            100.0
        );

        System.out.println(profile.get(0.5));

        path = new PathBuilder(new Pose2d(3.0, 3.0, 0.0))
            .splineTo(new Pose2d(-3.0, -3.0, 0.0))
            .lineTo(new Vector2d(1.0, 1.0))
            .build();

        constraints = new DriveConstraints(20.0, 40.0, 80.0, 1.0, 2.0, 4.0);
        trajectory = TrajectoryGenerator.INSTANCE.generateTrajectory(path, constraints);

        translationalPid = new PIDCoefficients(5.0, 0.0, 0.0);
        headingPid = new PIDCoefficients(2.0, 0.0, 0.0);
        follower = new TankPIDVAFollower(translationalPid, headingPid);

        follower.followTrajectory(trajectory);
    }

    public void startSpline()
    {
        follower.followTrajectory(trajectory);
    }

    public void loop(double t)
    {
        // double scaledVel = profile.get(t).getV()/100.0;

        DriveSignal ds = follower.update(new Pose2d(0, 0));
        List<Double> velocities = TankKinematics.robotToWheelVelocities(ds.getVel(), Context.TRACK_WIDTH);
        List<Double> acceleraations = TankKinematics.robotToWheelAccelerations(ds.getAccel(), Context.TRACK_WIDTH);
        List<Double> powers = Kinematics.calculateMotorFeedforward(velocities, acceleraations, Context.kV, Context.kA, Context.kStatic);
        System.out.println("Power1: " + powers.get(0) + ", Power2: " + powers.get(1) + ", Len: " + powers.size());
        System.out.println(ds + ", error: " + follower.getLastError() + ", vels: " + velocities);

        // System.out.println("Velocity: " + scaledVel);
        // Context.robotController.drivetrain.arcadeDrive(limitVal(scaledVel, -0.4, 0.4), 0);
    }

    // public void init()
    // {
    //     for(double t = 0.1; t < 4; t += 0.1) {
    //         // poseEstimate = trajectory.get(t);
    //         // System.out.println("Pose Estimate: " + poseEstimate);
    //         // signal = follower.update(poseEstimate);
    //         // System.out.println("Signal: " + signal);
    //         System.out.println("Time: " + t + " ; " + profile.get(t) + " ; Velocity: " + profile.get(t).getV());
    //     }
    // }

    public static double limitVal(double input, double min, double max)
    {
        if (input > max)
        {
            return max;
        }
        else if (input < min)
        {
            return min;
        }

        return input;
    }
}