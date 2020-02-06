package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.robot.util.*;

public class Climber
{
    public TalonSRX coilMotor;
    public CANSparkMax telescopeMotor;
    PID liftPID;
    long pastTime;
    double desiredPosition;


    public Climber(){
        coilMotor = new TalonSRX (Context.climberMotorID);
        telescopeMotor = new CANSparkMax(Context.climberMotorID, MotorType.kBrushless);
        liftPID = new PID(1, 0, 0); //Not tuned & not used
        pastTime = System.currentTimeMillis();

    }
    public void telescopeUp(double PIDVal) {
        telescopeMotor.set(ControlMode.PercentOutput, PIDVal);
    }

    public void telescopeDown(double PIDVal) {
        telescopeMotor.set(ControlMode.PercentOutput, PIDVal);
    }

    public void coil(){
        coilMotor.set(ControlMode.PercentOutput, Context.coilSpeed);
    }

    public void uncoil(){
        coilMotor.set(ControlMode.PercentOutput, Context.uncoilSpeed);
    }

    public void loop() {
        double currentPosition = telescopeMotor.getPosition();

        long currentTime = System.currentTimeMillis();
        double deltaTime = currentTime - pastTime;
        if (Context.robotController.driverJoystick.getClimbU())
        {
            desiredPosition = 1;
            telescopeUp(liftPID.update(desiredPosition, currentPosition, deltaTime));
        }
        else if (Context.robotController.driverJoystick.getClimbD())
        {
            desiredPosition = 0;
            telescopeDown(liftPID.update(desiredPosition, currentPosition, deltaTime));
            coil();
        }
        pastTime = currentTime;
    }
}