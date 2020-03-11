package frc.robot.controllers.drive;

import static org.junit.Assert.*;

import org.junit.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;

import frc.robot.util.PIDF;
import frc.robot.util.Context;

public class DrivetrainTest {

    private class ConcreteDrivetrain extends Drivetrain {
        public double leftSignal = 0;
        public double rightSignal = 0;

        public ConcreteDrivetrain(PIDF leftDrivePIDF_, PIDF rightDrivePIDF_) {
            super(leftDrivePIDF_, rightDrivePIDF_);
        }

        @Override
        public void tankDrive(double leftPower, double rightPower) {
            this.leftSignal = leftPower;
            this.rightSignal = rightPower;
        }

        @Override
        public void setCurrentLimiting(double amps, double activationAmps, boolean enableCurrentLimiting) {
        }

        @Override
        protected double getLeftTicks() {
            return 0;
        }

        @Override
        protected double getRightTicks() {
            return 0;
        }

        @Override
        public double getLeftDist() {
            return 0;
        }

        @Override
        public double getRightDist() {
            return 0;
        }

    }

    private PIDF leftPIDF = mock(PIDF.class);
    private PIDF rightPIDF = mock(PIDF.class);

    private ConcreteDrivetrain drivetrain = new ConcreteDrivetrain(leftPIDF, rightPIDF);

    @Before
    public void setup() {
        doAnswer(invocation -> {
            Double leftPower = invocation.getArgument(0, Double.class);
            return leftPower.doubleValue();
        }).when(leftPIDF).update(any(Double.class), any(Double.class), any(Double.class));

        doAnswer(invocation -> {
            Double rightPower = invocation.getArgument(0, Double.class);
            return rightPower.doubleValue();
        }).when(rightPIDF).update(any(Double.class), any(Double.class), any(Double.class));
    }
    /*
    @Test
    public void testArcadeDrive() {
        drivetrain.arcadeDrive(0, 0);
        assertEquals(0.0, drivetrain.leftSignal, 0.0);
        assertEquals(0.0, drivetrain.rightSignal, 0.0);

        drivetrain.arcadeDrive(-0, -0);
        assertEquals(0.0, drivetrain.leftSignal, 0.0);
        assertEquals(0.0, drivetrain.rightSignal, 0.0);

        drivetrain.arcadeDrive(1, 0);
        assertEquals(Context.maxDrivingSpeed, drivetrain.leftSignal, 0.0);
        assertEquals(Context.maxDrivingSpeed, drivetrain.rightSignal, 0.0);

        drivetrain.arcadeDrive(-10000, 0);
        assertEquals(-Context.maxDrivingSpeed, drivetrain.leftSignal, 0.0);
        assertEquals(-Context.maxDrivingSpeed, drivetrain.rightSignal, 0.0);

        drivetrain.arcadeDrive(0, 1);
        assertEquals(Context.maxDrivingSpeed, drivetrain.leftSignal, 0.0);
        assertEquals(-Context.maxDrivingSpeed, drivetrain.rightSignal, 0.0);

        drivetrain.arcadeDrive(-0, -1);
        assertEquals(-Context.maxDrivingSpeed, drivetrain.leftSignal, 0.0);
        assertEquals(Context.maxDrivingSpeed, drivetrain.rightSignal, 0.0);
    }
    
    @Test
    public void testCurvatureDrive() {
        drivetrain.curvatureDrive(0, 0, false);
        assertEquals(0, drivetrain.leftSignal, 0.0);
        assertEquals(0, drivetrain.rightSignal, 0.0);
        drivetrain.resetQuickStopAccum();

        drivetrain.curvatureDrive(0, 1, true);
        assertEquals(Context.maxDrivingSpeed, drivetrain.leftSignal, 0.0);
        assertEquals(-Context.maxDrivingSpeed, drivetrain.rightSignal, 0.0);
        drivetrain.resetQuickStopAccum();

        drivetrain.curvatureDrive(-0, -0, false);
        assertEquals(0.0, drivetrain.leftSignal, 0.0);
        assertEquals(0.0, drivetrain.rightSignal, 0.0);
        drivetrain.resetQuickStopAccum();

        drivetrain.curvatureDrive(1, 0, true);
        assertEquals(Context.maxDrivingSpeed, drivetrain.leftSignal, 0.0);
        assertEquals(Context.maxDrivingSpeed, drivetrain.rightSignal, 0.0);
        drivetrain.resetQuickStopAccum();

        drivetrain.curvatureDrive(-10000, 0, true);
        assertEquals(-Context.maxDrivingSpeed, drivetrain.leftSignal, 0.0);
        assertEquals(-Context.maxDrivingSpeed, drivetrain.rightSignal, 0.0);
        drivetrain.resetQuickStopAccum();

        drivetrain.curvatureDrive(-0, -1, true);
        assertEquals(-Context.maxDrivingSpeed, drivetrain.leftSignal, 0.0);
        assertEquals(Context.maxDrivingSpeed, drivetrain.rightSignal, 0.0);
        drivetrain.resetQuickStopAccum();
    }
    */
}
