package frc.robot.actions.auto;

import frc.robot.actions.*;
import frc.robot.util.Context;

import com.acmerobotics.roadrunner.geometry.*;

public class Forward extends Action {
    public double meters;

    public Forward(double meters) {
        super();
        this.meters = meters;
    }

    public void start() {
        super.start();
        Context.robotController.autoDrive.generateTrajectory(new Pose2d(meters, 0, 0));
    }

    public void loop() {
        Context.robotController.autoDrive.loop((System.currentTimeMillis()-startTime)/1000.0);

        if (Context.robotController.autoDrive.done) {
            markComplete();
        }
    }
}