package frc.robot.actions;

import frc.robot.util.*;

public class ReverseNMF extends Action {

    public void start() {
        super.start();
        
        if (!Context.robotController.nmfController.reversed) {
            Context.robotController.nmfController.spinNMFReverse();
        } else {
            Context.robotController.nmfController.reversed = false;
            switch(Context.robotController.nmfController.state) {
                case IDLE:
                    Context.robotController.nmfController.spinNMFIdle();
                    break;
                case INTAKING:
                    Context.robotController.nmfController.spinNMFIntaking();
                    break;
                case SHOOTING:
                    Context.robotController.nmfController.spinNMFShooting();
                    break;
            }
        }

        markComplete();
    }

    public void loop()
    {
        
    }
}