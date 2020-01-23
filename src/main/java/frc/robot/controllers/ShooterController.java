package frc.robot.controllers;

// import edu.wpi.first.wpilibj.Encoder;
import frc.robot.util.Context;
import frc.robot.util.PID;
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.*;

public class ShooterController {

    private PID velocityPID;

    private double time; //time that has passed since start
    private double lastTime; //time of last update
    private double deltaTime; //time from last update
    private double startTime; //time at which PID starts

    private double desiredVelocity; //desired velocity from flywheel
    private double actualVelocity; //actual velocity from flywheel
    private double setVelocity; //the velocity to which the flywheel is being set
    private double setCurrent; //current that will be passed to motor controller (PercentOutput)

    private TalonSRX shooterTalon;

    private final double minCurrent = 0; //minimum current needed for flywheel motor to overcome friction, etc. (to go into motion)
    private final double speedToCurrentRate = 0; //the linear conversion rate between a velocity and necessary current
    
    private final double RADIUS = 0;

    public ShooterController() {
        //initialize parameters
        shooterTalon = new TalonSRX(1);
        shooterTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
        velocityPID = new PID(0, 0, 0); //need tuning
        startTime = System.currentTimeMillis();
    }

    private void updateParameters() {
        //updates all necessary
        actualVelocity = flywheelVelocity();
        lastTime = time;
        time = Context.getRelativeTime(startTime);
        deltaTime = time - lastTime;
        setVelocity = velocityPID.update(desiredVelocity, actualVelocity, deltaTime);
    }

    private void updateVelocity() {
        //passes input to motor controller
        setCurrent = speedConverter(setVelocity);
        shooterTalon.set(ControlMode.PercentOutput, setCurrent);
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

    private double flywheelVelocity() {
        //get the linear speed of the flywheel
        return RADIUS * shooterTalon.getSensorCollection().getQuadratureVelocity();
    }


}
