
package frc.robot.actions;

import frc.robot.util.Context;

public class Climber extends Action{
    public enum ClimbActions
    {
        UP,
        DOWN,
        IDLE
    }

    public ClimbActions whatState() {
        ClimbActions state = ClimbActions.IDLE;        
        if (Context.robotController.driverJoystick.getClimbU()) {
            state = ClimbActions.UP;  
        }
        else if (Context.robotController.driverJoystick.getClimbD()) {
            state = ClimbActions.DOWN;
        }
        else {
            state = ClimbActions.IDLE;
        }
        return state;   
    }

    @Override
    public void loop() {
        ClimbActions state = whatState();
        switch (state) {
            case UP:
            Context.robotController.climber.up();
            break;
            case DOWN:
            Context.robotController.climber.down();
            break;
            case IDLE:
            Context.robotController.climber.idle();
            break;
        }
        if(Context.robotController.climber.isClimbDone()) {
            markComplete();
        }
    }
}