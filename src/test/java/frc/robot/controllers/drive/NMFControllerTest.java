package frc.robot.controllers.drive;

import com.revrobotics.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import org.junit.Assert.*;

import frc.robot.controllers.NMFController;
import frc.robot.util.AdditionalMath;
import frc.robot.util.PIDF;

public class NMFControllerTest {

    CANSparkMax NMFSpark = mock(CANSparkMax.class);
    CANEncoder NMFEncoder = mock(CANEncoder.class);
    CANSparkMax omniSpark = mock(CANSparkMax.class);
    CANEncoder omniEncoder = mock(CANEncoder.class);

    public NMFController NMFController;

    public double leftSpeed;
    public double rightSpeed;
    public final double NMFCurrentToSpeedRatio = 8;
    public final double NMFMaxAccel = 2;
    public final double omniCurrentToSpeedRatio = 8;
    public final double omniMaxAccel = 2;

    public PIDF leftTestPIDF;
    public PIDF rightTestPIDF;

    public final double totalTime = 100;
    public final double deltaTime = 0.02;

    public void setup() {
        leftTestPIDF = NeoDrivetrain.leftDrivePIDF;
        rightTestPIDF = NeoDrivetrain.rightDrivePIDF;

        doAnswer(invocation -> {
            return leftSpeed;
        }).when(NMFEncoder).getVelocity();

        doAnswer(invocation -> {
            return rightSpeed;
        }).when(NMFSpark).getEncoder();

        doAnswer(invocation -> {
            return omniEncoder;
        }).when(omniSpark).getEncoder();

        doAnswer(invocation -> {
            Double input = invocation.getArgument(0, Double.class);
            double setpoint = NMFCurrentToSpeedRatio*input;
            leftSpeed += (AdditionalMath.isInRange(leftSpeed, setpoint - 0.1, setpoint + 0.1, false)) ? Math.signum(setpoint - leftSpeed)*NMFMaxAccel*input : 0;
            return null;
        }).when(omniSpark).set(any(Double.class));

        doAnswer(invocation -> {
            Double input = invocation.getArgument(0, Double.class);
            double setpoint = -omniCurrentToSpeedRatio*input;
            leftSpeed += (AdditionalMath.isInRange(rightSpeed, setpoint - 0.1, setpoint + 0.1, false)) ? Math.signum(setpoint - rightSpeed)*NMFMaxAccel*input : 0;
            return null;
        }).when(omniSpark).set(any(Double.class));

        NMFController = new NMFController(NMFSpark, omniSpark);
    }

    public void testSetSpeed1() {

    }

    
    public void testSetSpeed2() {

    }

}