package frc.robot.util;
import frc.robot.util.*;

public class RecursiveMotionProfile {

    //Made to accommodate the difficulty of pre-calculating motion profiles
    //for continuous tasks such as drivetrain, shooter velocity, teleop, etc.

    private long startTime;

    private double MAX_JERK;
    private double MAX_ACCEL;
    private double MAX_VEL;
    private double DECEL_THRESHOLD;

    private double currentAccel;
    private double currentVel;

    private double jerkNext;
    private double accelNext;
    private double velNext;

    private double error;

    private final int MAX_DELTA_TIME_MEMORY = 50;
    private int deltaTimeMemory;
    private double averageDeltaTime;
    private double deltaTime;
    private double lastTime;

    public RecursiveMotionProfile(double MAX_JERK, double MAX_ACCEL, double MAX_VEL) {
        startTime = System.currentTimeMillis();
        this.MAX_JERK = MAX_JERK;
        this.MAX_ACCEL = MAX_ACCEL;
        this.MAX_VEL = MAX_VEL;
        DECEL_THRESHOLD = Math.pow(MAX_ACCEL, 2)/(2*MAX_JERK);
    }

    public void updateParameters(double setpoint, double currentVel, double currentAccel) {
        this.currentVel = currentVel;
        this.currentAccel = currentAccel;
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

        if (Math.abs(error) < DECEL_THRESHOLD) {
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
    }

    private double averageAccel(double currentAccel) {
        return (currentAccel + accelNext)/2;
    }
}