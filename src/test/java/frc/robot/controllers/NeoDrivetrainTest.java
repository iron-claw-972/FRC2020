package frc.robot.controllers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import org.junit.*;

import com.revrobotics.CANSparkMax;

public class NeoDrivetrainTest
{
    public double lm1Power = 0;
    public double lm2Power = 0;
    public double rm1Power = 0;
    public double rm2Power = 0;

    public CANSparkMax lm1 = mock(CANSparkMax.class);
    public CANSparkMax lm2 = mock(CANSparkMax.class);
    public CANSparkMax rm1 = mock(CANSparkMax.class);
    public CANSparkMax rm2 = mock(CANSparkMax.class);
    public NeoDrivetrain neoDrivetrain = new NeoDrivetrain(lm1, lm2, rm1, rm2);

    @Before
    public void setup() {
        doAnswer(invocation -> {
            Double power = invocation.getArgument(0, Double.class);
            lm1Power = power.doubleValue();
            return null;
        }).when(lm1).set(any(Double.class));

        doAnswer(invocation -> {
            Double power = invocation.getArgument(0, Double.class);
            lm2Power = power.doubleValue();
            return null;
        }).when(lm2).set(any(Double.class));

        doAnswer(invocation -> {
            Double power = invocation.getArgument(0, Double.class);
            rm1Power = power.doubleValue();
            return null;
        }).when(rm1).set(any(Double.class));

        doAnswer(invocation -> {
            Double power = invocation.getArgument(0, Double.class);
            rm2Power = power.doubleValue();
            return null;
        }).when(rm2).set(any(Double.class));
    }

    @Test
    public void testTankDrive0() {
        neoDrivetrain.tankDrive(1.0, 1.0);

        assertEquals(1.0, lm1Power, 0.1);
        assertEquals(1.0, lm2Power, 0.1);
        assertEquals(1.0, rm1Power, 0.1);
        assertEquals(1.0, rm2Power, 0.1);
    }

    @Test
    public void testTankDrive1() {
        neoDrivetrain.tankDrive(1.0, 2.0);

        assertEquals(1.0, lm1Power, 0.1);
        assertEquals(1.0, lm2Power, 0.1);
        assertEquals(2.0, rm1Power, 0.1);
        assertEquals(2.0, rm2Power, 0.1);
    }

    @Test
    public void testTankDrive2() {
        neoDrivetrain.tankDrive(3.0, 100.0);

        assertEquals(3.0, lm1Power, 0.1);
        assertEquals(3.0, lm2Power, 0.1);
        assertEquals(100.0, rm1Power, 0.1);
        assertEquals(100.0, rm2Power, 0.1);
    }
}
