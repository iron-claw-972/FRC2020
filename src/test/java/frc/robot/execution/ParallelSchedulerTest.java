package frc.robot.execution;

import static org.junit.Assert.*;

import org.junit.*;

import frc.robot.actions.Action;

public class ParallelSchedulerTest
{
    public ParallelScheduler parallelScheduler;

    public class Complete extends Action{
        public void loop()
        {
            markComplete();
        }
    }

    @Before
    public void setup() {
        parallelScheduler = new ParallelScheduler();
    }

    @Test
    public void testComplete() {
        Action action0 = new Complete();

        assertFalse(action0.isComplete);

        parallelScheduler.add(action0);

        parallelScheduler.loop();

        assertTrue(action0.isComplete);

        assertTrue(parallelScheduler.isDone());
    }

    @Test
    public void testParallelComplete() {
        Action action0 = new Complete();
        Action action1 = new Complete();

        assertFalse(action0.isComplete);
        assertFalse(action1.isComplete);

        parallelScheduler.add(action0);
        parallelScheduler.add(action1);

        parallelScheduler.loop();

        assertTrue(action0.isComplete);
        assertTrue(action1.isComplete);

        assertTrue(parallelScheduler.isDone());
    }
}
