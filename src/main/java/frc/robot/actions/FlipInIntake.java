package frc.robot.actions;

import frc.robot.util.*;

public class FlipInIntake extends Action {

    public void start() {
        super.start();
        
        Context.robotController.intake.flipOut();
        Context.robotController.nmfController.spinNMFIdle();

        markComplete();
    }

    public void loop()
    {
        
    }
}