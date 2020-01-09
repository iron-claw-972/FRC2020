package main.java.frc.robot.util;

public class PID {
    /**Variable Declarations*/
	//Proportional, Integral and Derivative Coefficients
    public double pFactor, iFactor, dFactor;
    //Integral Sum
    private double integral;
    //The previous error for derivative calculation
    private double lastError;

    /**Method Declarations*/
	//PID object
	//@param double pFactor (p_coeff), double iFactor (i_coeff), double dFactor (d_coeff)
	public PID(double pFactor, double iFactor, double dFactor) {
		this.pFactor = pFactor;
		this.iFactor = iFactor;
		this.dFactor = dFactor;
	}
	
	//Runs through one step of the PID
	//@param double setpoint_(goal value), double actual (current value), double timeFrame (time since last run)
	public double Update(double setpoint, double actual, double timeFrame) {
		//Actual PID math
		double present = setpoint - actual;
		integral += present * timeFrame;
		double deriv = (present - lastError) / timeFrame;
		lastError = present;
		return present * pFactor + integral * iFactor + deriv * dFactor;
    }
}