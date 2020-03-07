package frc.robot.actions;

import frc.robot.util.*;

public class ReverseIntake extends Action {

    public void start() {
        super.start();
        
        if (Context.robotController.intake.intakingSpeed<0){
            Context.robotController.intake.beginIntaking();
        }
        else{
            Context.robotController.intake.reverseIntaking();
        }
        

        markComplete();
    }
    
    public void buttonReleased(){
        Context.robotController.intake.stopIntaking();
    }

    public void loop()
    {
        
    }
}