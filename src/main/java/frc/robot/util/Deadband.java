package frc.robot.util;

public class Deadband {
    public static double handle (double input, double deadband)
    {
        if (Math.abs(input) < Math.abs(deadband))
        {
            return 0.0;
        }

        return input;
    }

    public static long handle (long input, long deadband)
    {
        if (Math.abs(input) < Math.abs(deadband))
        {
            return 0;
        }

        return input;
    }
}