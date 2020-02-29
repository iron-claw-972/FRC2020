package frc.robot.controllers.drive;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import org.junit.*;

import com.revrobotics.CANSparkMax;

public class NeoDrivetrainTest {
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
    }

    @Test
    public void testGetDist() {
        neoDrivetrain.tankDrive(1.0, 1.0);

        assertEquals(1.0, lm1Power, 0.0);
        assertEquals(1.0, lm2Power, 0.0);
        assertEquals(1.0, rm1Power, 0.0);
        assertEquals(1.0, rm2Power, 0.0);
    }

}
