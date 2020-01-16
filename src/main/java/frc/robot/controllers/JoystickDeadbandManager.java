package frc.robot.controllers;

import frc.robot.util.AdditionalMath;
import frc.robot.util.Context;

import edu.wpi.first.wpilibj.Joystick;

public class JoystickDeadbandManager
{
    Joystick joy;

    public JoystickDeadbandManager(Joystick j)
    {
        joy = j;
    }

    public JoystickDeadbandManager(int JoystickID)
    {
        joy = new Joystick(JoystickID);
    }

    public double getAxisDeadBandManaged(int axis)
    {
        double aValue = joy.getRawAxis(axis);
        if(AdditionalMath.isInRange(aValue, -Context.joystickMaxDeadband, Context.joystickMaxDeadband, true)) return 0.0;
        return aValue;
    }

    public double getRawAxis(int axis)
    {
        return joy.getRawAxis(axis);
    }

    public boolean getRawButton(int ID)
    {
        return joy.getRawButton(ID);
    }

    public boolean getRawButtonPressed(int ID)
    {
        return joy.getRawButtonPressed(ID);
    }

    public Joystick getJoystick()
    {
        return joy;
    }
}