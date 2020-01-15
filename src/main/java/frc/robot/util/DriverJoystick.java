package frc.robot.util;

import edu.wpi.first.wpilibj.Joystick;

/**
 * This class is wrapper class to allow the abstraction of buttons and joysticks (removes the need to specify ports every time you get a button)
 */
public class DriverJoystick {
    private Joystick joystick;

    public DriverJoystick() {
        joystick = new Joystick(Context.joystickID);
    }

    public double getThrottle() {
        return joystick.getRawAxis(Context.throttleAxisID);
    }

    public double getYaw() {
        return joystick.getRawAxis(Context.yawAxisID);
    }

    public double getRawAxis(int axis)
    {
        return joystick.getRawAxis(axis);
    }

    public double getAxisDeadBandManaged(int axis)
    {
        double aValue = joystick.getRawAxis(axis);

        if(AdditionalMath.isInRange(aValue, -Context.joystickMaxDeadband, Context.joystickMaxDeadband, true)) {
            return 0.0;
        }
        
        return aValue;
    }

    public boolean getRawButton(int ID)
    {
        return joystick.getRawButton(ID);
    }

    public boolean getRawButtonPressed(int ID)
    {
        return joystick.getRawButtonPressed(ID);
    }

    public Joystick getJoystick()
    {
        return joystick;
    }
}
