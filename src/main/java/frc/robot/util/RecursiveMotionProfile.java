package frc.robot.util;
import frc.robot.util.*;

public class RecursiveMotionProfile {

    //Made to accommodate the difficulty of pre-calculating motion profiles
    //for continuous tasks such as drivetrain, shooter velocity, teleop functions in general, etc.

    private long startTime;

    private double MAX_JERK;
    private double MAX_ACCEL;
    private double MAX_VEL;

    private double snapBand;
    private double decelThreshold;

    private double currentAccel;
    private double currentVel;

    private double jerkNext;
    private double accelNext;
    private double velNext;

    private double desiredVel;
    private double error;

    private final int MAX_DELTA_TIME_MEMORY = 50;
    private int deltaTimeMemory;
    private double averageDeltaTime;
    private double deltaTime;
    private double lastTime;

    public RecursiveMotionProfile(double MAX_JERK, double MAX_ACCEL, double MAX_VEL, double snapBand) {

        startTime = System.currentTimeMillis();
        this.MAX_JERK = MAX_JERK;
        this.MAX_ACCEL = MAX_ACCEL;
        this.MAX_VEL = MAX_VEL;
        this.snapBand = snapBand;
    }

    public void updateParameters(double desiredVel, double currentVel, double currentAccel) {

        //Pass current motion states to object
        //Used to update the next state of each derivative 

        this.currentAccel = currentAccel;
        this.desiredVel = desiredVel;
        this.currentVel = currentVel;

        decelThreshold = Math.pow(currentAccel, 2)/(2*MAX_JERK);
        error = desiredVel - currentVel;
        updateDeltaTime();
        updateJerkNext();
        updateAccelNext();
        updateVelNext();
    }

    public double getJerkNext() {
        return jerkNext;
    }

    public double getAccelNext() {
        return accelNext;
    }

    public double getVelNext() {
        return velNext;
    }

    private void updateDeltaTime() {

        deltaTime = Context.getRelativeTimeSeconds(startTime) - lastTime;
        lastTime = Context.getRelativeTimeSeconds(startTime);

        averageDeltaTime = (averageDeltaTime * (deltaTimeMemory - 1) + deltaTime)/deltaTimeMemory;

        if(MAX_DELTA_TIME_MEMORY == 0 || deltaTimeMemory <= MAX_DELTA_TIME_MEMORY) {
            deltaTimeMemory++;
        }
    }

    private void updateJerkNext() {

        jerkNext = Math.signum(error) * MAX_JERK;

        if (Math.abs(error) < decelThreshold && Math.signum(error) == Math.signum(currentAccel)) {
            jerkNext = -jerkNext;
        }
    }

    private void updateAccelNext() {

        accelNext = currentAccel + jerkNext * averageDeltaTime;
        accelNext = AdditionalMath.Clamp(accelNext, -MAX_ACCEL, MAX_ACCEL);
    }

    private void updateVelNext() {

        velNext = currentVel + averageAccel(currentAccel) * averageDeltaTime;
        velNext = AdditionalMath.Clamp(velNext, -MAX_VEL, MAX_VEL);

        if(Math.abs(error) <= snapBand) {
            velNext = desiredVel;
        }
    }

    private double averageAccel(double currentAccel) {

        return (currentAccel + accelNext)/2;
    }
}