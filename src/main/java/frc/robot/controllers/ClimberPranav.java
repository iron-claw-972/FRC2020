package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.util.*;

public class ClimberPranav
{
    public TalonSRX coilMotor1;
    public TalonSRX coilMotor2;
    public TalonSRX telescopeEncoderMotor;
    public CANSparkMax telescopeMotor;

    int currentLiftStep = 0;
    boolean PIDControllerOn = false;
    boolean PolyControllerOn = false;

    PID liftPID;
    long pastTime;
    double desiredPosition;
    double currentPosition;

    //Initializes Climber with Talon SRX motor, CANSparkMax, PID for the telescope, and initial time
    public ClimberPranav(TalonSRX coilMotor1_, TalonSRX coilMotor2_, TalonSRX telescopeEncoderMotor_, CANSparkMax telescopeMotor_){
        coilMotor1 = coilMotor1_;
        coilMotor2 = coilMotor2_;
        telescopeMotor = telescopeMotor_;
        telescopeEncoderMotor = telescopeEncoderMotor_;
        telescopeEncoderMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        liftPID = new PID(.0002, 0, 0.0004); //Not tuned & not used
        pastTime = System.currentTimeMillis(); 
    }
    public double  getPolyMotorPower(int step){
        double output = Math.pow(1.1, step*.28)/100 + .3;
        output = output>1 ? 1:output;
        output = currentPosition > 14400 ? 0:output;
        return output;
    }
    public void resetClimbEncoder() {
        telescopeEncoderMotor.setSelectedSensorPosition(0);
    }
    
    //Moves the telescope
    public void telescopeMove(double PIDVal) {
        telescopeMotor.set(PIDVal);
    }

    //Spins the motor to coil the winch
    public void coil(){
        coilMotor1.set(ControlMode.PercentOutput, Context.coilSpeed);
        coilMotor2.set(ControlMode.PercentOutput, Context.coilSpeed);
    }

    //Spins the motor to uncoil the winch
    public void uncoil(){
        coilMotor1.set(ControlMode.PercentOutput, Context.uncoilSpeed);
        coilMotor2.set(ControlMode.PercentOutput, Context.uncoilSpeed);
    }

    //Loop to react to button press
    public void loop() {
        //Finds current encoder value of the wheel, the current time and the change in time since the last run
        currentPosition = telescopeEncoderMotor.getSelectedSensorPosition();
        System.out.println("Encoder Value: " + currentPosition);
        long currentTime = System.currentTimeMillis();
        double deltaTime = currentTime - pastTime;
        desiredPosition = currentPosition;
        //depending on button press sets desired position and updates the PID for the power
        if (Context.robotController.driverJoystick.getClimbU())
        {
            currentLiftStep++;
            desiredPosition = 14000;
            PIDControllerOn = false;
            PolyControllerOn = true;
            //uncoil();
        }
        else if (Context.robotController.driverJoystick.getClimbD())
        {
            desiredPosition = 50;
            PIDControllerOn = true;
            PolyControllerOn = false;
            //coil();
        }
        else{
            PIDControllerOn = false;
            PolyControllerOn = false;
        }

        double pidVal = liftPID.update(desiredPosition, currentPosition, deltaTime);


        if (PIDControllerOn){
            telescopeMove(-pidVal);
        }
        else if (PolyControllerOn){
            telescopeMove(-getPolyMotorPower(currentLiftStep));
        }
        else {
            telescopeMove(0);
        }


        
        
        //updates the past time for next loop
        pastTime = currentTime;
    }
}