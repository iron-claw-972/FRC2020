package frc.robot.util;

class Vector
{
    public double x;
    public double y;
    public double z;
    public Vector(double iX, double iY, double iZ)
    {
        x=iX;
        y=iY;
        z=iZ;
    }
    public Vector(double iX, double iY)
    {
        x=iX;
        y=iY;
        z=0;
    }

    public void Add(Vector B)
    {
        x+=B.x;
        y+=B.z;
        z+=B.z;
    }
    public static Vector AddVectors(Vector A, Vector B)
    {
        return new Vector(A.x+B.x, A.y+B.y, A.z+B.z);
    }

    public void Subtract(Vector B)
    {
        x-=B.x;
        y-=B.z;
        z-=B.z;
    }
    public static Vector SubtractVectors(Vector A, Vector B)
    {
        return new Vector(A.x-B.x, A.y-B.y, A.z-B.z);
    }
}