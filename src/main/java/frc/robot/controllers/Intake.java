package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.*;

import frc.robot.util.*;

public class Intake {
    private DoubleSolenoid flipSolenoid;
    private TalonSRX intakeTalon;

    public double currentIntakeSpeed;
    public double setIntakeSpeed;
    public final double targetIntakeSpeed = 1.0; //RPM
    public PID intakePID = new PID(0, 0, 0); //Need to tune

    private boolean intaking;

    private double startTime;
    private double lastTime;
    private double currentTime;
    private double deltaTime;

    public Intake() {
        flipSolenoid = new DoubleSolenoid(Context.IntakeFlipChannelA, Context.IntakeFlipChannelB);
        intakeTalon = new TalonSRX(Context.intakeMotorId);

        intakeTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        startTime = System.currentTimeMillis();
    }

    public void startIntaking() {
        flipSolenoid.set(DoubleSolenoid.Value.kForward);
        intaking = true;
    }

    public void stopIntaking() {
        flipSolenoid.set(DoubleSolenoid.Value.kReverse);
        intaking = false;
    }

    public void loop() {
        lastTime = currentTime;
        currentTime = Context.getRelativeTime(startTime);
        deltaTime = currentTime - lastTime;

        currentIntakeSpeed = intakeTalon.getSensorCollection().getQuadratureVelocity();

        if (intaking) {
            setIntakeSpeed = intakePID.update(targetIntakeSpeed, currentIntakeSpeed, deltaTime);
        } else {
            setIntakeSpeed = intakePID.update(0, currentIntakeSpeed, deltaTime);
        }
    }
}