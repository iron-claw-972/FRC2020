package frc.robot.controllers;

// import edu.wpi.first.wpilibj.Encoder;
import frc.robot.util.Context;
import frc.robot.util.PID;
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.*;
import frc.robot.util.*;

public class ShooterController {

    private final double kF = 0;
    private final double kI = 0;
    private final double kLoadRatio = 0;

    private JRAD velocityJRAD;

    private double time; //time that has passed since start
    private double lastTime; //time of last update
    private double deltaTime; //time from last update
    private double startTime; //time at which PID starts

    private double desiredVelocity; //desired velocity from flywheel
    private double actualVelocity; //actual velocity from flywheel
    private double setVelocity; //the velocity to which the flywheel is being set
    private double setCurrent; //current that will be passed to motor controller (PercentOutput)

    private TalonFX shooterTalon;

    private final double minCurrent = 0; //minimum current needed for flywheel motor to overcome friction, etc. (to go into motion)
    private final double speedToCurrentRate = 0; //the linear conversion rate between a velocity and necessary current
    
    //Measurements in meters
    private double M_SHOOTING_RADIUS;

    public ShooterController() {
        //initialize parameters
        shooterTalon = new TalonFX(Context.shooterMotorID);
        shooterTalon.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        velocityJRAD = new JRAD(kF, kI, kLoadRatio); //need tuning
        M_SHOOTING_RADIUS = Context.M_FLYWHEEL_RADIUS + Context.M_BALL_DIAMETER/2;
        startTime = System.currentTimeMillis()/1000;
    }

    private void updateParameters() {
        //updates all necessary
        actualVelocity = flywheelVelocity()/2; //accounts for fact that ball rolls on inside of hood
        lastTime = time;
        time = Context.getRelativeTimeSeconds(startTime);
        deltaTime = time - lastTime;
        setVelocity = velocityJRAD.update(desiredVelocity, actualVelocity, deltaTime);
    }

    private void updateVelocity() {
        //passes input to motor controller
        setCurrent = AdditionalMath.Clamp(speedConverter(setVelocity), -0.8, 0.8);
        shooterTalon.set(ControlMode.PercentOutput, -setCurrent); //Sign depends on motor orientation
    }

    public void loop() {
        //execute update methods
        updateParameters();
        updateVelocity();
    }
    
    public void loop(double desiredVelocity) {
        //change desiredVelocity, and then execute update methods
        this.desiredVelocity = desiredVelocity;
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
        return M_SHOOTING_RADIUS * 2 * Math.PI * 10 * shooterTalon.getSelectedSensorVelocity()/Context.FALCON_ENCODER_CPR;
    }

    public double flywheelRPM() {
        //get the RPM of the flywheel
        return 600 * shooterTalon.getSelectedSensorVelocity()/2048;
    }
    
    public double getDesiredVelocity() {
        return desiredVelocity;
    }

}
