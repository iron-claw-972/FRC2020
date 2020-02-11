package frc.robot.controllers;

import frc.robot.util.*;

public abstract class Drivetrain {
    public final double speedFac = -1, turnFac = -1;

    public double startPosLeft = 0, startPosRight = 0;
    public double pastLeftDist = 0, pastRightDist = 0;
    public long pastTime;

    private PID leftDrivePID, rightDrivePID;
    
    /**
     * Requires PIDs to be inputted so each drivetrain can have it's own values
     * @param leftDrivePID_
     * @param rightDrivePID_
     */
    public Drivetrain(PID leftDrivePID_, PID rightDrivePID_) {
        pastTime = System.currentTimeMillis();

        leftDrivePID = leftDrivePID_;
        rightDrivePID = rightDrivePID_;
    }

    public void arcadeDrive(double power, double turn) {
        power *= speedFac;
        turn *=  turnFac;
        double leftPower = turn - power;
        double rightPower = turn + power;

        tankDrivePID(leftPower, rightPower);
    }

    public void tankDrivePID(double leftGoalPower, double rightGoalPower) {
        double deltaTime = (double)(System.currentTimeMillis() - pastTime);

        double leftDistTraveled = getLeftDist() - pastLeftDist;
        double leftVelocity = leftDistTraveled/deltaTime;
        double leftPower = leftDrivePID.update(leftGoalPower, leftVelocity, deltaTime);

        double rightDistTraveled = getRightDist() - pastRightDist;
        double rightVelocity = rightDistTraveled/deltaTime;
        double rightPower = rightDrivePID.update(rightGoalPower, rightVelocity, deltaTime);

        // System.out.println("Left Power: " + leftPower + " ; Right Power: " + rightPower);

        tankDrive(leftPower, rightPower);

        pastTime = System.currentTimeMillis();
        pastLeftDist = getLeftDist();
        pastRightDist = getRightDist();
    }

    public abstract void tankDrive(double leftPower, double rightPower);

    protected abstract double getLeftTicks();

    protected abstract double getRightTicks();

    public void resetEncoders() {
        startPosLeft = getLeftTicks();
        startPosRight = getRightTicks();
    }

    public abstract double getLeftDist();

    public abstract double getRightDist();

}