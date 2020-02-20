package frc.robot.actions;

public class Wait extends Action{
    public long duration;

    public Wait (long duration)
    {
        this.duration = duration;
    }

    public void loop()
    {
        if(System.currentTimeMillis() - startTime > duration)
        {
            markComplete();
        }
    }
}