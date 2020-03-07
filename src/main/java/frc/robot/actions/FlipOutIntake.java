package frc.robot.actions;

import frc.robot.util.*;

public class FlipOutIntake extends Action {

    public void start() {
        super.start();
        
        Context.robotController.intake.flipOut();
        Context.robotController.intake.beginIntaking();
        Context.robotController.nmfController.spinNMFIntaking();

        markComplete();
    }

    public void loop()
    {
        
    }
}