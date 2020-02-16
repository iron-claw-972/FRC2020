package frc.robot.controllers;

// import edu.wpi.first.wpilibj.Encoder;
import frc.robot.util.Context;
import frc.robot.util.PID;
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.*;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
 
import com.revrobotics.*;

public class NMFController {
    TalonSRX NMFTalon;
    TalonSRX omniTalon;
    public double idleNMFSpeed = 0;
    public double intakeNMFSpeed = 0;
    public double omniSpeed = 0;

    public NMFController(){
        NMFTalon = new TalonSRX(Context.indexerTalonID);
        omniTalon = new TalonSRX(Context.omniTalonID);
    }
    public void spinNMFIntaking(){
        NMFTalon.set(ControlMode.PercentOutput, intakeNMFSpeed);
    }
    public void spinNMFIdle(){
        NMFTalon.set(ControlMode.PercentOutput, idleNMFSpeed);
    }

    public void spinOmni(){
        omniTalon.set(ControlMode.PercentOutput, omniSpeed);
    }
    
    public void stopNMF(){
        NMFTalon.set(ControlMode.PercentOutput, 0);
    }
    
    public void loop(){

    }
}
