package frc.robot.controllers;

// import edu.wpi.first.wpilibj.Encoder;
import frc.robot.util.Context;
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.*;
import frc.robot.util.*;

public class ShooterController {

    public double MAX_CURRENT = 0.9;
    public double kP = 5;
    public double kT = 0.75;
    public double kF = 0.0006;
    public double kI = 5.25;
    public double loadRatioConstant = 1.375562266718773;
    public double loadRatioRate = -0.01962825016;
    public double kLoadRatio = 1.0;

    private JRADD velocityJRADD;

    private final double velCorrectCoeff = 0.582781;

    private double time; //time that has passed since start
    private double lastTime; //time of last update
    private double deltaTime; //time from last update
    private double startTime; //time at which PID starts

    private double desiredVelocity; //desired velocity from flywheel
    private double actualVelocity; //actual velocity from flywheel
    private double setVelocity; //the velocity to which the flywheel is being set
    private double setCurrent; //current that will be passed to motor controller (PercentOutput)

    private TalonFX leftShooterTalon;
    private int leftShooterID;
    private TalonFX rightShooterTalon;
    private int rightShooterID;
    private boolean invertShooter; //inverts direction of motor rotation, true = positive is clockwise, false = positive is counter clockwise

    private final double minCurrent = 0.0560258; //minimum current needed for flywheel motor to overcome friction, etc. (to go into motion)
    private final double speedToCurrentRate = 0.0125859; //the linear conversion rate between a velocity and necessary current
    
    //Measurements in meters
    private double M_SHOOTING_RADIUS;

    public ShooterController(int _leftShooterID, int _rightShooterID, boolean _orientation) {
        //initialize variables

        leftShooterID = _leftShooterID;
        rightShooterID = _rightShooterID;
        invertShooter = _orientation;

        leftShooterTalon = new TalonFX(leftShooterID);
        leftShooterTalon.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        rightShooterTalon = new TalonFX(rightShooterID);
        rightShooterTalon.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

        velocityJRADD = new JRADD(kP, kT, kF, kI, kLoadRatio, loadRatioConstant, loadRatioRate);

        M_SHOOTING_RADIUS = Context.M_FLYWHEEL_RADIUS + Context.M_BALL_DIAMETER/2;
        startTime = System.currentTimeMillis();
    }

    private void updateParameters() {
        //updates all necessary

        actualVelocity = shootingVelocity();
        lastTime = time;
        time = Context.getRelativeTimeSeconds(startTime);
        deltaTime = time - lastTime;
        setVelocity = velocityJRADD.update(desiredVelocity, actualVelocity, deltaTime);
    }

    private void updateVelocity() {
        //passes input to motor controller

        setCurrent = AdditionalMath.Clamp(speedConverter(setVelocity), -MAX_CURRENT, MAX_CURRENT);
        leftShooterTalon.set(ControlMode.PercentOutput, -setCurrent); //Sign depends on motor invertShooter
        rightShooterTalon.set(ControlMode.PercentOutput, setCurrent);
    }

    public void startShooting() {
        //Used to update objects/parameters relying on constant updates after a period without updates

        lastTime = Context.getRelativeTimeSeconds(startTime);
        velocityJRADD = new JRADD(velocityJRADD.kP, velocityJRADD.kT, velocityJRADD.kF, velocityJRADD.kI, velocityJRADD.kLoadRatio, loadRatioConstant, loadRatioRate);
    }

    public void updateObjects() {
        //execute update methods

        updateParameters();
        updateVelocity();
    }
    
    public void setDesiredVelocity(double _desiredVelocity) {
        //change desiredVelocity, which is altered by invertShooter

        desiredVelocity = (invertShooter) ? _desiredVelocity : -_desiredVelocity;
    }

    private double speedConverter(double speed) {

        return Math.signum(speed) * (Math.abs(speed) * speedToCurrentRate + minCurrent);
        //converts a desired speed into a motor controller input with ControlMode.PercentOutput
        //speedToCurrentRate, minCurrent calculated via linear regression (best fit)
    }

    public double shootingVelocity() {
        //get the linear speed of the flywheel

        //Sensor output is clicks/0.1s
        //Accounts for ball rolling inside hood with division by 2
        return velCorrectCoeff * M_SHOOTING_RADIUS * 2 * Math.PI * 10 * rightShooterTalon.getSelectedSensorVelocity()/Context.FALCON_ENCODER_CPR / 2;
    }

    public double flywheelRPM() {
        //get the RPM of the flywheel

        return 600 * rightShooterTalon.getSelectedSensorVelocity()/2048;
    }
    
    public double getDesiredVelocity() {
        return desiredVelocity;
    }

    public double getSetVelocity() {
        return setVelocity;
    }

}
