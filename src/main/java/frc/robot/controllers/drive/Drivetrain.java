package frc.robot.controllers.drive;

import edu.wpi.first.wpiutil.math.MathUtil;
import frc.robot.util.*;

public abstract class Drivetrain {
  protected double startPosLeft = 0, startPosRight = 0;
  protected double pastLeftDist = 0, pastRightDist = 0;
  protected long pastTime;

  // Curvature Drive Values
  private double m_quickStopThreshold = 0.2;
  private double m_quickStopAlpha = 0.1;
  private double m_quickStopAccumulator;

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

  private void chooseDriveMethod(double leftMotorOutput, double rightMotorOutput) {
    // Drive with pid only when output is > 0.5 and scale tankdrive output to make it bounded by maxDrivingSpeed
    // if (Math.abs(leftMotorOutput) < 0.5 || Math.abs(rightMotorOutput) < 0.5) {
    //   tankDrive(leftMotorOutput * Context.maxDrivingSpeed / Context.maxTheoreticalDrivingSpeed, rightMotorOutput * Context.maxDrivingSpeed / Context.maxTheoreticalDrivingSpeed);
    // } else {
    //   tankDrivePIDF(leftMotorOutput * Context.maxDrivingSpeed, rightMotorOutput * Context.maxDrivingSpeed);
    // }
    tankDrive(leftMotorOutput, rightMotorOutput);
  }

  public void arcadeDrive(double power, double turn) {
    power = MathUtil.clamp(power, -1.0, 1.0);
    turn = MathUtil.clamp(turn, -1.0, 1.0);

    double leftMotorOutput;
    double rightMotorOutput;

    double maxInput = Math.copySign(Math.max(Math.abs(power), Math.abs(turn)), power);

    if (power >= 0.0) {
      // First quadrant, else second quadrant
      if (turn >= 0.0) {
        leftMotorOutput = maxInput;
        rightMotorOutput = power - turn;
      } else {
        leftMotorOutput = power + turn;
        rightMotorOutput = maxInput;
      }
    } else {
      // Third quadrant, else fourth quadrant
      if (turn >= 0.0) {
        leftMotorOutput = power + turn;
        rightMotorOutput = maxInput;
      } else {
        leftMotorOutput = maxInput;
        rightMotorOutput = power - turn;
      }
    }

    chooseDriveMethod(leftMotorOutput, rightMotorOutput);

  }

  public void curvatureDrive(double power, double turn, boolean isQuickTurn) {
    power = MathUtil.clamp(power, -1.0, 1.0);
    turn = MathUtil.clamp(turn, -1.0, 1.0);

    double angularPower;
    boolean overPower;

    if (isQuickTurn) {
      if (Math.abs(power) < m_quickStopThreshold) {
        m_quickStopAccumulator = (1 - m_quickStopAlpha) * m_quickStopAccumulator
            + m_quickStopAlpha * MathUtil.clamp(turn, -1.0, 1.0) * 2;
      }
      overPower = true;
      angularPower = turn;
    } else {
      overPower = false;
      angularPower = Math.abs(power) * turn - m_quickStopAccumulator;

      if (m_quickStopAccumulator > 1) {
        m_quickStopAccumulator -= 1;
      } else if (m_quickStopAccumulator < -1) {
        m_quickStopAccumulator += 1;
      } else {
        m_quickStopAccumulator = 0.0;
      }
    }

    double leftMotorOutput = power + angularPower;
    double rightMotorOutput = power - angularPower;

    // If rotation is overpowered, reduce both outputs to within acceptable range
    if (overPower) {
      if (leftMotorOutput > 1.0) {
        rightMotorOutput -= leftMotorOutput - 1.0;
        leftMotorOutput = 1.0;
      } else if (rightMotorOutput > 1.0) {
        leftMotorOutput -= rightMotorOutput - 1.0;
        rightMotorOutput = 1.0;
      } else if (leftMotorOutput < -1.0) {
        rightMotorOutput -= leftMotorOutput + 1.0;
        leftMotorOutput = -1.0;
      } else if (rightMotorOutput < -1.0) {
        leftMotorOutput -= rightMotorOutput + 1.0;
        rightMotorOutput = -1.0;
      }
    }

    // Normalize the wheel speeds
    double maxMagnitude = Math.max(Math.abs(leftMotorOutput), Math.abs(rightMotorOutput));
    if (maxMagnitude > 1.0) {
      leftMotorOutput /= maxMagnitude;
      rightMotorOutput /= maxMagnitude;
    }

    chooseDriveMethod(leftMotorOutput, rightMotorOutput);
  }

  /**
   * Quick stop accum messes up unit test. Must be reset to give predictable results
   */
  public void resetQuickStopAccum() {
    m_quickStopAccumulator = 0;
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

  /**
   * Change negatives so that when fed positive left and right powers, the robot drives forward
   */
  public abstract void tankDrive(double leftPower, double rightPower);

  /**
   * @param amps for each motor
   * @param activationAmps the amperage that current limiting will be activated at
   */
  public abstract void setCurrentLimiting(double amps, double activationAmps, boolean enableCurrentLimiting);

  protected abstract double getLeftTicks();

  protected abstract double getRightTicks();

  public void resetEncoders() {
    startPosLeft = getLeftTicks();
    startPosRight = getRightTicks();
  }

  public abstract double getLeftDist();

  public abstract double getRightDist();

}