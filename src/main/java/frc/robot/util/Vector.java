package frc.robot.util;

public class Vector // this class was made just for easy storage of two variables
{                   // poor java, doesn't even have structs
    public double X = 0.0;
    public double Y = 0.0; // hahahaha// bold of you to assume we will be off the ground at any time
    public double Z = 0.0;

    public Vector(double iX, double iY)
    {
        X = iX;
        Y = iY;
    }

    public Vector(double iX, double iY, double iZ)
    {
        X = iX;
        Y = iY;
        Z = iZ;
    }

    public void add(Vector b)
    {
        X += b.X;
        Y += b.Y;
        Z += b.Z;
    }

    public void subtract(Vector b)
    {
        X -= b.X;
        Y -= b.Y;
        Z -= b.Z;
    }

    public double dot(Vector b)
    {
        return X * b.X + Y * b.Y + Z * b.Z;
    }

    public double getLength()
    {
        return Math.sqrt(X*X+Y*Y+Z*Z);
    }

    public void normalize()
    {
        double l = getLength();
        X = X/l;
        Y = Y/l;
        Z = Z/l;
    }
}