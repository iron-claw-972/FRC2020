package frc.robot.controllers;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.util.*;
import frc.robot.actions.*;

/**
 * This class is wrapper class to allow the abstraction of buttons and joysticks (removes the need to specify ports every time you get a button)
 */
public class OperatorJoystick implements CompetitionJoystick {
    public Joystick joystick;
    private long inUseStartTime = 0;
    public ArrayList<Trigger> triggers = new ArrayList<Trigger>();

    public OperatorJoystick() {
        joystick = new Joystick(Context.operatorJoystickID);

        addTriggers(new Trigger[]{
            new Trigger(this, Trigger.Type.BUTTON, Context.flipOutIntakeButtonID, new FlipOutIntake()),
            new Trigger(this, Trigger.Type.BUTTON, Context.flipInIntakeButtonID, new FlipInIntake()),
            new Trigger(this, Trigger.Type.BUTTON, Context.reverseNMFDirectionButtonID, new ReverseNMF()),
            new Trigger(this, Trigger.Type.AXIS, Context.spinNMFToggleTriggerID, new StopNMF()),
            new Trigger(this, Trigger.Type.AXIS, Context.spinIntakeTriggerID, new SpinIntake()),
            new Trigger(this, Trigger.Type.BUTTON, Context.reverseIntakeDirectionButtonID, new ReverseIntake()),
            
        });
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
