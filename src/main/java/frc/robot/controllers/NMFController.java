package frc.robot.controllers;

// import edu.wpi.first.wpilibj.Encoder;
import frc.robot.util.Context;
import frc.robot.util.PID;
import frc.robot.util.PIDF;

import com.revrobotics.CANSparkMax;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.*;

public class NMFController {
    CANSparkMax NMFspark;
    CANEncoder NMFencoder;
    CANSparkMax omniSpark;
    CANEncoder omniEncoder;
    TalonSRX nmfEncoderInterface;

    public static enum State {
        IDLE, INTAKING, SHOOTING;
    }
    public State state;
    public boolean reversed;
    public boolean stopped;

    private final double NMFGearRatio = (1.0/10) * (16.0/22) * (18.0/84); //NMF rotations / motor rotations

    public double NMFidleSpeed = 10;
    public double NMFintakeSpeed = 25;
    public double NMFshootingSpeed = 30;
    public double NMFreverseSpeed = -20;
    public double omniForwardsSpeed = -10;
    public double omniReverseSpeed = -10;

    public double NMFcurrentSpeed; //Encoder-read speed
    public double NMFsetSpeed; //The speed to set based on PID
    public double NMFtargetSpeed; //The current desired speed;
    public double NMFrememberedSpeed;
    public PIDF NMFPID = new PIDF(0.003, 0, 0, 0.008); //Need to tune

    public double omniCurrentSpeed; //Encoder-read speed
    public double omniSetSpeed; //The speed to set based on PID
    public double omniTargetSpeed; //The current desired speed;
    public PIDF omniPID = new PIDF(-0.005, 0, 0, 0); //Need to tune

    private double omniGearRatio = 1.0/10.0 * 24.0/18.0;

    private double startTime;
    private double lastTime;
    private double currentTime;
    private double deltaTime;
    

    public NMFController(CANSparkMax nmfSpark, CANSparkMax OmniSpark, TalonSRX nmfEncoderInterface){
        NMFspark = nmfSpark;
        NMFencoder = NMFspark.getEncoder();
        omniSpark = OmniSpark;
        omniEncoder = omniSpark.getEncoder();
        state = State.IDLE;
        this.nmfEncoderInterface = nmfEncoderInterface;
        nmfEncoderInterface.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        lastTime = startTime;
    }

    public void spinNMFIntaking(){
        NMFtargetSpeed = NMFintakeSpeed;
        state = State.INTAKING;
    }

    public void spinNMFIdle(){
        NMFtargetSpeed = NMFidleSpeed;
        state = State.IDLE;
    }

    public void spinNMFShooting(){
        NMFtargetSpeed = NMFshootingSpeed;
        state = State.SHOOTING;
    }

    public void spinNMFReverse(){
        NMFrememberedSpeed = NMFtargetSpeed;
        NMFtargetSpeed = NMFreverseSpeed;
        reversed = true;
    }

    public void spinNMFForward(){
        NMFtargetSpeed = NMFrememberedSpeed;
        reversed = false;
    }

    public void stopNMF(){
        NMFtargetSpeed = 0;
    }

    public void startNMF(){
        switch (state){
            case IDLE:
                NMFtargetSpeed = NMFidleSpeed;
                break;
            case SHOOTING:
                NMFtargetSpeed = NMFshootingSpeed;
                break;
            case INTAKING:
                NMFtargetSpeed = NMFintakeSpeed;
        }
    }

    public void spinOmni(){
        omniTargetSpeed = omniForwardsSpeed;
    }

    public void spinOmniReverse(){
        omniTargetSpeed = omniReverseSpeed;
    }

    public void stopOmni(){
        omniSetSpeed = 0;
    }
    
    public void loop(){
        currentTime = System.currentTimeMillis();
        deltaTime = currentTime - lastTime;

        NMFcurrentSpeed = NMFencoder.getVelocity() * NMFGearRatio;
        NMFsetSpeed = NMFPID.update(NMFtargetSpeed, NMFcurrentSpeed, deltaTime);
        NMFspark.set(NMFsetSpeed);

        omniCurrentSpeed = omniEncoder.getVelocity() * omniGearRatio;
        omniSetSpeed = omniPID.update(omniTargetSpeed, omniCurrentSpeed, deltaTime);
        omniSpark.set(omniSetSpeed);
        lastTime = currentTime;

        System.out.println("vel: " + omniCurrentSpeed + ", set: " + omniTargetSpeed + ", pow: " + omniSetSpeed + ", pos: " + omniEncoder.getPosition());
    }

    public double getNMFcurrentSpeed() {
        return NMFcurrentSpeed;
    }

    public double getOmniCurrentSpeed() {
        return omniCurrentSpeed;
    }

    public double getNMFError() {
        return NMFtargetSpeed - NMFcurrentSpeed;
    }

    public double getOmniError() {
        return omniTargetSpeed - omniCurrentSpeed;
    }
}
