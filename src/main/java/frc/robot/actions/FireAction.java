package frc.robot.actions;

import frc.robot.controllers.*;
import frc.robot.util.*;

public class FireAction extends Action {

    private final double NMF_ERROR_THRESHOLD = 0.1;
    private final double SHOOTER_ERROR_THRESHOLD = 0.1;
    private final double FIRING_TIME_OUT = 1.0;

    public enum Position { TRENCH, CLOSE_INIT_LINE, VARIABLE }
    public enum Stage { CHECKING, IDLE, ACCELERATING, FIRING, DECELERATING, COMPLETE, TIME_OUT}

    Stage shootingStage;
    double timeOutCounter;

    ShooterController shooterController;
    NMFColorSensorController NMFColorSensor;
    NMFController NMFController;

    DistanceSensor distanceSensor;

    boolean[] ballPositions;
    int balls;
    int lastBalls;

    double lastFiredTime;

    boolean variableDistance;
    double firingDistance;

    double firingSpeed;
    private final double CALIBRATED_INIT_SPEED = 0;
    private final double CALIBRATED_TRENCH_SPEED = 0; //calibrate

    public FireAction(Position _position) {
        switch(_position) {
            case TRENCH:
                variableDistance = false;
                firingSpeed = CALIBRATED_TRENCH_SPEED;
                break;
            case CLOSE_INIT_LINE:
                variableDistance = false;
                firingSpeed = CALIBRATED_INIT_SPEED;
                break;
            case VARIABLE:
                variableDistance = true;
                firingSpeed = 5;
                break;
        }
    }

    public void init() {

        shootingStage = Stage.CHECKING;
        timeOutCounter = 0.0;

        shooterController = Context.robotController.shooterController;
        NMFColorSensor = Context.robotController.nmfColorSensorController;
        NMFController = Context.robotController.nmfController;

        distanceSensor = Context.robotController.distanceSensor;
    }

    public void loop() {

        if(variableDistance) {
            firingDistance = 100 * distanceSensor.getDistance(); //Conversion from cm to meters
        }

        ballPositions = NMFColorSensor.getBallPositions();
        lastBalls = balls;
        balls = 0;

        for(int i = 0; i < ballPositions.length; i++) {
            if(ballPositions[i]) {
                balls++;
            }
        }

        switch(shootingStage) {
            case CHECKING:
                //Checks if the NMF is empty.

                if(balls == 0) {
                    shootingStage = Stage.COMPLETE;
                } else {
                    shootingStage = Stage.ACCELERATING;
                }
                break;
            case ACCELERATING:
                //Gets shooter and NMF up to speed before starting to eject balls.

                shooterController.setDesiredVelocity(firingSpeed);
                NMFController.spinNMFShooting();

                //TODO: Make alignment a condition

                if(acceptNMFError() && acceptShooterError()) {
                    shootingStage = Stage.FIRING;
                    lastFiredTime = Context.getRelativeTimeSeconds(startTime);
                }

                break;
            case FIRING:
                //Begin to eject balls. Checks if NMF empty or if the time for a ball fired is too long.

                NMFController.spinOmni();

                if(balls == 0) {
                    shootingStage = Stage.DECELERATING;
                } else if(lastBalls < balls) {
                    lastFiredTime = Context.getRelativeTimeSeconds(startTime);
                } else if((Context.getRelativeTimeSeconds(startTime) - lastFiredTime) >= FIRING_TIME_OUT) {
                    shootingStage = Stage.TIME_OUT;
                }
                break;
            case DECELERATING:

                shooterController.setDesiredVelocity(0);
                NMFController.spinNMFIdle();
                shootingStage = Stage.COMPLETE;
                break;
            case COMPLETE:
                //NMF cleared out. Successfully fired all balls.

                markComplete();
                break;
            case TIME_OUT:
                //Too much time spent between balls fired, implying a jam. Ends the action.

                shootingStage = Stage.DECELERATING;
                break;
        }
    }

    private boolean acceptNMFError() {
        return (Math.abs(NMFController.getNMFError()) < NMF_ERROR_THRESHOLD);
    }
    
    private boolean acceptShooterError() {
        return (Math.abs(shooterController.getError()) < SHOOTER_ERROR_THRESHOLD);
    }
}