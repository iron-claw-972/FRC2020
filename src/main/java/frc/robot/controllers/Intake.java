package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.*;

import frc.robot.util.*;

public class Intake {
    private DoubleSolenoid flipSolenoid;
    private TalonSRX intakeMotor;

    public static final double targetIntake = 0.1;

    private boolean intaking;

    public Intake() {
        flipSolenoid = new DoubleSolenoid(Context.IntakeFlipChannelA, Context.IntakeFlipChannelB);
        intakeMotor = new TalonSRX(Context.intakeMotorId);
    }

    public void loop() {
        if (intaking) {
            flipSolenoid.set(DoubleSolenoid.Value.kForward);
            intakeMotor.set(ControlMode.PercentOutput, targetIntake);
        } else {
            flipSolenoid.set(DoubleSolenoid.Value.kReverse);
            intakeMotor.set(ControlMode.PercentOutput, 0);
        }
    }
}