package frc.robot.controllers.drive;

import frc.robot.util.*;

public abstract class Drivetrain {
    public final double speedFac = -1, turnFac = -1;

    public double startPosLeft = 0, startPosRight = 0;
    public double pastLeftDist = 0, pastRightDist = 0;
    public long pastTime;

    private PIDF leftDrivePIDF, rightDrivePIDF;
    
    /**
     * Requires PIDFs to be inputted so each drivetrain can have it's own values
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
        double leftPower = turn - power;
        double rightPower = turn + power;

        tankDrivePIDF(leftPower, rightPower);
    }

    public void tankDrivePIDF(double leftGoalPower, double rightGoalPower) {
        double deltaTime = (double)(System.currentTimeMillis() - pastTime);

        double leftDistTraveled = getLeftDist() - pastLeftDist;
        double leftVelocity = leftDistTraveled/deltaTime;
        double leftPower = leftDrivePIDF.update(leftGoalPower, leftVelocity, deltaTime);

        double rightDistTraveled = getRightDist() - pastRightDist;
        double rightVelocity = rightDistTraveled/deltaTime;
        double rightPower = rightDrivePIDF.update(rightGoalPower, rightVelocity, deltaTime);

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