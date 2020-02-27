package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.*;

import frc.robot.util.*;

public class Intake {
    private DoubleSolenoid flipSolenoid;
    private TalonSRX intakeTalon;

    public final double intakingSpeed = 1.0; //The speed to rotate at while intaking

    public double setSpeed; //The speed to set based on not PID


    public Intake(TalonSRX IntakeTalon, DoubleSolenoid FlipSolenoid) {
        flipSolenoid = FlipSolenoid;
        intakeTalon = IntakeTalon;
    }

    public void flipOut() {
        flipSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void flipIn() {
        flipSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public void beginIntaking() {
        setSpeed = intakingSpeed;
    }

    public void stopIntaking() {
        setSpeed = 0;
    }

    public void loop() {
        intakeTalon.set(ControlMode.PercentOutput, setSpeed); //motion profiling later, ik this is bad
    }
}