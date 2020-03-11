
package frc.robot.actions;

import frc.robot.util.Context;

public class Winch extends Action{

    public enum WinchState {
        COIL,
        IDLE
    }

    public WinchState state = WinchState.IDLE;

    public Winch (Winch.WinchState state_){
        state = state_;
    }

    public void buttonReleased()
    {
        state = WinchState.IDLE;
        markComplete();
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