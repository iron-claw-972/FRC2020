package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.robot.util.*;

public class Climber
{
    TalonSRX coilMotor;
    PID liftPID;

    public Climber(){
        coilMotor = new TalonSRX (Context.climberMotorID);
        liftPID = new PID(1, 0, 0); //Not tuned & not used
    }

    public void coil(){
        coilMotor.set(ControlMode.PercentOutput, Context.coilSpeed);
    }

    public void uncoil(){
        coilMotor.set(ControlMode.PercentOutput, Context.uncoilSpeed);
    }
}