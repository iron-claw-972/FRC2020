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
    private double setVelocity;
    private double currentVelocity;

    private TalonSRX shooterTalon;

    public ShooterController() {
        shooterTalon = new TalonSRX(1);
        shooterTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        velocityPID = new PID(0, 0, 0);
    }

    public void updateParameters() {
        currentVelocity = shooterTalon.getSensorCollection().getQuadratureVelocity();
        lastTime = time;
        time = Context.getRelativeTime(startTime);
        deltaTime = time - lastTime;
        setVelocity = velocityPID.update(desiredVelocity, currentVelocity, deltaTime);
    }

    public void updateVelocity() {
        //wait for motor power converter object
    }


}