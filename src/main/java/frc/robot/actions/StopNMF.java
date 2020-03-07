package frc.robot.actions;

import frc.robot.controllers.NMFController;
import frc.robot.util.*;

public class StopNMF extends Action {

    public void start() {
        super.start(); //its a toggle, when it is stopped, it starts and vice versa
        if (Context.robotController.nmfController.NMFtargetSpeed == 0){
            Context.robotController.nmfController.startNMF();
        }
        else{
            Context.robotController.nmfController.stopNMF();
        }
        markComplete();
    }

    public void loop()
    {
        
    }
}