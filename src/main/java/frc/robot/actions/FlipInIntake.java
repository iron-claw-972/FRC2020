package frc.robot.actions;

import frc.robot.util.*;
import frc.robot.actions.*;

public class FlipInIntake extends Action {

    public void start() {
        super.start();
        
        Context.robotController.intake.flipIn();
        Context.robotController.nmfController.spinNMFIntaking();
        
    }

    public void loop()
    {
        if ((System.currentTimeMillis()-startTime)>3000){
            Context.robotController.nmfController.spinNMFIdle();
            markComplete();
        }
    }
}