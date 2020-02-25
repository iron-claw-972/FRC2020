package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.*;

// import edu.wpi.first.wpilibj.Encoder;
import frc.robot.util.*;

public class ShooterController {

    public double MAX_CURRENT = 0.9;
    public double kP = 5;
    public double kT = 0.75;
    public double kF = 0.0006;
    public double kI = 5.25;
    public double loadRatioConstant = 1.375562266718773;
    public double loadRatioRate = -0.01962825016; //-0.011438971256922784 pre-velocity correction
    public double kLoadRatio = 1.0;

    public JRADD velocityJRADD;
    public PID velocityPID;

    private final double velCorrectCoeff = 0.582781;
    //Calculated via testing

    private double time; //time 7that has passed since start
    private double lastTime; //time of last update
    private double deltaTime; //time from last update
    private long startTime; //time at which game starts/obj. init.
    private boolean shooting;

    private double desiredVelocity; //desired velocity from flywheel
    private double actualVelocity; //actual velocity from flywheel
    private double lastVelocity; //previous velocity
    private double setVelocity; //the velocity to which the flywheel is being set
    private double setCurrent; //current that will be passed to motor controller (PercentOutput)

    private double actualAccel;

    public TalonFX shooterTalon;
    private int shooterID;
    private boolean orientation;

    private final double minCurrent = 0.0560258; //minimum current needed for flywheel motor to overcome friction, etc. (to go into motion)
    private final double speedToCurrentRate = 0.0125859; //the linear conversion rate between a velocity and necessary current

    private RecursiveMotionProfile motionProf;
    
    //Measurements in meters
    private double M_SHOOTING_RADIUS;

    public ShooterController(int shooterID, boolean orientation) {
        //initialize parameters
        this.shooterID = shooterID;
        shooterTalon = new TalonFX(shooterID);
        shooterTalon.setNeutralMode(NeutralMode.Brake);
        this.orientation = orientation;
        shooterTalon.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        //velocityJRADD = new JRADD(kP, kT, kF, kI, kLoadRatio, loadRatioConstant, loadRatioRate); //need tuning
        velocityJRADD = new JRADD(kP, kT, kF, kI, kLoadRatio, 1, 0);
        M_SHOOTING_RADIUS = Context.M_FLYWHEEL_RADIUS + Context.M_BALL_DIAMETER/2;
        startTime = System.currentTimeMillis();
        motionProf = new RecursiveMotionProfile(50, 200, 9, 0.1);
    }

    private void updateParameters() {
        //updates all necessary
        if(!shooting) {
            lastTime = Context.getRelativeTime(startTime);
            velocityJRADD = new JRADD(velocityJRADD.kP, velocityJRADD.kT, velocityJRADD.kF, velocityJRADD.kI, velocityJRADD.kLoadRatio, 1, 0);
            lastVelocity = flywheelVelocity();
            shooting = true;
        } else {
            lastTime = time;
        }
        time = Context.getRelativeTime(startTime);
        deltaTime = time - lastTime;
        actualVelocity = flywheelVelocity(); //accounts for fact that ball rolls on inside of hood
        actualAccel = (actualVelocity - lastVelocity)/deltaTime;
        lastVelocity = actualVelocity;
        motionProf.updateParameters(evalLoadRatio(desiredVelocity), actualVelocity, actualAccel);;
        setVelocity = velocityJRADD.update(motionProf.getVelNext(), actualVelocity, deltaTime);
    }

    private double evalLoadRatio(double input) {
        return loadRatioConstant + loadRatioRate * Math.abs(input);
    }

    private void updateVelocity() {
        //passes input to motor controller
        setCurrent = AdditionalMath.Clamp(speedConverter(setVelocity), -MAX_CURRENT, MAX_CURRENT);
        shooterTalon.set(ControlMode.PercentOutput, setCurrent);
    }

    public void startShooting() {
        shooting = false;
    }

    public void loop() {
        //execute update methods
        updateParameters();
        updateVelocity();
    }
    
    public void loop(double desiredVelocity) {
        //change desiredVelocity, and then execute update methods
        this.desiredVelocity = (orientation) ? desiredVelocity:-desiredVelocity;
        loop();
    }

    private double speedConverter(double speed) {
        return Math.signum(speed) * (Math.abs(speed) * speedToCurrentRate + minCurrent);
        //converts a desired speed into a motor controller input with ControlMode.PercentOutput
        //speedToCurrentRate, minCurrent calculated via linear regression (best fit)
    }

    public double flywheelVelocity() {
        //get the linear speed of the flywheel
        //Sensor output is clicks/0.1s
        return velCorrectCoeff * M_SHOOTING_RADIUS * 2 * Math.PI * 10 * shooterTalon.getSelectedSensorVelocity()/Context.FALCON_ENCODER_CPR / 2;
    }

    public double flywheelRPM() {
        //get the RPM of the flywheel
        return 600 * shooterTalon.getSelectedSensorVelocity()/2048;
    }
    
    public double getDesiredVelocity() {
        return desiredVelocity;
    }

    public double getSetVelocity() {
        return setVelocity;
    }
}
