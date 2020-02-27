package frc.robot.actions;

public abstract class Action{
    public boolean isComplete = false;
    public long startTime = 0;

    public Action() {};
    
    public void start()
    {
        startTime = System.currentTimeMillis();
    }

    public abstract void loop();

    public void markComplete()
    {
        isComplete = true;
    }
}