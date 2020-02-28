package frc.robot.controllers;

// import edu.wpi.first.wpilibj.Encoder;
import frc.robot.util.Context;
import frc.robot.util.PID;
import com.revrobotics.CANSparkMax;
 
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
    public PID NMFPID = new PID(0, 0, 0); //Need to tune

    public double omniCurrentSpeed; //Encoder-read speed
    public double omniSetSpeed; //The speed to set based on PID
    public double omniTargetSpeed; //The current desired speed;
    public PID omniPID = new PID(0, 0, 0); //Need to tune

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
        lastTime = currentTime;
        currentTime = Context.getRelativeTimeSeconds(startTime/1000)*1000;
        deltaTime = currentTime - lastTime;

        NMFcurrentSpeed = NMFencoder.getVelocity();
        NMFsetSpeed = NMFPID.update(NMFtargetSpeed, NMFcurrentSpeed, deltaTime);
        NMFspark.set(NMFsetSpeed);

        omniCurrentSpeed = omniEncoder.getVelocity();
        omniSetSpeed = omniPID.update(omniTargetSpeed, omniCurrentSpeed, deltaTime);
        omniSpark.set(omniSetSpeed);

    }
}
