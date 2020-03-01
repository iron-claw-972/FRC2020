package frc.robot.controllers.auto;

import java.util.List;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.kinematics.*;
import com.acmerobotics.roadrunner.path.Path;
import com.acmerobotics.roadrunner.path.PathBuilder;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryGenerator;

import frc.robot.util.Context;

/**
 * To limit drive signal, change max drive velocity in Context
 */
public class AutoDrive {
    Trajectory trajectory;
    public Localizer localizer;

    public AutoDrive() {
        localizer = new Localizer(Context.TRACK_WIDTH);
        trajectory = generateTrajectory();
    }

    public Trajectory generateTrajectory() {
        Path path = new PathBuilder(new Pose2d(0, 0, 0))
                .splineTo(new Pose2d(2, -2, 0))
                .build();

        return TrajectoryGenerator.INSTANCE.generateTrajectory(path, Context.BASE_CONSTRAINTS);
    }

    public List<Double> getWheelVelocities(double time) {
        Pose2d robotPose = trajectory.get(time);
        Pose2d fieldVel = trajectory.velocity(time);
    	Pose2d robotVel = Kinematics.fieldToRobotVelocity(robotPose, fieldVel);
    	return TankKinematics.robotToWheelVelocities(robotVel, Context.TRACK_WIDTH);
    }

    public void loop(double time) {
        localizer.update();

        /* Hacky fix for autism roadrunner error */
        if (time == trajectory.duration()/2) {
            time += 0.001;
        }

        // System.out.println("goal: " + trajectory.get(time));
        List<Double> wheelVelocities = getWheelVelocities(time);

        // Context.robotController.drivetrain.printWheelVelocities();
        // System.out.println("LeftDes: " + wheelVelocities.get(0) + "| RightDes: " + wheelVelocities.get(1));
        Context.robotController.drivetrain.tankDrivePID(wheelVelocities.get(0), wheelVelocities.get(1));
    }

}