package frc.robot.actions;

import frc.robot.util.Context;

public class ShiftGears extends Action {
    public void loop()
    {
        //TODO: Move actual shifting logic to action
        if (!isComplete){
            Context.robotController.drivetrain.shiftGears();
            markComplete();
        }
    }
}