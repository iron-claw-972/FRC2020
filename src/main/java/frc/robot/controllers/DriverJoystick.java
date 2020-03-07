package frc.robot.controllers;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.actions.*;
import frc.robot.util.*;

/**
 * This class is wrapper class to allow the abstraction of buttons and joysticks (removes the need to specify ports every time you get a button)
 */
public class DriverJoystick implements CompetitionJoystick {
    public Joystick joystick;
    private long inUseStartTime = 0;
    public ArrayList<Trigger> triggers = new ArrayList<Trigger>();

    public DriverJoystick() {
        joystick = new Joystick(Context.driverJoystickID);

        addTriggers(new Trigger[]{
            new Trigger(this, Trigger.Type.BUTTON, Context.toggleTrack, new VisionAlign()),
            new Trigger(this, Trigger.Type.BUTTON, Context.shiftGearsButtonID, new ShiftGears()),
            new Trigger(this, Trigger.Type.BUTTON, Context.climbButtonUp, new Climb(Climb.ClimbActions.UP)),
            new Trigger(this, Trigger.Type.BUTTON, Context.climbButtonDown, new Climb(Climb.ClimbActions.DOWN)),
            new Trigger(this, Trigger.Type.BUTTON, Context.winchID, new Winch(Winch.winchState.COIL))
        });
    }

    public double getThrottle() {
        return getAxisDeadBandManaged(Context.throttleAxisID);
    }

    public double getYaw() {
        return getAxisDeadBandManaged(Context.yawAxisID);
    }

    public boolean getButtonPressed(int buttonID) {
        return joystick.getRawButtonPressed(buttonID);
    }

    public boolean getAxisPressed(int axisID) {
        return joystick.getRawAxis(axisID) > 0.5;
    }

    public boolean isInUse() {
        return inUseStartTime + Context.inUseLengthMillis > System.currentTimeMillis();
    }

    public double getAxisDeadBandManaged(int axis) {
        double axisValue = joystick.getRawAxis(axis);

        if(AdditionalMath.isInRange(axisValue, -Context.joystickMaxDeadband, Context.joystickMaxDeadband, true)) {
            return 0.0;
        }

        inUseStartTime = System.currentTimeMillis();
        
        return axisValue;
    }

    public void loop() {
        for (Trigger trigger : triggers) {
            trigger.loop();
        }
    }

    public void addTrigger(Trigger triggerToAdd) {
        triggers.add(triggerToAdd);
    }

    public void addTriggers(Trigger[] triggersToAdd) {
        for (Trigger trigger : triggersToAdd) {
            triggers.add(trigger);
        }
    }

    public boolean getClimbU() {
        return joystick.getRawButton(Context.climbButtonUp);
    }

    public boolean getClimbD() {
        return joystick.getRawButton(Context.climbButtonDown);
    }

    /**
     * For some inexplicable reason the drive code completely breaks when you input negative zero so use this to invert the values instead
     */
    public static double invertValue(double joyValue) {
        joyValue = -joyValue;

        if (joyValue == -0.0) {
            joyValue = 0.0;
        }

        return joyValue;
    }
}
