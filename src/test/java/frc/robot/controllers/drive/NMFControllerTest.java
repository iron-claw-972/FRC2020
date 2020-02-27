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

    public double NMFSpeed;
    public double omniSpeed;
    public final double NMFCurrentToSpeedRatio = 8;
    public final double NMFMaxAccel = 2;
    public final double omniCurrentToSpeedRatio = 8;
    public final double omniMaxAccel = 2;

    public PIDF NMFTestPIDF;
    public PIDF omniTestPIDF;

    public final double totalTime = 100;
    public final double deltaTime = 0.02;

    public void setup() {
        NMFTestPIDF = NeoDrivetrain.leftDrivePIDF;
        omniTestPIDF = NeoDrivetrain.rightDrivePIDF;

        doAnswer(invocation -> {
            return NMFSpeed;
        }).when(NMFEncoder).getVelocity();

        doAnswer(invocation -> {
            return omniSpeed;
        }).when(omniEncoder).getVelocity();

        doAnswer(invocation -> {
            return NMFEncoder;
        }).when(NMFSpark).getEncoder();

        doAnswer(invocation -> {
            return omniEncoder;
        }).when(omniSpark).getEncoder();

        doAnswer(invocation -> {
            Double input = invocation.getArgument(0, Double.class);
            double setpoint = NMFCurrentToSpeedRatio*input;
            NMFSpeed += (AdditionalMath.isInRange(NMFSpeed, setpoint - 0.1, setpoint + 0.1, false)) ? Math.signum(setpoint - NMFSpeed)*NMFMaxAccel*input : 0;
            return null;
        }).when(omniSpark).set(any(Double.class));

        doAnswer(invocation -> {
            Double input = invocation.getArgument(0, Double.class);
            double setpoint = -omniCurrentToSpeedRatio*input;
            omniSpeed += (AdditionalMath.isInRange(omniSpeed, setpoint - 0.1, setpoint + 0.1, false)) ? Math.signum(setpoint - omniSpeed)*NMFMaxAccel*input : 0;
            return null;
        }).when(omniSpark).set(any(Double.class));

        NMFController = new NMFController(NMFSpark, omniSpark);
    }

    public void testSetSpeed1() {
        for
    }

    
    public void testSetSpeed2() {

    }

}