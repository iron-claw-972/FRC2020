package frc.robot.util;

public class PIDF {
    /**Variable Declarations*/
	//Proportional, Integral and Derivative Coefficients
    public double pFactor, iFactor, dFactor, fFactor;
    //Integral Sum
    private double integral;
    //The previous error for derivative calculation
    private double lastError;

    /**Method Declarations*/
	//PID object
	//@param double pFactor (p_coeff), double iFactor (i_coeff), double dFactor (d_coeff)
	public PIDF(double pFactor_, double iFactor_, double dFactor_, double fFactor_) {
		pFactor = pFactor_;
		iFactor = iFactor_;
		dFactor = dFactor_;
		fFactor = fFactor_;
	}
	
	//Runs through one step of the PID
	//@param double setpoint_(goal value), double actual (current value), double timeFrame (time since last run)
	public double update(double setpoint, double actual, double timeFrame) {
		//Actual PID math
		double error = setpoint - actual;
		integral += error * timeFrame;
		double deriv = (error - lastError) / timeFrame;
		lastError = error;
		return error * pFactor + integral * iFactor + deriv * dFactor + fFactor * setpoint;
    }
}