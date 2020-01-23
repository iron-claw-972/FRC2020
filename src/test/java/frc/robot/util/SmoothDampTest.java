package frc.robot.util;

import static org.junit.Assert.*;
import org.junit.*;
// import static org.mockito.Mockito.*;
// import edu.wpi.first.wpilibj.*;
// import com.revrobotics.CANSparkMax;

public class SmoothDampTest {

    @Test
    public void smoothDampStepsize1() { 
        // Arrange
        // CANSparkMax motor = mock(CANSparkMax.class);
        // Servo servo = mock(Servo.class);

        // Act
        SmoothDamp smoothDamp = new SmoothDamp(1);

        // Assert
        assertEquals(1, smoothDamp.stepSize, 0.1);
    }

    @Test
    public void smoothDampStepsize10() {
        SmoothDamp smoothDamp = new SmoothDamp(10);
        
        assertEquals(10, smoothDamp.stepSize, 0.1);
    }
}