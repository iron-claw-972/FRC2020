package frc.robot.actions;

import frc.robot.util.*;
import frc.robot.actions.*;

public class FlipInIntake extends Action {

    public void start() {
        super.start();
        
        Context.robotController.intake.flipIn();
        Context.robotController.intake.stopIntaking();
        Context.robotController.nmfController.spinNMFIntaking();
        
    }

    public void loop()
    {
        if ((System.currentTimeMillis()-startTime)>3000){  //after 3 seconds spin NMF at idle speed
            Context.robotController.nmfController.spinNMFIdle();
            markComplete();
        }
    }
}