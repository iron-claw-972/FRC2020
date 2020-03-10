package frc.robot.actions;

import frc.robot.util.*;
import frc.robot.actions.*;

public class NMFSlowSpin extends Action {

    public void start() {
        super.start();
        
        Context.robotController.nmfController.spinNMFIdle();
        markComplete();
        
    }

    public void loop()
    {
    }
}