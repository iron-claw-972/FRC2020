package frc.robot.util;

import static org.junit.Assert.*;
import org.junit.*;

public class JRADTest
{
    @Test
    public void updateTest100()
    {
        JRAD jrad = new JRAD(1, 100, 1);
        double output = jrad.update(1, 0, 1);
        assertEquals(100, output, 1.1);
    }

    @Test
    public void updateTest50()
    {
        JRAD jrad = new JRAD(1, 50, 1);
        double output = jrad.update(1, 0, 1);
        assertEquals(50, output, 1.1);
    }
}