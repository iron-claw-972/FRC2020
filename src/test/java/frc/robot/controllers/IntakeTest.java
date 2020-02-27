package frc.robot.controllers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import org.junit.*;
import com.revrobotics.CANSparkMax;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.*;
import edu.wpi.first.wpilibj.*;

public class IntakeTest
{
    // Declaration of variables used to store inputs to motors
    public double intakeTalonPower = 0;
    public double flipSolenoidPower = 0;

    // Creation of mock sparks and putting them into NeoDrivetrain
    public DoubleSolenoid flipSolenoid = mock(DoubleSolenoid.class);
    public TalonSRX intakeTalon = mock(TalonSRX.class);
    public Intake intake = new Intake(intakeTalon, flipSolenoid);

    // @Before allows for the setup() method to be called before any other methods
    @Before
    public void setup() {
        // Basically replaces the .set() method for mock Spark with 
        // method that puts input into variable for testing
        doAnswer(invocation -> {
            Double power = invocation.getArgument(1, Double.class);
            intakeTalonPower = power.doubleValue();
            return null;
        }).when(intakeTalon).set(eq(ControlMode.PercentOutput), any(Double.class));

        doAnswer(invocation -> {
            Double power = invocation.getArgument(0, Double.class);
            flipSolenoidPower = power.doubleValue();
            return null;
        }).when(flipSolenoid).set(any());
    }

    // Test that ensures that coil method increases coil motor power to 0.5
    @Test
    public void flipOutTest() {
        
    }

    // Test that ensures that coil method increases coil motor power to 0.5
    @Test
    public void flipInTest() {
        
    }

    // Test that ensures that coil method increases coil motor power to 0.5
    @Test
    public void beginIntakingTest() {
        
    }

    // Test that ensures that coil method increases coil motor power to 0.5
    @Test
    public void stopIntakingTest() {
        
    }
}
