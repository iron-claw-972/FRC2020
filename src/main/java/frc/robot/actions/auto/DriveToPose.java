package frc.robot.actions.auto;

import frc.robot.actions.*;
import frc.robot.util.Context;

import com.acmerobotics.roadrunner.geometry.*;

public class DriveToPose extends Action {
    public double x, y, heading;

    public DriveToPose(double x, double y, double heading) {
        super();
        this.x = x;
        this.y = y;
        this.heading = heading;
    }

    public void start() {
        super.start();
        Context.robotController.autoDrive.generateTrajectory(new Pose2d(x, y, heading));
    }

    public void loop() {
        Context.robotController.autoDrive.loop((System.currentTimeMillis()-startTime)/1000.0);

        if (Context.robotController.autoDrive.done) {
            markComplete();
        }
    }
}