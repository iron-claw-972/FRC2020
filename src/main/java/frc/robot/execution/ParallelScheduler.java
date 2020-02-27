package frc.robot.execution;

import frc.robot.actions.Action;
import java.util.*;

public class ParallelScheduler {
    public ArrayList<Action> currentActions = new ArrayList<Action>();

    public void add(Action action)
    {
        currentActions.add(action);
        action.start();;
    }

    public void add(Action[] actions)
    {
        for (Action action : actions)
        {
            add(action);
        }
    }

    public void loop() 
    {
        ArrayList<Action> completedActions = new ArrayList<Action>();
        for (Action action : currentActions) {
            action.loop();
            if (action.isComplete) {
                completedActions.add(action);
            }
        }

        if (completedActions.size() > 0) {
            for (Action action : completedActions) {
                currentActions.remove(action);
            }
        }
    }

    public boolean isDone()
    {
        return currentActions.size() == 0;
    }
}