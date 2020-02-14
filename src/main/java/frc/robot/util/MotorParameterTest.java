package frc.robot.util;

import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.*;
import java.util.ArrayList;

public class MotorParameterTest {

    private enum parameter {
        POS, VEL, ACCEL, JERK
    }

    public final double MAX_CURRENT = 0.8;

    private final int JERK_SAMPLES_THRESHOLD = 10;
    private final int ACCEL_SAMPLES_THRESHOLD = 20;
    private final int VEL_SAMPLES_THRESHOLD = 50;

    private final double JERK_NOISE_TOLERANCE = 10;
    private final double ACCEL_NOISE_TOLERANCE = 10;
    private final double VEL_NOISE_TOLERANCE = 10;

    private double maxJerk;
    private double maxAccel;
    private double maxVel;

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
    public boolean flood;

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

        if(!testStarted) {
            System.out.println("testing");
            startTime = System.currentTimeMillis();
            testStarted = true;
        }

        if(Context.getRelativeTime(startTime) > TIME_OUT) {
            motor.set(ControlMode.PercentOutput, 0.0);
            if(!flood) {
                System.out.println("TalonParameterTest timed out");
                System.out.println("JERK: " + maxJerk);
                System.out.println("ACCEL: " + maxAccel);
                System.out.println("VEL: " + maxVel);
                flood = true;
            }
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

                /*maxVel = checkMax(velSample, lastVel);
                maxAccel = checkMax(accelSample, lastAccel);
                maxJerk = checkMax(jerkSample, lastJerk);*/

                checkParameter(parameter.VEL);
                checkParameter(parameter.ACCEL);
                checkParameter(parameter.JERK);

                maxVel = checkMax(velSample, lastVel);
                maxAccel = checkMax(accelSample, lastAccel);
                maxJerk = checkMax(jerkSample, lastJerk);

                System.out.println("JERKerr: " + (jerkSample - lastJerk) + " ACCELerr: " + (accelSample - lastAccel) + " VELerr: " + (velSample - lastVel));
                lastVel = velSample;
                lastAccel = accelSample;
                lastJerk = jerkSample;
                System.out.println("JERKr: " + jerkSample + " ACCELr: " + accelSample + " VELr: " + velSample);
                System.out.println("JERKF " + jerkFound + " ACCELF " + accelFound + " VELF " + velFound);

           } else {
                motor.set(ControlMode.PercentOutput, 0);
                if(!flood) {
                    flood = true;
                    System.out.println(velSampleSum + " " + velSamples);
                    System.out.println(accelSampleSum + " " + accelSamples);
                    System.out.println(jerkSampleSum + " " + jerkSamples);
                    System.out.println("TalonParameterTest Done");
                    System.out.println("TRUE VEL: " + maxVel + " TRUE ACCEL: " + maxAccel + " TRUE JERK: " + maxJerk);
                    System.out.println("MAX VEL: " + velSampleSum/velSamples + " MAX ACCEL: " + accelSampleSum/accelSamples + " MAX JERK: " + jerkSampleSum/jerkSamples);
                }
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

        if(!testStarted) {
            System.out.println("testing");
            startTime = System.currentTimeMillis();
            testStarted = true;
        }

        //calculates the gains for a linear function converting a desired velocity to current
        if(!coeffFound && Context.getRelativeTime(startTime) > TIME_OUT) {
            motor.set(ControlMode.PercentOutput, 0.0);
            System.out.println("TalonVelToCurrentTest timed out");
        } else {
            if(!velToCurrentFound) {

                currentCurrent = Context.getRelativeTime(startTime) * currentIncreaseRate;

                if(currentCurrent <= MAX_CURRENT) {
                    System.out.println("Current: " + currentCurrent);
                    input = (orientation) ? currentCurrent : -currentCurrent;
                    motor.set(ControlMode.PercentOutput, input);
                    currentSpeed = radius * 2 * Math.PI * 10 * motor.getSelectedSensorVelocity()/CPR;
                    if(currentSpeed > 0) {
                        System.out.println("Adding (" + currentCurrent + ", " + currentSpeed + ")");
                        currentPoints.add(currentCurrent);
                        speedPoints.add(currentSpeed);
                    }
                } else {
                    velToCurrentFound = true;
                }
            } else {
                if(!coeffFound) {
                    
                    double[] coeff = linearRegressionVelCurrent(speedPoints, currentPoints);
                    frictionBound = coeff[0];
                    velToCurrentRatio = coeff[1];
                    coeffFound = true;
                    
                }
                input = 0;
                motor.set(ControlMode.PercentOutput, 0);
                System.out.println("TalonVelToCurrentTest Done");
                System.out.println("FRICTION BOUND (X-INTERCEPT): " + frictionBound + " VEL TO CURRENT RATIO: " + velToCurrentRatio);
            }
        }

    }

    
    public double[] linearRegressionVelCurrent(ArrayList<Double> inputPoints, ArrayList<Double> outputPoints) {

        double[][] A = new double[2][2];
        double[] b = new double[2];

        for(int i = 0; i < inputPoints.size(); i++) {
            A[0][0] += 1;
            A[1][0] += inputPoints.get(i);
            A[0][1] += inputPoints.get(i);
            A[1][1] += inputPoints.get(i) * inputPoints.get(i);
            b[0] += outputPoints.get(i);
            b[1] += outputPoints.get(i) * inputPoints.get(i);
        }
        System.out.println("init");
        System.out.println(A[0][0] + " " + A[0][1] + "|" +b[0]);
        System.out.println(A[1][0] + " " + A[1][1] + "|" +b[1]);

        //First row division
        A[0][1] = A[0][1]/A[0][0];
        b[0] = b[0]/A[0][0];
        A[0][0] = 1;
        System.out.println("div first");
        System.out.println(A[0][0] + " " + A[0][1] + "|" +b[0]);
        System.out.println(A[1][0] + " " + A[1][1] + "|" +b[1]);
        //Sub first row from second
        A[1][1] = A[1][1] - A[1][0] * A[0][1];
        b[1] = b[1] - A[1][0] * b[0];
        A[1][0] = 0;
        System.out.println("sub first");
        System.out.println(A[0][0] + " " + A[0][1] + "|" +b[0]);
        System.out.println(A[1][0] + " " + A[1][1] + "|" +b[1]);
        //Div. second
        b[1] = b[1]/A[1][1];
        A[1][1] = 1;
        System.out.println("div sec");
        System.out.println(A[0][0] + " " + A[0][1] + "|" +b[0]);
        System.out.println(A[1][0] + " " + A[1][1] + "|" +b[1]);
        //Sub. second from first
        b[0] = b[0] - A[0][1]*b[1];
        A[0][1] = 0;
        System.out.println("sub second");
        System.out.println(A[0][0] + " " + A[0][1] + "|" +b[0]);
        System.out.println(A[1][0] + " " + A[1][1] + "|" +b[1]);

        double[] solution = new double[] {b[0], b[1]};

        return solution;

    }
    
    

    private void checkParameter(parameter param) {
        if(param == parameter.VEL && !velFound && Context.getRelativeTime(startTime) > 6) {
            if(Math.abs(velSample - lastVel) > VEL_NOISE_TOLERANCE && !velFound) {
                //if the samples are changing too much, reset the sample collection process
                velSampleSum = 0;
                velSamples = 0;
                System.out.println(param + " drop");
            } else {
                velSampleSum += velSample;
                velSamples++;
                System.out.println(param + "add");
                if(velSampleSum >= VEL_SAMPLES_THRESHOLD) {
                    System.out.println(param + "done");
                    switch(param) {
                        case JERK:
                            jerkFound = true;
                        break;
                        case ACCEL:
                            accelFound = true;
                        break;
                        case VEL:
                            velFound = true;
                        break;
                    }
                    System.out.println(jerkFound + " " + accelFound + " " + velFound);
                }
            }
        } else if (param == parameter.ACCEL && !accelFound) {
            if(Math.abs(accelSample - lastAccel) > ACCEL_NOISE_TOLERANCE && !accelFound) {
                //if the samples are changing too much, reset the sample collection process
                accelSampleSum = 0;
                accelSamples = 0;
                System.out.println(param + " drop");
            } else {
                accelSampleSum += accelSample;
                accelSamples++;
                System.out.println(param + "add");
                if(accelSampleSum >= ACCEL_SAMPLES_THRESHOLD) {
                    System.out.println(param + "done");
                    switch(param) {
                        case JERK:
                            jerkFound = true;
                        break;
                        case ACCEL:
                            accelFound = true;
                        break;
                        case VEL:
                            velFound = true;
                        break;
                    }
                    System.out.println(jerkFound + " " + accelFound + " " + velFound);
                }
            }
        } else if (param == parameter.JERK && !jerkFound) {
            if(Math.abs(jerkSample - lastJerk) > JERK_NOISE_TOLERANCE && !jerkFound) {
                //if the samples are changing too much, reset the sample collection process
                jerkSampleSum = 0;
                jerkSamples = 0;
                System.out.println(param + " drop");
            } else {
                jerkSampleSum += jerkSample;
                jerkSamples++;
                System.out.println(param + "add");
                if(jerkSampleSum >= JERK_SAMPLES_THRESHOLD) {
                    System.out.println(param + "done");
                    switch(param) {
                        case JERK:
                            jerkFound = true;
                        break;
                        case ACCEL:
                            accelFound = true;
                        break;
                        case VEL:
                            velFound = true;
                        break;
                    }
                    System.out.println(jerkFound + " " + accelFound + " " + velFound);
                }
            }
        }
    }

    private void checkParameter(parameter param, double parameterSample, double lastParameter, double parameterSampleSum, int parameterSamples, boolean parameterFound, double PARAMETER_NOISE_TOLERANCE, double PARAMETER_SAMPLES_THRESHOLD) {
        if(Math.abs(parameterSample - lastParameter) > PARAMETER_NOISE_TOLERANCE && !parameterFound) {
            //if the samples are changing too much, reset the sample collection process
            parameterSampleSum = 0;
            parameterSamples = 0;
            System.out.println(param + " drop");
        } else {
            parameterSampleSum += parameterSample;
            parameterSamples++;
            System.out.println(param + "add");
            if(parameterSampleSum >= PARAMETER_SAMPLES_THRESHOLD) {
                System.out.println(param + "done");
                switch(param) {
                    case JERK:
                        jerkFound = true;
                    break;
                    case ACCEL:
                        accelFound = true;
                    break;
                    case VEL:
                        velFound = true;
                    break;
                }
                System.out.println(jerkFound + " " + accelFound + " " + velFound);
            }
        }
    }

    public double checkMax(double newSample, double oldSample) {
        if(Math.abs(newSample) > Math.abs(oldSample)) {
            System.out.println("update");
            return newSample;
        } else {
            return oldSample;
        }
    }

    private double takeDerivative(double valInit, double valFinal, double deltaTime) {
        return (valFinal - valInit)/deltaTime;
    }
}