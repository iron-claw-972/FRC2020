
package frc.robot.actions;

import frc.robot.util.Context;

public class Climb extends Action{

    public enum ClimbActions {
        UP,
        DOWN,
        IDLE
    }

    public ClimbActions state = ClimbActions.IDLE;

    public Climb (Climb.ClimbActions state_) {
        state = state_;
    }

    public void buttonReleased()
    {
        state = ClimbActions.IDLE;
        markComplete();
    }

    @Override
    public void loop() {
        switch (state) {
            case UP:
            Context.robotController.climber.up();
            break;
            case DOWN:
            Context.robotController.climber.down();
            break;
            case IDLE:
            Context.robotController.climber.telescopeMove(0);
            break;
        }
        
        if(Context.robotController.climber.isClimbDone()) {
            markComplete();
        }
    }
}