package frc.robot.controllers;

import frc.robot.util.*;

public abstract class Drivetrain {
    public final double speedFac = -1, turnFac = -1;

    public double startPosLeft = 0, startPosRight = 0;
    public double pastLeftDist = 0, pastRightDist = 0;
    public long pastTime;

    private PIDF leftDrivePIDF, rightDrivePIDF;
    
    /**
     * Requires PIDs to be inputted so each drivetrain can have it's own values
     * @param leftDrivePIDF_
     * @param rightDrivePIDF_
     */
    public Drivetrain(PIDF leftDrivePIDF_, PIDF rightDrivePIDF_) {
        pastTime = System.currentTimeMillis();

        leftDrivePIDF = leftDrivePIDF_;
        rightDrivePIDF = rightDrivePIDF_;
    }

    public void arcadeDrive(double power, double turn) {
        power *= speedFac;
        turn *=  turnFac;
        double leftPower = power - turn;
        double rightPower = power + turn;

        tankDrivePID(leftPower, rightPower);
    }

    public void tankDrivePID(double leftGoalPower, double rightGoalPower) {
        double deltaTime = (double)(System.currentTimeMillis() - pastTime);

        double leftVelocity = getLeftVel();
        double leftPower = leftDrivePIDF.update(leftGoalPower, leftVelocity, deltaTime);

        double rightVelocity = getRightVel();
        double rightPower = rightDrivePIDF.update(rightGoalPower, rightVelocity, deltaTime);

        // System.out.printf("LeftDesired: %f | LeftCurrent: %f | LeftPow: %f\n", leftGoalPower, leftVelocity, leftPower);

        if (leftGoalPower == 0) {
            tankDrive(0, rightPower);
        } else {
            tankDrive(leftPower, rightPower);
        }
        if (rightGoalPower == 0) {
            tankDrive(leftPower, 0);
        } else {
            tankDrive(leftPower, rightPower);
        }

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

    public abstract double getLeftVel();

    public abstract double getRightVel();

    public void printWheelDistances() {
        System.out.printf("LeftDist: %f | RightDist: %f\n", getLeftDist(), getRightDist());
    }

    public void printWheelVelocities() {
        System.out.printf("LeftVel: %f | RightVel: %f\n", getLeftVel(), getRightVel());
    }

}