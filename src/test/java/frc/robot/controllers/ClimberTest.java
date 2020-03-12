
package frc.robot.controllers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;
import org.junit.*;

import frc.robot.util.Context;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;

public class ClimberTest
{
    // Declaration of variables used to store inputs to motors
    public double telescopePower = 0;
    public double coil1Power = 0;
    public double coil2Power = 0;

    // Creation of mock sparks and putting them into NeoDrivetrain
    public CANSparkMax telescope = mock(CANSparkMax.class);
    public CANSparkMax telescopeMotor = mock(CANSparkMax.class);
    public CANEncoder encoder = mock(CANEncoder.class);
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
            coil1Power = power.doubleValue();
            return null;
        }).when(coil1).set(any(Double.class));

        doAnswer(invocation -> {
            Double power = invocation.getArgument(0, Double.class);
            coil2Power = power.doubleValue();
            return null;
        }).when(coil2).set(any(Double.class));
    }

    // Test that ensures that coil method increases coil motor power to 0.5
    @Test
    public void coil1Test() {
        climb.coil(Context.coilSpeed);
        double finalValue = coil1Power;
        assertEquals(finalValue, 0.5, 1);
    }

    @Test
    public void coil2Test() {
        climb.coil(Context.coilSpeed);
        double finalValue = coil2Power;
        assertEquals(finalValue, 0.5, 1);
    }

    @Test
    public void telescopeDownTest() {
        when(telescopeMotor.getEncoder()).thenReturn(encoder);
        when(encoder.getPosition()).thenReturn(20.0);
        climb.pastTime = System.currentTimeMillis()+10000;
        climb.bottomEncoderHeight = 5;
        //throws nullpointer, please investigate
        climb.down();
        //present = -15 pfactor = .0001 integral = -150000 ifactor = 0 deriv = -15/10000 dfactor = 0.0003
        //present * pFactor + integral * iFactor + deriv * dFactor
        //-0.00150045
        assertEquals(climb.pidVal, -0.00150045, 0.001);
    }
    
    @Test
    public void telescopeUpBaseTest() {
        climb.telescopeMove(climb.getPolyMotorPower(0));
        assertEquals(telescopePower, .58, .05);
    }

    @Test
    public void telescopeUpToFastTest() {
        //395 is max lift step before speed returned by getPolyMototPower will be set to a max of 1
        climb.telescopeMove(climb.getPolyMotorPower(395));
        assertEquals(telescopePower, 1, .05);
    }

    @Test
    public void telescopeUpTopTest() {
        climb.currentPosition = climb.topEncoderHeight + 1;
        climb.telescopeMove(climb.getPolyMotorPower(50));
        assertEquals(telescopePower, 0, .05);
    }
}
