# FRC2020

This is the 2020 repository of the FRC team 972 Ironclaw.

The project board for this repository is located [here](https://github.com/iron-claw-972/FRC2020/projects/1). Feel free to assign yourself to tasks and to complete them.

# Code Structure

Overall the code structure is very basic and may be in some need of revising. Under ~\src\main\java\frc\robot there are two directories, described below. In addion there are two files, robot.java and main.java, which control robot functions at the highest level.

## Controllers

Pretty much everything that controls a specific action that the robot can take, this includes the conventional subsystems.

## Util

All of the general purpose classes that are used throughout the robot code.

## Online library import links

Do `ctrl-shift-p` to open the quick actions bar, then select manage vendor libraries. From there select `install new library (online)` and paste the urls below before pressing `ok`.

### Spark MAX:

http://www.revrobotics.com/content/sw/max/sdk/REVRobotics.json

### NavX

https://www.kauailabs.com/dist/frc/2020/navx_frc.json

### Talon SRX

http://devsite.ctr-electronics.com/maven/release/com/ctre/phoenix/Phoenix-latest.json

### Google Java Style Guide
https://google.github.io/styleguide/javaguide.html

# Writing Tests

Unit tests are simple to write and provide many benefits. The steps for making tests are detailed below:

1. Navigate to [FRC2020/src/test/java/frc/robot/](https://github.com/iron-claw-972/FRC2020/tree/master/src/test/java/frc/robot)

2. Add a file in the same respective folder as the main class is in main. For example, if one wanted to write a test for the Intake.java class located in ~/main/java/frc/robot/controllers, the test should be written in a file named IntakeTest.java located in ~/test/java/frc/robot/controllers.

3. Add the following code to the test file:

``` java
package frc.robot.util;

import static org.junit.Assert.*;
import org.junit.*;

// Replace YourTest with the name of the test file
public class YourTest {
  @Test
  // The function should have a descriptive name for what it is testing.
  public void exampleTestFunction() { 
    // Add test code here

    // Assert equals compares two values to determine if they are equal, if they are the test is PASSED
    assertEquals(expected, actual, delta);
  }
}
```

4. Test your test to determine if it does what it should

### Simple Example Test

This test is designed to test the Calculator.java class, which has the methods add(), multuply() and divide().

``` java
package frc.robot.util;

import static org.junit.Assert.*;
import org.junit.*;

public class CalculatorTest {
  public Calculator calculator = new Calculator();
  
  @Test
  // This function tests if the addition feature of Calculator works
  public void testAdd() { 
    int actualSum = calculator.add(10, 5);
    
    assertEquals(10 + 5, actualSum, 1);
  }

  @Test
  // This function tests if the multiplication feature of Calculator works
  public void testMultiply() { 
    int actualProduct = calculator.multiply(3, 7);
    
    assertEquals(3 * 7, actualProduct, 1);
  }

  @Test
  // This function makes sure that 4 / 2 does not equal 73
  public void testDivide() { 
    int actualProduct = calculator.divide(4, 2);
    
    assertNotEquals(73, actualQuotient);
  }
}
```

### Advanced Example Test

This test is designed to test the NavX.java class. The test is located [here](https://github.com/iron-claw-972/FRC2020/blob/master/src/test/java/frc/robot/controllers/NavXTest.java).

``` java
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
```

### CANSparkMax Example

The example below demonstrates how to read the value used as the input to the spark's .set method. This style of test can be applied to many different classes that use motors.

```java
package frc.robot.controllers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import org.junit.*;
import com.revrobotics.CANSparkMax;

public class NeoDrivetrainTest
{
    // Declaration of variables used to store inputs to motors
    public double lm1Power = 0;
    public double lm2Power = 0;
    public double rm1Power = 0;
    public double rm2Power = 0;

    // Creation of mock sparks and putting them into NeoDrivetrain
    public CANSparkMax lm1 = mock(CANSparkMax.class);
    public CANSparkMax lm2 = mock(CANSparkMax.class);
    public CANSparkMax rm1 = mock(CANSparkMax.class);
    public CANSparkMax rm2 = mock(CANSparkMax.class);
    public NeoDrivetrain neoDrivetrain = new NeoDrivetrain(lm1, lm2, rm1, rm2);

    // @Before allows for the setup() method to be called before any other methods
    @Before
    public void setup() {
        // Basically replaces the .set() method for mock Spark with 
        // method that puts input into variable for testing
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

    // Simple test that calls method that calls in turn the .set function of motors
    @Test
    public void testTankDrive0() {
        neoDrivetrain.tankDrive(1.0, 1.0);

        assertEquals(1.0, lm1Power, 0.1);
        assertEquals(1.0, lm2Power, 0.1);
        assertEquals(1.0, rm1Power, 0.1);
        assertEquals(1.0, rm2Power, 0.1);
    }
}
```