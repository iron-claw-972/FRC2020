
package frc.robot.actions;

import frc.robot.util.Context;

public class Winch extends Action{

    public enum winchState {
        COIL,
        IDLE
    }

    public winchState state = winchState.IDLE;

    public Winch (Winch.winchState state_){
        state = state_;
    }

    @Override
    public void loop() {

        switch (state) {
            case COIL:
            Context.robotController.climber.coil(Context.coilSpeed);
            break;
            case IDLE:
            Context.robotController.climber.coil(0);
            break;
        }

        if(Context.robotController.climber.isWinchDone()) {
            markComplete();
        }
    }
}