package frc.robot.controllers.drive;

import com.revrobotics.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
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
    public final double NMFCurrentToSpeedRatio = 6000;
    public final double NMFMaxAccel = 2;
    public final double omniCurrentToSpeedRatio = 12000;
    public final double omniMaxAccel = 2;

    public final double dragCoefficient = 0.8;
    public final double errorTolerance = 50;

    public final double totalTime = 100;
    public final double deltaTime = 0.02;

    @Before
    public void setup() {
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
            double setpoint = NMFCurrentToSpeedRatio * input;
            NMFSpeed = setpoint * dragCoefficient;
            return null;
        }).when(NMFSpark).set(any(Double.class));

        doAnswer(invocation -> {
            Double input = invocation.getArgument(0, Double.class);
            double setpoint = omniCurrentToSpeedRatio*input;
            omniSpeed = setpoint * dragCoefficient;
            return null;
        }).when(omniSpark).set(any(Double.class));

        NMFController = new NMFController(NMFSpark, omniSpark);
    }

    @Test
    public void testSetSpeed1() {

        double NMFTargetSpeed = 60;
        double omniTargetSpeed = 1000;

        NMFController.setNMFTargetSpeed(NMFTargetSpeed);
        NMFController.setOmniTargetSpeed(omniTargetSpeed);

        for(double time = 0; time < totalTime; time += deltaTime) {
            NMFController.setDeltaTime(deltaTime);
            NMFController.updateNMFSpeed();
            NMFController.updateOmniSpeed();
        }

        double NMFFinalSpeed = NMFController.getNMFcurrentSpeed();
        double omniFinalSpeed = NMFController.getOmniCurrentSpeed();

        System.out.println("NMF, Fin: " + NMFFinalSpeed + " init: " + NMFTargetSpeed + "   Omni, final: " + omniFinalSpeed + " init: " + omniTargetSpeed);
        assertEquals(NMFTargetSpeed, NMFFinalSpeed, errorTolerance);
        assertEquals(omniTargetSpeed, omniFinalSpeed, errorTolerance);
    }

    @Test
    public void testSetSpeed2() {

        double NMFTargetSpeed = 100;
        double omniTargetSpeed = 800;

        NMFController.setNMFTargetSpeed(NMFTargetSpeed);
        NMFController.setOmniTargetSpeed(omniTargetSpeed);

        for(double time = 0; time < totalTime; time += deltaTime) {
            NMFController.setDeltaTime(deltaTime);
            NMFController.updateNMFSpeed();
            NMFController.updateOmniSpeed();
        }

        double NMFFinalSpeed = NMFController.getNMFcurrentSpeed();
        double omniFinalSpeed = NMFController.getOmniCurrentSpeed();

        System.out.println("NMF, Fin: " + NMFFinalSpeed + " init: " + NMFTargetSpeed + "   Omni, final: " + omniFinalSpeed + " init: " + omniTargetSpeed);
        assertEquals(NMFTargetSpeed, NMFFinalSpeed, errorTolerance);
        assertEquals(omniTargetSpeed, omniFinalSpeed, errorTolerance);
    }

    @Test
    public void testSetSpeed3() {
        
        double NMFTargetSpeed = 500;
        double omniTargetSpeed = 6000;

        NMFController.setNMFTargetSpeed(NMFTargetSpeed);
        NMFController.setOmniTargetSpeed(omniTargetSpeed);

        for(double time = 0; time < totalTime; time += deltaTime) {
            NMFController.setDeltaTime(deltaTime);
            NMFController.updateNMFSpeed();
            NMFController.updateOmniSpeed();
        }

        double NMFFinalSpeed = NMFController.getNMFcurrentSpeed();
        double omniFinalSpeed = NMFController.getOmniCurrentSpeed();

        System.out.println("NMF, Fin: " + NMFFinalSpeed + " init: " + NMFTargetSpeed + "   Omni, final: " + omniFinalSpeed + " init: " + omniTargetSpeed);
        assertEquals(NMFTargetSpeed, NMFFinalSpeed, errorTolerance);
        assertEquals(omniTargetSpeed, omniFinalSpeed, errorTolerance);
    }

}