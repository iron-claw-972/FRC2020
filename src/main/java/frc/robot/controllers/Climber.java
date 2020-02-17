package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.util.*;

public class Climber
{
    public TalonSRX coilMotor;
    public CANSparkMax telescopeMotor;
    PID liftPID;
    long pastTime;
    double desiredPosition;

    //Initializes Climber with Talon SRX motor, CANSparkMax, PID for the telescope, and initial time
    public Climber(TalonSRX coilMotor_, CANSparkMax telescopeMotor_){
        coilMotor = coilMotor_;
        telescopeMotor = telescopeMotor_;
        liftPID = new PID(1, 0, 0); //Not tuned & not used
        pastTime = System.currentTimeMillis();

    }
    //Moves the telescope
    public void telescopeMove(double PIDVal) {
        telescopeMotor.set(PIDVal);
    }

    //Spins the motor to coil the winch
    public void coil(){
        coilMotor.set(ControlMode.PercentOutput, Context.coilSpeed);
    }

    //Spins the motor to uncoil the winch
    public void uncoil(){
        coilMotor.set(ControlMode.PercentOutput, Context.uncoilSpeed);
    }

    //Loop to react to button press
    public double loop() {
        //Finds current encoder value of the wheel, the current time and the change in time since the last run
        double currentPosition = telescopeMotor.getEncoder().getPosition();
        long currentTime = System.currentTimeMillis();
        double deltaTime = currentTime - pastTime;
        //depending on button press sets desired position and updates the PID for the power
        if (Context.robotController.driverJoystick.getClimbU())
        {
            desiredPosition = 1;
        }
        else if (Context.robotController.driverJoystick.getClimbD())
        {
            desiredPosition = 0;
            coil();
        }
        double pidVal = liftPID.update(desiredPosition, currentPosition, deltaTime);
        telescopeMove(pidVal);
        //updates the past time for next loop
        pastTime = currentTime;
        return pidVal;
    }
}