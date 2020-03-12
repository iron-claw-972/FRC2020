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

    public static boolean isInRange(double value, double min, double max, boolean inclusive)
    {
        if(inclusive)
        {
            if(value <= max && value >= min) return true;
        }
        else
        {
            if(value < max && value > min) return true;
        }
        return false;
    }

    public static double OvercomeFriction(double value, double decouple)
    {
        if(value>0) return value+decouple;
        if(value<0) return value-decouple;
        return value;
    }

    public static final double mPerFt = 0.3048;

    public static double ftToM(double feet, double inches) {
        return (feet + inches/12) * mPerFt;
    }

    public static double ftToM(double feet) {
        return ftToM(feet, 0);
    }

    public static Pose2d relativePose(Pose2d targetPose, )
}
