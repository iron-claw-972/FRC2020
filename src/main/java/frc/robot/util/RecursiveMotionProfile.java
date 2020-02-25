package frc.robot.util;
import frc.robot.util.*;

public class RecursiveMotionProfile {

    //Made to accommodate the difficulty of pre-calculating motion profiles
    //for continuous tasks such as drivetrain, shooter velocity, teleop, etc.

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

    private double setpoint;
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

    public void updateParameters(double setpoint, double currentVel, double currentAccel) {
        this.setpoint = setpoint;
        this.currentVel = currentVel;
        this.currentAccel = currentAccel;
        decelThreshold = Math.pow(currentAccel, 2)/(2*MAX_JERK);
        error = setpoint - currentVel;
        updateDeltaTime();
        updateJerkNext();
        updateAccelNext();
        updateVelNext();
    }

    public double getVelNext() {
        return velNext;
    }

    public double getAccelNext() {
        return accelNext;
    }

    public double getJerkNext() {
        return jerkNext;
    }

    private void updateDeltaTime() {


        deltaTime = Context.getRelativeTime(startTime) - lastTime;
        lastTime = Context.getRelativeTime(startTime);

        averageDeltaTime = (averageDeltaTime * (deltaTimeMemory - 1) + deltaTime)/deltaTimeMemory;

        if(MAX_DELTA_TIME_MEMORY == 0 || deltaTimeMemory <= MAX_DELTA_TIME_MEMORY) {
            deltaTimeMemory++;
        }
    }

    private void updateJerkNext() {
        jerkNext = Math.signum(error) * MAX_JERK;

        if (Math.abs(error) < decelThreshold && Math.signum(error) == -Math.signum(currentAccel)) {
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

        if(Math.abs(error) < snapBand) {
            velNext = setpoint;
        }
    }

    private double averageAccel(double currentAccel) {
        return (currentAccel + accelNext)/2;
    }
}