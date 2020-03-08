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
            new Trigger(this, Trigger.Type.AXIS, Context.flipInIntakeTriggerID, new FlipInIntake()),
            new Trigger(this, Trigger.Type.DPAD, Context.reverseNMFDirectionDpadID, new ReverseNMF()),
            new Trigger(this, Trigger.Type.BUTTON, Context.spinNMFToggleButtonID, new StopNMF()),
            new Trigger(this, Trigger.Type.DPAD, Context.reverseIntakeDirectionDpadID, new ReverseIntake()),
            new Trigger(this, Trigger.Type.DPAD, Context.nmfSlowSpinDpadID, new NMFSlowSpin()),
            new Trigger(this, Trigger.Type.AXIS, Context.shoot, new Shoot())
            
        });
    }

    public boolean getButtonPressed(int buttonID) {
        return joystick.getRawButtonPressed(buttonID);
    }

    public boolean getButtonReleased(int buttonID) {
        return joystick.getRawButtonReleased(buttonID);
    }

    public boolean getAxisPressed(int axisID) {
        return joystick.getRawAxis(axisID) > 0.5;
    }

    public boolean getAxisReleased(int axisID) {
        return joystick.getRawAxis(axisID) <= 0.5;
    }

    public boolean getDpadPressed(int dpadID) {
        int dpadValue = 0;
        switch (joystick.getPOV()){
            case 0:
                dpadValue=1;
                break;
            case 90:
                dpadValue=2;
                break;
            case 180:
                dpadValue=3;
                break;
            case 270:
                dpadValue=4;
                break;

        }
        return dpadID == dpadValue;
    }

    public boolean getDpadReleased(int dpadID) {
         return joystick.getPOV()==-1;
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
