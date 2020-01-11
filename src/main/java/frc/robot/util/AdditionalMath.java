package frc.robot.util;

public class AdditionalMath
{
    public static double Clamp(double value, double min, double max)
    {
        if(value >= max) return max;
        if(value <= min) return min;
        return value;
    }

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