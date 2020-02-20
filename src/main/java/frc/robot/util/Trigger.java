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

    public void loop()
    {
        if(Context.robotController.driverJoystick.getButtonPressed(buttonID)){
            String seralized = gson.toJson(this.action);
            Action clone = gson.fromJson(seralized, action.getClass());
            Context.robotController.parallelScheduler.add(clone);
        }
    }
}