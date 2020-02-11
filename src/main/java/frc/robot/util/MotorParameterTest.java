package frc.robot.util;

import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.*;
import java.util.ArrayList;

public class MotorParameterTest {

    private enum parameter {
        POS, VEL, ACCEL, JERK
    }

    public final double MAX_CURRENT = 0.8;

    private final int JERK_SAMPLES_THRESHOLD = 1;
    private final int ACCEL_SAMPLES_THRESHOLD = 1;
    private final int VEL_SAMPLES_THRESHOLD = 1;

    private final double JERK_NOISE_TOLERANCE = 999;
    private final double ACCEL_NOISE_TOLERANCE = 999;
    private final double VEL_NOISE_TOLERANCE = 999;

    private double jerkSampleSum;
    private int jerkSamples;
    private double accelSampleSum;
    private int accelSamples;
    private double velSampleSum;
    private int velSamples;

    private boolean jerkFound;
    private boolean accelFound;
    private boolean velFound;

    private double jerkSample;
    private double lastJerk;

    private double accelSample;
    private double lastAccel;

    private double velSample;
    private double lastVel;

    private double deltaTime;
    private double lastTime;
    private double relativePoint;

    private boolean timeOut;
    private final double TIME_OUT = 10.0;
    private long startTime;

    public MotorParameterTest() {
        startTime = System.currentTimeMillis();
        currentPoints = new ArrayList<>();
        speedPoints = new ArrayList<>();
    }

    public void TalonParameterTest(BaseTalon motor, Boolean orientation, int CPR, double radius) {

        //motors is the array of motors being tested
        //orientation is direction of rotation; true for right, false for left
        //CPR is CPR of related motor encoders
        //radius is radius of wheels related to encoders
        if(Context.getRelativeTime(startTime) > TIME_OUT) {
            motor.set(ControlMode.PercentOutput, 0.0);
            System.out.println("TalonParameterTest timed out");
        } else {
            double speed = (orientation) ? MAX_CURRENT : -MAX_CURRENT;
            motor.set(ControlMode.PercentOutput, speed);

            deltaTime = Context.getRelativeTime(startTime) - lastTime;
            lastTime = Context.getRelativeTime(startTime);

            if((!jerkFound || !accelFound || !velFound) && !timeOut) {
                //While the maximum jerk, accel, and vel have not been found,
                //the test will keep running and checking for samples
                velSample = radius * 2 * Math.PI * 10 * motor.getSelectedSensorVelocity()/CPR;
                accelSample = takeDerivative(lastVel, velSample, deltaTime);
                jerkSample = takeDerivative(lastAccel, accelSample, deltaTime);

                checkParameter(parameter.VEL);
                checkParameter(parameter.ACCEL);
                checkParameter(parameter.JERK);

                lastVel = velSample;
                lastAccel = accelSample;
                lastJerk = jerkSample;
                System.out.println(" JERK " + jerkFound + " ACCEL " + accelFound + " VEL " + velFound);

           } else {
                motor.set(ControlMode.PercentOutput, 0);
                System.out.println("TalonParameterTest Done");
                System.out.println("MAX VEL: " + velSampleSum/velSamples + " MAX ACCEL: " + accelSampleSum/accelSamples + " MAX JERK: " + jerkSampleSum/jerkSamples);
            }
        }
    }

    private double currentCurrent;
    private double currentSpeed;
    private double currentIncreaseRate = 0.1;
    private ArrayList<Double> currentPoints;
    private ArrayList<Double> speedPoints;
    private boolean velToCurrentFound;
    private boolean coeffFound;
    private double frictionBound;
    private double velToCurrentRatio;
    public double input;
    private boolean testStarted;

    public void TalonVelToCurrentTest(BaseTalon motor, Boolean orientation, int CPR, double radius) {
        System.out.println("run" + currentCurrent + " " + Context.getRelativeTime(startTime));

        if(!testStarted) {
            System.out.println("testing");
            startTime = System.currentTimeMillis();
            testStarted = true;
        }

        //calculates the gains for a linear function converting a desired velocity to current
        if(Context.getRelativeTime(startTime) > TIME_OUT) {
            motor.set(ControlMode.PercentOutput, 0.0);
            System.out.println("TalonVelToCurrentTest timed out");
        } else {
            if(!velToCurrentFound) {

                currentCurrent = Context.getRelativeTime(startTime) * currentIncreaseRate;

                if(currentCurrent <= MAX_CURRENT) {
                    input = (orientation) ? currentCurrent : -currentCurrent;
                    motor.set(ControlMode.PercentOutput, input);
                    currentSpeed = radius * 2 * Math.PI * 10 * motor.getSelectedSensorPosition()/CPR;
                    if(currentSpeed > 0) {
                        currentPoints.add(currentCurrent);
                        speedPoints.add(currentSpeed);
                    }
                } else {
                    velToCurrentFound = true;
                }
            } else {
                if(!coeffFound) {
                    
                    double[] coeff = linearRegressionVelCurrent(currentPoints, speedPoints);
                    frictionBound = coeff[0];
                    velToCurrentRatio = coeff[1];
                    coeffFound = true;
                    
                }
                System.out.println("TalonVelToCurrentTest Done");
                System.out.println("FRICTION BOUND (X-INTERCEPT): " + frictionBound + " VEL TO CURRENT RATIO: " + velToCurrentRatio);
            }
        }

    }

    
    public double[] linearRegressionVelCurrent(ArrayList<Double> currentPoints, ArrayList<Double> speedPoints) {

        double[][] A = new double[2][2];
        double[] b = new double[2];

        for(int i = 0; i < currentPoints.size(); i++) {
            A[0][0] += 1;
            A[1][0] += currentPoints.get(i);
            A[0][1] += currentPoints.get(i);
            A[1][1] += currentPoints.get(i) * currentPoints.get(i);
            b[0] = speedPoints.get(i);
            b[1] = speedPoints.get(i) * currentPoints.get(i);
        }

        double[] solution = new double[2];

        return new double[]{0, 0};

    }
    
    

    private void checkParameter(parameter param) {
        if(param == parameter.VEL) {
            checkParameter(velSample, lastVel, velSampleSum, velSamples, velFound, VEL_NOISE_TOLERANCE, VEL_SAMPLES_THRESHOLD);
        } else if (param == parameter.ACCEL) {
            checkParameter(accelSample, lastAccel, accelSampleSum, accelSamples, accelFound, ACCEL_NOISE_TOLERANCE, ACCEL_SAMPLES_THRESHOLD);
        } else if (param == parameter.JERK) {
            checkParameter(jerkSample, lastJerk, jerkSampleSum, jerkSamples, jerkFound, JERK_NOISE_TOLERANCE, JERK_SAMPLES_THRESHOLD);
        }
    }

    private void checkParameter(double parameterSample, double lastParameter, double parameterSampleSum, int parameterSamples, boolean parameterFound, double PARAMETER_NOISE_TOLERANCE, double PARAMETER_SAMPLES_THRESHOLD) {
        if(Math.abs(parameterSample - lastParameter) > PARAMETER_NOISE_TOLERANCE && !parameterFound) {
            //if the samples are changing too much, reset the sample collection process
            parameterSampleSum = 0;
            parameterSamples = 0;
        } else {
            parameterSampleSum += parameterSample;
            parameterSamples++;
            if(parameterSampleSum >= PARAMETER_SAMPLES_THRESHOLD) {
                parameterFound = true;
            }
        }
    }

    private double takeDerivative(double valInit, double valFinal, double deltaTime) {
        return (valFinal - valInit)/deltaTime;
    }
}