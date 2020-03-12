package frc.robot.controllers.auto;

import java.util.List;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.kinematics.*;
import com.acmerobotics.roadrunner.path.Path;
import com.acmerobotics.roadrunner.path.PathBuilder;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryGenerator;
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;

import frc.robot.util.*;

/**
 * To limit drive signal, change max drive velocity in Context
 */
public class AutoDrive {
    Trajectory trajectory;
    public Localizer localizer;

    public boolean done = false;

    private static final DriveConstraints BASE_CONSTRAINTS = new DriveConstraints(
        Context.maxDrivingSpeed,
        Context.maxDrivingAcceleration,
        Context.maxDrivingJerk,
        Context.maxTurningSpeed,
        Context.maxTurningAcceleration,
        Context.maxTurningJerk
    );

    public AutoDrive() {
        localizer = new Localizer(Context.TRACK_WIDTH);
        
    }

    public void generateTrajectory(Pose2d pose) {
        Path path = new PathBuilder(localizer.getPoseEstimate())
                .splineTo(pose)
                .build();

        done = false;
        trajectory = TrajectoryGenerator.INSTANCE.generateTrajectory(path, BASE_CONSTRAINTS);
    }

    public List<Double> getWheelVelocities(double time) {
        Pose2d robotPose = trajectory.get(time);
        Pose2d fieldVel = trajectory.velocity(time);
    	Pose2d robotVel = Kinematics.fieldToRobotVelocity(robotPose, fieldVel);
    	return TankKinematics.robotToWheelVelocities(robotVel, Context.TRACK_WIDTH);
    }

    public void loop(double time) {


        //This is run when the trajectory is done
        if (time >= trajectory.duration()) {
            System.out.println(time + "    " + trajectory.duration());
            //done = true;
            Context.robotController.drivetrain.tankDrivePIDF(0, 0); 
            return;
        }

        localizer.update();

        /* Hacky fix for autism roadrunner error */
        if (time == trajectory.duration()/2) {
            time += 0.001;
        }

        // System.out.println("goal: " + trajectory.get(time));
        List<Double> wheelVelocities = getWheelVelocities(time);

        // Context.robotController.drivetrain.printWheelVelocities();
        // System.out.println("LeftDes: " + wheelVelocities.get(0) + "| RightDes: " + wheelVelocities.get(1));
        Context.robotController.drivetrain.tankDrivePIDF(wheelVelocities.get(0), wheelVelocities.get(1));
    }

}
