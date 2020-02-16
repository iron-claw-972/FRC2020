package frc.robot.util;

public class JRADD {
    /*
    Intersection of 254's JRAD with a P gain (so as to offset late-stage kF accumulation).
    v'[t + dt] = kF * setpoint + v'[t] + kI * dt * (kLoadRatio * setpoint – actual)
    v[t + dt] = v'[t + dt] + kP * (setpoint - actual)
    */

    /////Variable Declarations///
    private double kP;
    //Proportonal gain
    private double kF; 
    //Feed-forward gain
    private double kI; 
    //Integral gain
    private double kLoadRatio;
    //Offset ratio to account for flywheel loss of energy
    //when firing ball
    
    public double updateValue;
    //Returned double from the update method

    //@param double kF (feed forward coeff), kI (integral coeff), double kLoadRatio (offset for setpoint coeff)
    public JRADD(double kP, double kF, double kI, double kLoadRatio) {
        this.kP = kP;
        this.kF = kF;
        this.kI = kI;
        this.kLoadRatio = kLoadRatio;
        updateValue = 0;
    }

    //Runs through a step of the controller
    //@param double setpoint (desired value), double actual (actual state of value), dt (time since last update)
    public double update(double setpoint, double actual, double dt) {
        //JRAD Math
        updateValue = kF * setpoint + updateValue + kI * dt * (kLoadRatio * setpoint - actual);
        return updateValue + kP * (setpoint - actual);
    }
}