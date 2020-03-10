
package frc.robot.controllers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import org.junit.*;

import frc.robot.util.Context;

import com.revrobotics.CANSparkMax;

public class ClimberTest
{
    // Declaration of variables used to store inputs to motors
    public double telescopePower = 0;
    public double coilPower = 0;

    // Creation of mock sparks and putting them into NeoDrivetrain
    public CANSparkMax telescope = mock(CANSparkMax.class);
    public CANSparkMax coil1 = mock(CANSparkMax.class);
    public CANSparkMax coil2 = mock(CANSparkMax.class);
    public Climber climb = new Climber(coil1, coil2, telescope);

    // @Before allows for the setup() method to be called before any other methods
    @Before
    public void setup() {
        // Basically replaces the .set() method for mock Spark with 
        // method that puts input into variable for testing
        doAnswer(invocation -> {
            Double power = invocation.getArgument(0, Double.class);
            telescopePower = power.doubleValue();
            return null;
        }).when(telescope).set(any(Double.class));

        doAnswer(invocation -> {
            Double power = invocation.getArgument(0, Double.class);
            coilPower = power.doubleValue();
            return null;
        }).when(coil1).set(any(Double.class));
    }

    // Test that ensures that coil method increases coil motor power to 0.5
    @Test
    public void coilUpTest() {
        climb.coil(Context.coilSpeed);
        double finalValue = coilPower;
        assertEquals(finalValue, 0.5, 1);
    }
}
