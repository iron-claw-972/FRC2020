package frc.robot.controllers;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.*;
import frc.robot.util.Context;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.robot.util.Context;

import frc.robot.util.*;

public class Intake
{
    public TalonSRX intakeMotor;
    public PID beltSpeed = new PID(1, 0, 0);
    public DigitalInput limitSwitch = new DigitalInput(-1);
    public DoubleSolenoid intakePistons;
    public Intake()
    {
        intakePistons = new DoubleSolenoid(Context.intakePistonChannelA, Context.intakePistonChannelB);
        intakeMotor = new TalonSRX(Context.intakeTalonID);
    }

    public void retractIntake(){
        intakePistons.set(DoubleSolenoid.Value.kForward);
    }

    public void extendIntake(){
        intakePistons.set(DoubleSolenoid.Value.kReverse);
    }

    public void clearPistons(){
        intakePistons.set(DoubleSolenoid.Value.kOff);
    }

    public void startIntakeMotor(){
        intakeMotor.set(ControlMode.PercentOutput, .75);
    }

    public void stopIntakeMotor(){
        intakeMotor.set(ControlMode.PercentOutput, 0);
    }

    public void loop()
    {

    }

}