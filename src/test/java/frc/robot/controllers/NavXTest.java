package frc.robot.controllers;

import static org.junit.Assert.*;
import org.junit.*;
import static org.mockito.Mockito.*;
import com.kauailabs.navx.frc.AHRS;

public class NavXTest {

    @Test
    // Tests getting the rawHeading of a mocked AHRS
    public void testRawHeading() {
        // Makes a mock AHRS, it acts in almost every way like a real AHRS
        AHRS mockAhrs = mock(AHRS.class);

        // Declares and initializes NavX
        NavX navX = new NavX(mockAhrs);
        
        // When the AHRS.getAngle() method is called, it will return 5.0
        when(mockAhrs.getAngle()).thenReturn(5.0);

        navX.ahrs = mockAhrs;

        // Assert
        assertEquals(5.0, navX.getRawHeading(), 0.1);
    }

    @Test
    // Tests getting the rawHeading of a mocked AHRS
    public void testLimitedHeading() {
        // Makes a mock AHRS, it acts in almost every way like a real AHRS
        AHRS mockAhrs = mock(AHRS.class);

        // Declares and initializes NavX
        NavX navX = new NavX(mockAhrs);
        
        // When the getPressure() is called, it will return the float 5.0f
        // On the second call, it will return the float 6.5f
        when(mockAhrs.getAngle()).thenReturn(258.0);

        navX.ahrs = mockAhrs;

        // Assert
        assertEquals(-102.0, navX.getConstrainedHeading(), 0.1);
    }

    @Test
    // Tests getting the rawHeading of a mocked AHRS
    public void testBarometer() {
        // Makes a mock AHRS, it acts in almost every way like a real AHRS
        AHRS mockAhrs = mock(AHRS.class);

        // Declares and initializes NavX
        NavX navX = new NavX(mockAhrs);
        
        // When the getPressure() is called, it will return the float 5.0f
        // On the second call, it will return the float 6.5f
        when(mockAhrs.getBarometricPressure()).thenReturn(3.0f);

        navX.ahrs = mockAhrs;

        // Assert
        assertEquals(3.0f, navX.getBarometricPressure(), 0.1f);
    }
}
