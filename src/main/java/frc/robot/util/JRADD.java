package frc.robot.util;

public class JRADD {
    /*
    Intersection of 254's JRAD with a P gain (so as to offset late-stage kF accumulation).
    v'[t + dt] = kF * setpoint + v'[t] + kI * dt * (kLoadRatio * setpoint – actual)
    v[t + dt] = v'[t + dt] + kP * (kT * setpoint - actual)
    kP calculated by ~ kF/(kLoadRatio - kT), where Thres. is >=1, <=kLoadRatio
        removes kF control at peak and slows the shooter
    */

    //TODO: Add linear kLoadRatio function to account for constant friction energy loss

    /////Variable Declarations///
    private double kP;
    //Proportonal gain
    private double kT;
    //Point at which proportional gain resists increase
    private double kF; 
    //Feed-forward gain
    private double kI; 
    //Integral gain
    private double kLoadRatio;
    //Offset ratio modifier to account for flywheel loss of energy
    //when firing ball
    private double loadRatioConstant;
    //Y-intercept in loadRatio linear regression
    private double loadRatioRate;
    //Slope in loadRatio linear regression
    
    private double updateValue;
    //Returned double from the update method
    private double loadRatio;
    //Offset on desired to account for energy loss, calculated via testing

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
        loadRatio = loadRatioConstant + loadRatioRate * setpoint;
        updateValue = kF * setpoint + updateValue + kI * dt * (kLoadRatio * loadRatio * setpoint - actual);
        return updateValue + kP * (kT * setpoint - actual);
    }
}