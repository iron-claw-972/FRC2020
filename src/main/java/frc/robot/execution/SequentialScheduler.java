package frc.robot.execution;

import frc.robot.actions.Action;
import java.util.*;

public class SequentialScheduler {
    public ArrayList<Action> pendingActions = new ArrayList<Action>();
    public Action currentAction = null;
    public long actionStart = 0L;

    public void add(Action action)
    {
        pendingActions.add(action);
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
        if (currentAction != null) {
            if (!currentAction.isComplete) {
                currentAction.loop();
            }
        }
        
        if (pendingActions.size() == 0) {
            return;
        }

        if (currentAction == null || currentAction.isComplete) {
            currentAction = pendingActions.remove(0);
            actionStart = System.currentTimeMillis();
            currentAction.start();
        }
    }

    public boolean isDone()
    {
        return pendingActions.size() == 0 && (currentAction == null || currentAction.isComplete);
    }
}