package frc.robot.controllers.drive;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.util.*;

public abstract class Drivetrain {
    private final double speedFac = -1, turnFac = -1;

    protected double startPosLeft = 0, startPosRight = 0;
    protected double pastLeftDist = 0, pastRightDist = 0;
    protected long pastTime;

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

    protected void curvatureDrive(DifferentialDrive differentialDrive, double power, double turn, boolean isQuickTurn) {
        differentialDrive.curvatureDrive(power, turn, isQuickTurn);
    }

    public void tankDrivePIDF(double leftGoalPower, double rightGoalPower) {
        double deltaTime = (double)(System.currentTimeMillis() - pastTime);

        double leftDistTraveled = getLeftDist() - pastLeftDist;
        double leftVelocity = leftDistTraveled/deltaTime;
        double leftPower = leftDrivePIDF.update(leftGoalPower, leftVelocity, deltaTime);

        double rightDistTraveled = getRightDist() - pastRightDist;
        double rightVelocity = rightDistTraveled/deltaTime;
        double rightPower = rightDrivePIDF.update(rightGoalPower, rightVelocity, deltaTime);

        // Allows robot to coast to a halt on teleop
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