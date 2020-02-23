package frc.robot.controllers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.*;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.actions.Action;
import frc.robot.execution.ParallelScheduler;
import frc.robot.util.Context;
import frc.robot.util.Trigger;

public class DriverJoystickTest {
    public DriverJoystick driverJoystick;

    public class Complete extends Action {
        public void loop() {
            markComplete();
        }
    }

    public class Complete0 extends Action {
        public void loop() {
            markComplete();
        }
    }

    public Action passedAction;

    public Trigger trigger0;
    public Trigger trigger1;

    @Before
    public void setup() {
        Context.robotController = mock(RobotController.class);

        driverJoystick = new DriverJoystick();
        driverJoystick.joystick = mock(Joystick.class);

        Context.robotController.driverJoystick = driverJoystick;

        when(driverJoystick.joystick.getRawButtonPressed(1)).thenReturn(false);
        when(driverJoystick.joystick.getRawButtonPressed(2)).thenReturn(true);
        when(driverJoystick.joystick.getRawAxis(1)).thenReturn(0.001);
        when(driverJoystick.joystick.getRawAxis(2)).thenReturn(-0.5);

        trigger0 = new Trigger(1, new Complete());
        trigger1 = new Trigger(2, new Complete0());

        Context.robotController.parallelScheduler = mock(ParallelScheduler.class);

        doAnswer(invocation -> {
            passedAction = invocation.getArgument(0, Action.class);
            return null;
        }).when(Context.robotController.parallelScheduler).add(any(Action.class));
    }

    @Test
    public void testReadButtonPressed() {
        assertEquals(false, driverJoystick.getButtonPressed(1));
        assertEquals(true, driverJoystick.getButtonPressed(2));
    }

    @Test
    public void testReadJoystick() {
        assertEquals(0, driverJoystick.getAxisDeadBandManaged(1), 0.000001);
        assertEquals(-0.5, driverJoystick.getAxisDeadBandManaged(2), 0.000001);
    }

    @Test
    public void testAddTrigger() {
        driverJoystick.addTrigger(trigger0);

        assertEquals(trigger0, driverJoystick.triggers.get(0));

        driverJoystick.triggers.clear();
    }

    @Test
    public void testAddTriggers() {
        driverJoystick.addTriggers( new Trigger[] {
            trigger0,
            trigger1
        } );

        assertEquals(trigger0, driverJoystick.triggers.get(0));
        assertEquals(trigger1, driverJoystick.triggers.get(1));

        driverJoystick.triggers.clear();
    }

    @Test
    public void testLoop() {
        driverJoystick.addTriggers( new Trigger[] {
            trigger0,
            trigger1
        } );

        assertEquals(trigger0, driverJoystick.triggers.get(0));
        assertEquals(trigger1, driverJoystick.triggers.get(1));

        driverJoystick.loop();

        assertEquals(trigger1.action.getClass(), passedAction.getClass());

        driverJoystick.triggers.clear();
        passedAction = null;
    }
}