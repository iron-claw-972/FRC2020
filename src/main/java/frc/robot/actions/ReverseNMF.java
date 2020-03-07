package frc.robot.actions;

import frc.robot.util.*;

public class ReverseNMF extends Action {

    public void start() {
        super.start();
        
        if (!Context.robotController.nmfController.reversed) {
            Context.robotController.nmfController.spinNMFReverse();
        } 
        
    }

    public void buttonReleased(){
        Context.robotController.nmfController.spinNMFForward();
        markComplete();
    }

    public void loop()
    {
        
    }
}