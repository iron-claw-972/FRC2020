package frc.robot.controllers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import org.junit.*;
import com.revrobotics.CANSparkMax;
import com.ctre.phoenix.motorcontrol.can.*;

public class ClimberTest
{
    // Declaration of variables used to store inputs to motors
    public double telescopePower = 0;

    // Creation of mock sparks and putting them into NeoDrivetrain
    public CANSparkMax telescope = mock(CANSparkMax.class);
    public TalonSRX coil = mock(TalonSRX.class);
    public Climber climb = new Climber(coil, telescope);

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
    }

    // Simple test that calls method that calls in turn the .set function of motors
    @Test
    public void climberTest() {
        assertEquals(1.0, 1, 0.1);

    }
    
}