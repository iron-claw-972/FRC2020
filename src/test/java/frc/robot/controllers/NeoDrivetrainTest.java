package frc.robot.controllers;

import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

import com.revrobotics.CANSparkMax;

public class NeoDrivetrainTest
{
    @Test
    public void testTankDrive() {
        CANSparkMax lm1 = mock(CANSparkMax.class);
        CANSparkMax lm2 = mock(CANSparkMax.class);
        CANSparkMax rm1 = mock(CANSparkMax.class);
        CANSparkMax rm2 = mock(CANSparkMax.class);
        NeoDrivetrain neoDrivetrain = new NeoDrivetrain(lm1, lm2, rm1, rm2);
        
        ArgumentCaptor<Double> argCap = ArgumentCaptor.forClass(Double.class);

        verify(neoDrivetrain.leftMotor1, times(1)).set(argCap.capture());

        neoDrivetrain.tankDrive(1.0, 1.0);

        assertEquals(0.0, argCap.getValue(), 0.1);
    }
}