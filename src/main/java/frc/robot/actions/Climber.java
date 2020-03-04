package frc.robot.actions;

import frc.robot.util.Context;

public class Climber extends Action{
    @Override
    public void loop() {
        Context.robotController.climber.loop();
        
        if(Context.robotController.climber.isClimbDone()) {
            markComplete();
        }
    }
}