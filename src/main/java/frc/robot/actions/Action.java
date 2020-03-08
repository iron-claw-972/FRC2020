package frc.robot.actions;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class Action{
    public boolean isComplete = false;
    public long startTime = 0;

    public Action() {};
    
    @OverridingMethodsMustInvokeSuper
    public void start()
    {
        startTime = System.currentTimeMillis();
    }

    public abstract void loop();

    public void markComplete()
    {
        isComplete = true;
    }
    
    public void buttonReleased(){

    }
}