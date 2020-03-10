package frc.robot.util;

import frc.robot.actions.Action;
import frc.robot.controllers.*;
import com.google.gson.Gson;

public class Trigger {
    private CompetitionJoystick joystick;
    private Type type;
    public int id;
    public Action action;
    private Gson gson;
    private Action clone = null;

    public static enum Type {
        BUTTON, AXIS, DPAD
    }

    public Trigger(CompetitionJoystick joystick, Type type, int id, Action action) {
        this.joystick = joystick;
        this.type = type;
        this.id = id;
        this.action = action;
        gson = new Gson();
    }

    public void loop() {
        if(type == Type.BUTTON ? joystick.getButtonPressed(id) : ( type == Type.AXIS ? joystick.getAxisPressed(id) : joystick.getDpadPressed(id))){
            // The action is serialized to json and deselerialized in order to create a deep copy,
            // which is necessary in order to not edit the action passed to the trigger in the
            // trigger's initilzation. Without doing this, the trigger's action would be edited
            // and if the trigger were to be activated again, it would result in the addition
            // of a already complete action to the scheduler.
            String seralized = gson.toJson(this.action);
            clone = gson.fromJson(seralized, action.getClass());
            Context.robotController.parallelScheduler.add(clone);
        }

        if (clone != null && !clone.isComplete){
            switch (type) {
            case BUTTON:
                if (joystick.getButtonReleased(id)){
                    clone.buttonReleased();
                    clone = null;
                }
                break;
            case AXIS:
                if (joystick.getAxisReleased(id)){
                    clone.buttonReleased();
                    clone = null;
                }
                break;
            case DPAD:
                if (joystick.getDpadReleased(id)){
                    clone.buttonReleased();
                    clone = null;
                }
                break;
            }
        }
        
    }
}