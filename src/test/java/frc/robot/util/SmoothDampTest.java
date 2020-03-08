package frc.robot.util;

import static org.junit.Assert.*;
import org.junit.*;

public class SmoothDampTest {

    @Test
    public void smoothDampStepsize1() {
        SmoothDamp smoothDamp = new SmoothDamp(1);

        assertEquals(1, smoothDamp.stepSize, 0.1);
    }

    @Test
    public void smoothDampStepsize10() {
        SmoothDamp smoothDamp = new SmoothDamp(10);
        
        assertEquals(10, smoothDamp.stepSize, 0.1);
    }
}