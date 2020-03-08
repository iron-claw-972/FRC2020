package frc.robot.util;

public class Vector

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
        z=0;
    }

    public Vector(double iX, double iY, double iZ)
    {
        x=iX;
        y=iY;
        z=iZ;
    }

    public Vector add(Vector B)
    {
        return new Vector(x + B.x,y + B.y,z + B.z);
    }

    public Vector subtract(Vector B)
    {
        return new Vector(x - B.x,y - B.y,z - B.z);
    }

    public Vector multiply(double A)
    {
        return new Vector(x*A,y*A,z*A);
    }

    public double Length()
    {
        return Math.sqrt(x*x + y*y + z*z);
    }

    public Vector normalize()
    {
        double l = Length();
        return new Vector(x/l, y/l, z/l);
    }

    public Vector rotateXY(double Angle)
    {
        return new Vector(x*Math.cos(Angle) - y*Math.sin(Angle), x* Math.sin(Angle) + y * Math.cos(Angle), z); // basic rotation matrix
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

    public static Vector multiplyVector(Vector A, double B)
    {
        return new Vector(A.x*B, A.z*B, A.z*B);
    }
}