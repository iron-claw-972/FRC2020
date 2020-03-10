package frc.robot.actions;

import frc.robot.util.*;
import frc.robot.actions.*;

public class Shoot extends Action {

    public void start() {
        super.start();
        
        Context.robotController.intake.flipIn();
        Context.robotController.intake.stopIntaking();
        Context.robotController.nmfController.spinOmni();
        Context.robotController.nmfController.spinNMFShooting();
    }

    public void buttonReleased() {
        Context.robotController.nmfController.spinNMFIdle();
        Context.robotController.nmfController.stopOmni();
        markComplete();
    }

    public void loop() {}
}