package frc.robot.util;

import frc.robot.actions.Action;
import com.google.gson.Gson;

public class Trigger {
    public int buttonID;
    public Action action;
    private Gson gson;

    public Trigger (int buttonID, Action action) {
        this.buttonID = buttonID;
        this.action = action;
        gson = new Gson();
    }

    public void loop() {
        if(Context.robotController.driverJoystick.getButtonPressed(buttonID)){
            // The action is serialized to json and deselerialized in order to create a deep copy,
            // which is necessary in order to not edit the action passed to the trigger in the
            // trigger's initilzation. Without doing this, the trigger's action would be edited
            // and if the trigger were to be activated again, it would result in the addition
            // of a already complete action to the scheduler.
            String seralized = gson.toJson(this.action);
            Action clone = gson.fromJson(seralized, action.getClass());
            Context.robotController.parallelScheduler.add(clone);
        }
    }
}