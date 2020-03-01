package frc.robot.actions;

import java.util.*;


public class ParallelAction extends Action {
    public ArrayList<Action> actions = new ArrayList<Action>();

    public ParallelAction(Action... actionArray) {
        for (Action action : actionArray) {
            actions.add(action);
        }
    }

    public void start() {
        super.start();
        for (Action action : actions) {
            action.start();
        }
    }

    public void loop() {
        boolean allComplete = true;
        ArrayList<Action> completedActions = new ArrayList<Action>();

        for (Action action : actions) {
            action.loop();
            if (action.isComplete) {
                completedActions.add(action);
            } else {
                allComplete = false;
            }
        }

        if (completedActions.size() > 0) {
            for (Action action : completedActions) {
                actions.remove(action);
            }
        }

        if (allComplete) {
            markComplete();
        }
    }

    public void add(Action action) {
        actions.add(action);
    }

    public void add(ArrayList<Action> actionsList) {
        for (Action action : actionsList) {
            add(action);
        }
    }
}