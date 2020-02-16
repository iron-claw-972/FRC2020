package frc.robot.util;

import frc.robot.util.JRAD;
import frc.robot.controllers.*;

public class JRADParameterTest {

    private ShooterController shooterController;

    private double startTime;

    private final double FIRING_THRESHOLD = 0.2;
    private final double RESET_VEL_THRESHOLD = 1;
    private final int RESET_SAMPLES_THRESHOLD = 25;
    private final int MAX_STABLE_SAMPLES = 50;

    private double currentVelocity;
    private double stableVelocity;

    private int stableSamples;
    private int resetSamples;
    
    private boolean firingBall; //Is the shooter firing and/or stabilizing?
    private long startFiringTime; //Time ball enters shooter, detect by velocity drop

    private double dropTime; //Time it takes for velocity to reach lowest point
    private double lowestVelocity; //Lowest velocity reached by motor while firing

    private double ballFireTimeSum;
    private double ballFireVelSum;
    private double ballFireSamples;
    private double ballFireTime; //Time ball is fired at relative to startFiringTime
    private double ballFireVelocity; //Velocity at which ball fires, corresponds to flywheel velocity
                                     //Objective is to minimize error relative to setpoint

    private double totalFiringTime; //Time from ball firing to RPM return to desired
    private double riseTime; //Time it takes for RPM to rise back to setpoint
                             //Objective with JRAD is to minimize this
    private double reloadTime; //Time from ball fired to reset

    private double loadRatio; //Predicted Offset gain in JRAD Controller to account for lost RPM
    private double error; //Lowest vel desired vel difference

    private boolean tested;

    public JRADParameterTest(ShooterController shooterController) {
        startTime = Context.getRelativeTime(0);
        this.shooterController = shooterController;
    }

    public void JRADStatTest(boolean reset, boolean ballExiting) {
        currentVelocity = shooterController.flywheelVelocity()/2;
        if(reset) {
            resetTestingVariables();
        } else if(!firingBall && !checkFiring()) {
            updateStableVelocity();
            if(tested) {
                printStatements();
            }
        } else {
            if(!firingBall) {
                startFiringTime = System.currentTimeMillis();
                firingBall = true;
                resetTestingVariables();
            }

            updateLowestVelocity();
            updateBallFireVelocity(ballExiting);
            checkReset();

            if(!firingBall) {
                totalFiringTime = dropTime + riseTime;
                reloadTime = totalFiringTime - ballFireTime;
                //loadRatio = shooterController.getDesiredVelocity()/ballFireVelocity;
                loadRatio = shooterController.getDesiredVelocity()/lowestVelocity;
                error = shooterController.getDesiredVelocity() - lowestVelocity;
                tested = true;
            }
        }
        
    }

    private void updateStableVelocity() {

        if(stableSamples < MAX_STABLE_SAMPLES) {
            stableSamples++;
        }

        stableVelocity = (stableVelocity * (stableSamples - 1) + currentVelocity)/stableSamples;
    }

    private boolean checkFiring() {
        return ((Math.abs(currentVelocity) - Math.abs(stableVelocity)) < -FIRING_THRESHOLD) ? true : false;
    }

    private void updateLowestVelocity() {
        if(Math.abs(currentVelocity) < Math.abs(lowestVelocity)) {
            lowestVelocity = currentVelocity;
            dropTime = Context.getRelativeTime(startFiringTime);
        }
    }

    private void updateBallFireVelocity(boolean ballExiting) {
        if(ballExiting) {
            ballFireSamples++;
            ballFireTimeSum += Context.getRelativeTime(startFiringTime);
            ballFireVelSum += shooterController.flywheelVelocity()/2;
            ballFireVelocity = ballFireVelSum/ballFireSamples;
            ballFireTime = ballFireTimeSum/ballFireSamples;
        }
    }

    private boolean checkReset() {
        if (Math.abs(currentVelocity - stableVelocity) <= RESET_VEL_THRESHOLD) {
            resetSamples++;
            if(resetSamples >= RESET_SAMPLES_THRESHOLD) {
                firingBall = false;
                riseTime = Context.getRelativeTime(startFiringTime) - dropTime;
                return true;
            }
            return false;
        } else {
            resetSamples = 0;
            return false;
        }
    }

    private void resetTestingVariables() {
        lowestVelocity = stableVelocity;
        stableSamples = 1;
        resetSamples = 0;
        totalFiringTime = 0;
        reloadTime = 0;
        ballFireTime = 0;
        ballFireVelSum = 0;
        ballFireVelocity = 0;
        ballFireTimeSum = 0;
        ballFireSamples = 0;
        dropTime = 0;
        riseTime = 0;
    }

    private void printStatements() {
        System.out.println("TOTAL TIME: " + totalFiringTime + " RISE TIME: " + riseTime);
        //System.out.println("DESIRED: " + shooterController.getDesiredVelocity() + " ACTUAL: " + ballFireVelocity + " ERROR: " + (shooterController.getDesiredVelocity() - ballFireVelocity));
        System.out.println("LOAD RATIO: " + loadRatio + " ERROR: " + error);
        System.out.println("STABLE VELOCITY: " + stableVelocity + " LOWEST VELOCITY: " + lowestVelocity);
    }

}