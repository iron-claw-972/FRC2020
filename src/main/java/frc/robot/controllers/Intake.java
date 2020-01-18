package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import frc.robot.util.*;

public class Intake
{
    Joystick joy = new Joystick(0);
    TalonSRX rollingIntake = new TalonSRX(0);
    TalonSRX beltIntake = new TalonSRX(1);
    PID beltSpeed = new PID(1, 0, 0);
    DigitalInput limitSwitch = new DigitalInput(1);

    public void roll()
    {
        rollingIntake.set(ControlMode.PercentOutput, joy.getRawAxis(0));
    }
    
    public void belt()
    {
        if(true)
        {
            beltIntake.set(ControlMode.PercentOutput, 0.5);
        }
    }
}