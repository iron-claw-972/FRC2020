package frc.robot.controllers;

import edu.wpi.first.wpilibj.Encoder;
import frc.robot.util.Context;
import frc.robot.util.PID;
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.*;

public class ShooterController {

    private PID velocityPID;

    private double time;
    private double lastTime;
    private double deltaTime;
    private double startTime;

    private double desiredVelocity;
    private double currentVelocity;
    private double setVelocity;
    private double setCurrent;

    private TalonSRX shooterTalon;

    private final double minCurrent = 0;
    private final double speedToCurrentRate = 0;

    public ShooterController() {
        shooterTalon = new TalonSRX(1);
        shooterTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        velocityPID = new PID(0, 0, 0); //need tuning
        startTime = System.currentTimeMillis();
    }

    private void updateParameters() {
        currentVelocity = encoderVelocity();
        lastTime = time;
        time = Context.getRelativeTime(startTime);
        deltaTime = time - lastTime;
        setVelocity = velocityPID.update(desiredVelocity, currentVelocity, deltaTime);
    }

    private void updateVelocity() {
        setCurrent = speedConverter(setVelocity);
        shooterTalon.set(ControlMode.PercentOutput, setCurrent);
    }

    public void loop() {
        updateParameters();
        updateVelocity();
    }

    private double speedConverter(double speed) {
        return Math.signum(speed) * (Math.abs(speed) * speedToCurrentRate + minCurrent);
        //speedToCurrentRate, minCurrent calculated via linear regression (best fit)
    }

    private int encoderVelocity() {
        return shooterTalon.getSensorCollection().getQuadratureVelocity();
    }


}