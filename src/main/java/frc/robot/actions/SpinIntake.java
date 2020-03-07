package frc.robot.actions;

import frc.robot.util.*;

public class SpinIntake extends Action {

    public void start() {
        super.start();
        
        if (Context.robotController.intake.intakingSpeed==0){
            Context.robotController.intake.beginIntaking();
        }
        else{
            Context.robotController.intake.stopIntaking();
        }
        

        markComplete();
    }

    public void loop()
    {
        
    }
}