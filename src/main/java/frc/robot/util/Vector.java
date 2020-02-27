package frc.robot.util;

class Vector
{
    public double x = 0;
    public double y = 0;
    public double z = 0;

    
    public Vector() {}
    
    public Vector(double iX, double iY)
    {
        x=iX;
        y=iY;
    }

    public Vector(double iX, double iY, double iZ)
    {
        x=iX;
        y=iY;
        z=iZ;
    }

    public void add(Vector B)
    {
        x+=B.x;
        y+=B.y;
        z+=B.z;
    }

    public void subtract(Vector B)
    {
        x-=B.x;
        y-=B.y;
        z-=B.z;
    }

    public static Vector subtractVectors(Vector A, Vector B)
    {
        return new Vector(A.x-B.x, A.y-B.y, A.z-B.z);
    }

    public static Vector addVectors(Vector A, Vector B)
    {
        return new Vector(A.x+B.x, A.y+B.y, A.z+B.z);
    }
}