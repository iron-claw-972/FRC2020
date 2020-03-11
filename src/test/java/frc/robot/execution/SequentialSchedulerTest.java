package frc.robot.execution;

import static org.junit.Assert.*;

import org.junit.*;

import frc.robot.actions.Action;

public class SequentialSchedulerTest
{
    public SequentialScheduler sequentialScheduler;

    public class Complete extends Action{
        public void loop()
        {
            markComplete();
        }
    }

    @Before
    public void setup() {
        sequentialScheduler = new SequentialScheduler();
    }
    /*
    @Test
    public void testComplete() {
        Action action0 = new Complete();

        assertFalse(action0.isComplete);

        sequentialScheduler.add(action0);

        sequentialScheduler.loop();

        assertTrue(action0.isComplete);

        assertTrue(sequentialScheduler.isDone());
    }

    @Test
    public void testParallelComplete() {
        Action action0 = new Complete();
        Action action1 = new Complete();

        assertFalse(action0.isComplete);
        assertFalse(action1.isComplete);

        sequentialScheduler.add(action0);
        sequentialScheduler.add(action1);

        sequentialScheduler.loop();

        assertTrue(action0.isComplete);
        assertFalse(action1.isComplete);

        sequentialScheduler.loop();

        assertTrue(action0.isComplete);
        assertTrue(action1.isComplete);

        assertTrue(sequentialScheduler.isDone());
    }
    */
}
