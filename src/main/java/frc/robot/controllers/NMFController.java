package frc.robot.controllers;

// import edu.wpi.first.wpilibj.Encoder;
import frc.robot.util.Context;
import frc.robot.util.PID;
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.*;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
 
import com.revrobotics.*;

public class NMFController {
    CANSparkMax NMFspark;
    CANEncoder NMFencoder;
    CANSparkMax omniSpark;
    CANEncoder omniEncoder;

    public double NMFidleSpeed = 0;
    public double NMFintakeSpeed = 0;
    public double NMFshootingSpeed = 0;
    public double NMFreverseSpeed = 0;
    public double omniForwardsSpeed = 0;
    public double omniReverseSpeed = 0;

    public double NMFcurrentSpeed; //Encoder-read speed
    public double NMFsetSpeed; //The speed to set based on PID
    public double NMFtargetSpeed; //The current desired speed;
    public PID NMFPID = new PID(0.0001, -0.01, 0); //Need to tune

    public double omniCurrentSpeed; //Encoder-read speed
    public double omniSetSpeed; //The speed to set based on PID
    public double omniTargetSpeed; //The current desired speed;
    public PID omniPID = new PID(0.0001, -0.01, 0); //Need to tune

    private double startTime;
    private double lastTime;
    private double currentTime;
    private double deltaTime;
    

    public NMFController(CANSparkMax nmfSpark, CANSparkMax OmniSpark){
        NMFspark = nmfSpark;
        NMFencoder = NMFspark.getEncoder();
        omniSpark = OmniSpark;
        omniEncoder = omniSpark.getEncoder();
    }

    public void spinNMFIntaking(){
        NMFtargetSpeed = NMFintakeSpeed;
    }

    public void spinNMFIdle(){
        NMFtargetSpeed = NMFidleSpeed;
    }

    public void spinNMFShooting(){
        NMFtargetSpeed = NMFshootingSpeed;
    }

    public void spinNMFReverse(){
        NMFtargetSpeed = NMFreverseSpeed;
    }

    public void stopNMF(){
        NMFtargetSpeed = 0;
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
        updateDeltaTime();
        updateNMFSpeed();
        updateOmniSpeed();
    }

    public void updateDeltaTime() {
        lastTime = currentTime;
        currentTime = Context.getRelativeTimeSeconds(startTime/1000)*1000;
        deltaTime = currentTime - lastTime;
    }

    public void updateNMFSpeed() {
        NMFcurrentSpeed = NMFencoder.getVelocity();
        System.out.println("NMF vel: " + NMFcurrentSpeed + " NMF target: " + NMFtargetSpeed);
        NMFsetSpeed = NMFPID.update(NMFtargetSpeed, NMFcurrentSpeed, deltaTime);
        System.out.println("NMF Set: " + NMFsetSpeed);
        NMFspark.set(NMFsetSpeed);
    }

    public void updateOmniSpeed() {
        omniCurrentSpeed = omniEncoder.getVelocity();
        System.out.println("Omni vel: " + omniCurrentSpeed + " Omni target: " + omniTargetSpeed);
        omniSetSpeed = omniPID.update(omniTargetSpeed, omniCurrentSpeed, deltaTime);
        System.out.println("Omni Set: " + omniSetSpeed);
        omniSpark.set(omniSetSpeed);
    }

    public double getNMFcurrentSpeed() {
        return NMFcurrentSpeed;
    }

    public double getOmniCurrentSpeed() {
        return omniCurrentSpeed;
    }

    public void setDeltaTime(double _deltaTime) {
        deltaTime = _deltaTime;
    }

    public void setNMFTargetSpeed(double _NMFTargetSpeed) {
        NMFtargetSpeed = _NMFTargetSpeed;
    }
    
    public void setOmniTargetSpeed(double _omniTargetSpeed) {
        omniTargetSpeed = _omniTargetSpeed;
    }

    public void setNMFcurrentSpeed(double _NMFCurrentSpeed) {
        NMFcurrentSpeed = _NMFCurrentSpeed;
    }

    public void setOmniCurrentSpeed(double _omniCurrentSpeed) {
        omniCurrentSpeed = _omniCurrentSpeed;
    }
}
