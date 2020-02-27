package frc.robot.util;

import static org.junit.Assert.*;
import org.junit.*;

import frc.robot.controllers.*;

import static org.mockito.Mockito.*;

import frc.robot.actions.Action;
import frc.robot.controllers.DriverJoystick;
import frc.robot.execution.ParallelScheduler;

public class TriggerTest {
    public Action passedAction;

    public class Complete extends Action{
        public void loop()
        {
            markComplete();
        }
    }

    @Before
    public void setup() {
        Context.robotController = mock(RobotController.class);

        Context.robotController.driverJoystick = mock(DriverJoystick.class);

        when(Context.robotController.driverJoystick.getButtonPressed(1)).thenReturn(false);
        when(Context.robotController.driverJoystick.getButtonPressed(2)).thenReturn(true);

        Context.robotController.parallelScheduler = mock(ParallelScheduler.class);

        doAnswer(invocation -> {
            passedAction = invocation.getArgument(0, Action.class);
            return null;
        }).when(Context.robotController.parallelScheduler).add(any(Action.class));
    }

    @Test
    public void testNotPressed() {
        Complete completeAction = new Complete();
    
        Trigger trigger = new Trigger(1, completeAction);

        trigger.loop();
        
        assertEquals(null, passedAction);
        
        passedAction = null;
    }

    @Test
    public void testPassing() {
        Complete completeAction = new Complete();
    
        Trigger trigger = new Trigger(2, completeAction);

        trigger.loop();

        assertNotEquals(null, passedAction);

        passedAction = null;
    }

    @Test
    public void testCloning() {
        Complete completeAction = new Complete();
    
        Trigger trigger = new Trigger(2, completeAction);

        trigger.loop();

        passedAction.loop();

        assertNotEquals(trigger.action, passedAction);

        passedAction = null;
    }
}