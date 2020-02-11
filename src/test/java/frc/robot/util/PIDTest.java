package frc.robot.util;

import static org.junit.Assert.*;
import org.junit.*;

public class PIDTest
{
    @Test
    public void updateTest()
    {
        PID testPID = new PID(1.0, 0, 0);
        
        double output = 0;

        for(int i = 0; i<10; i++)
        {
            output += 0.5 * testPID.update(10, output, 5);
        }

        assertEquals(10, output, 1.0);
    }

    @Test
    public void integralTest()
    {
        PID testPID = new PID(1.0, 0.00001, 0);
        
        double output = 0;

        for(int i = 0; i<10; i++)
        {
            output += 0.5 * testPID.update(100, output, 5);
        }

        assertNotEquals(0.0, testPID.integral, 0.001);
        
        assertEquals(100, output, 1.0);
    }

}