package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.*;

import frc.robot.util.*;

public class Intake {
    private DoubleSolenoid flipSolenoid;
    private TalonSRX intakeTalon;
    private Encoder intakeEncoder;
    private Joystick joystick;

    public final double intakingSpeed = 1.0; //The speed to rotate at while intaking
    public final boolean automaticallyIntake = false; //Automatically begin/stop intaking if flipped out/in

    public double currentSpeed; //Encoder-read speed
    public double setSpeed; //The speed to set based on PID
    
    public double targetSpeed; //The current desired speed;
    public PID intakePID = new PID(0, 0, 0); //Need to tune

    private double startTime;
    private double lastTime;
    private double currentTime;
    private double deltaTime;

    public Intake() {
        flipSolenoid = new DoubleSolenoid(Context.intakeFlipChannelA, Context.intakeFlipChannelB);
        intakeTalon = new TalonSRX(Context.intakeMotorId);
        intakeEncoder = new Encoder(Context.intakeEncoderChannelA, Context.intakeEncoderChannelB);
        joystick = new Joystick(-1);

        startTime = System.currentTimeMillis();
    }

    public void flipOut() {
        flipSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void flipIn() {
        flipSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public void beginIntaking() {
        targetSpeed = intakingSpeed;
    }

    public void stopIntaking() {
        targetSpeed = 0;
    }

    public void loop() {
        if (joystick.getRawButtonPressed(-1)) {
            flipOut();
            if(automaticallyIntake) {beginIntaking();}
        } else if (joystick.getRawButtonPressed(-1)) {
            flipIn();
            if(automaticallyIntake) {stopIntaking();}
        }

        if (joystick.getRawButtonPressed(-1)) {
            beginIntaking();
        } else if (joystick.getRawButtonPressed(-1)) {
            stopIntaking();
        }

        lastTime = currentTime;
        currentTime = Context.getRelativeTime(startTime);
        deltaTime = currentTime - lastTime;

        currentSpeed = intakeEncoder.getRate();
        setSpeed = intakePID.update(targetSpeed, currentSpeed, deltaTime);

        intakeTalon.set(ControlMode.PercentOutput, setSpeed);
    }
}