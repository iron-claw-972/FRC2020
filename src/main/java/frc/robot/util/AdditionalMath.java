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

    public static double ShooterSpeed(double targetDistanceMeters, double shooterAngleDegrees, double shooterHeightMeters, double targetHeightMeters) {
        double shooterAngleRadians = shooterAngleDegrees * Math.PI/180;
        return Math.sqrt((9.81*Math.pow(targetDistanceMeters, 2))
        /((2*Math.pow(Math.cos(shooterAngleRadians), 2))*(targetHeightMeters - shooterHeightMeters - targetDistanceMeters*Math.tan(shooterAngleRadians))));

        /*  __________________________
           /   (gd^2)/(2cos(a)^2)
          /    -----------------
        \/    (h_2 - h_1 - dtan(a))
        */
    }

}
