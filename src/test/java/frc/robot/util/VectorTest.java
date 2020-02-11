package frc.robot.util;

import static org.junit.Assert.*;
import org.junit.*;

public class VectorTest
{
    @Test
    public void testConstructorZero() {
        Vector a = new Vector();

        assertEquals(0.0, a.x, 0.1);
        assertEquals(0.0, a.y, 0.1);
        assertEquals(0.0, a.z, 0.1);
    }

    @Test
    public void testConstructorTwo() {
        Vector a = new Vector(2,4);

        assertEquals(2.0, a.x, 0.1);
        assertEquals(4.0, a.y, 0.1);
        assertEquals(0.0, a.z, 0.1);
    }

    @Test
    public void testConstructorThree() {
        Vector a = new Vector(2,4,6);

        assertEquals(2.0, a.x, 0.1);
        assertEquals(4.0, a.y, 0.1);
        assertEquals(6.0, a.z, 0.1);
    }

    @Test
    public void testVectorAdd() {
        Vector a = new Vector(2,4,6);
        Vector b = new Vector(-7, 3, 9);

        a.add(b);

        assertEquals(-5.0, a.x, 0.1);
        assertEquals(7.0, a.y, 0.1);
        assertEquals(15.0, a.z, 0.1);
    }

    @Test
    public void testVectorSubtract() {
        Vector a = new Vector(2,4,6);
        Vector b = new Vector(-7, 3, 9);

        a.subtract(b);

        assertEquals(9.0, a.x, 0.1);
        assertEquals(1.0, a.y, 0.1);
        assertEquals(-3.0, a.z, 0.1);
    }

    @Test
    public void testAddVectors() {
        Vector a = new Vector(2,4,6);
        Vector b = new Vector(-7, 3, 9);

        Vector c = Vector.addVectors(a, b);

        assertEquals(-5.0, c.x, 0.1);
        assertEquals(7.0, c.y, 0.1);
        assertEquals(15.0, c.z, 0.1);
    }

    @Test
    public void testSubstractVectors() {
        Vector a = new Vector(2,4,6);
        Vector b = new Vector(-7, 3, 9);

        Vector c = Vector.subtractVectors(a, b);

        assertEquals(9.0, c.x, 0.1);
        assertEquals(1.0, c.y, 0.1);
        assertEquals(-3.0, c.z, 0.1);
    }
}