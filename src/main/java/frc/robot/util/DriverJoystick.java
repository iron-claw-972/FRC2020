package frc.robot.util;

import edu.wpi.first.wpilibj.Joystick;

/**
 * This class is wrapper class to allow the abstraction of buttons and joysticks (removes the need to specify ports every time you get a button)
 */
public class DriverJoystick {
    private Joystick driverJoy = new Joystick(Context.joystickID);

    public double getThrottle() {
        return driverJoy.getRawAxis(Context.throttleAxisID);
    }

    public double getYaw() {
        return driverJoy.getRawAxis(Context.yawAxisID);
    }

}