package frc.robot.util;

public class JRADD {
    /*
    Intersection of 254's JRAD with a P gain (so as to create a rev-up "brake").
    1. v'[t + dt] = kF * setpoint + v'[t] + kI * dt * (kLoadRatio * setpoint – actual)
    2. P = kF/((1 - kT) * kLoadRatio * loadRatio)
        removes kF control at desired speed and preemptively slows the shooter
        loadRatio is adjusted by desiredValue
    3. v[t + dt] = v'[t + dt] + kP * P * (kT * setpoint - actual)
    */

    /////Variable Declarations///
    public double kP;
    //Proportonal gain
    public double kT;
    //Point at which proportional gain resists increase
    public double kF; 
    //Feed-forward gain
    public double kI; 
    //Integral gain
    public double kLoadRatio;
    //Offset ratio modifier to account for flywheel loss of energy
    //when firing ball
    public double loadRatioConstant;
    //Y-intercept in loadRatio linear regression
    public double loadRatioRate;
    //Slope in loadRatio linear regression
    
    public double updateValue;
    //Returned double from the update method
    public double loadRatio;
    //Offset on desired to account for energy loss, calculated via testing
    public double proportional;
    //Braking term using to counteract kF accumulation at target point/overshoot
    public double output;
    //Final value returned

    //@param double kF (feed forward coeff), kI (integral coeff), double kLoadRatio (offset for setpoint coeff)
    public JRADD(double kP, double kT, double kF, double kI, double kLoadRatio, double loadRatioConstant, double loadRatioRate) {
        this.kP = kP;
        this.kF = kF;
        this.kI = kI;
        this.kT = kT;
        this.kLoadRatio = kLoadRatio;
        this.loadRatioConstant = loadRatioConstant;
        this.loadRatioRate = loadRatioRate;
        updateValue = 0;
    }

    //Runs through a step of the controller
    //@param double setpoint (desired value), double actual (actual state of value), dt (time since last update)
    public double update(double setpoint, double actual, double dt) {
        //JRAD Math
        loadRatio = loadRatioConstant + loadRatioRate * Math.abs(setpoint);
        updateValue = kF * setpoint + updateValue + kI * dt * (kLoadRatio * loadRatio * setpoint - actual);
        if(kT < 1) {
            proportional = kF/((1 - kT) * kLoadRatio * loadRatio);
        } else {
            proportional = 1;
        }
        output = updateValue + kP * proportional * (kT * kLoadRatio * loadRatio * setpoint - actual);
        return output;
    }
}