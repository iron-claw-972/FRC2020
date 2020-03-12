package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;

import org.apache.commons.io.filefilter.CanReadFileFilter;

import frc.robot.util.*;

public class Climber
{
    public CANSparkMax coilMotor1;
    public CANSparkMax coilMotor2;
    public CANSparkMax telescopeMotor;

    int currentLiftStep = 0;
    boolean PIDControllerOn = false;
    boolean PolyControllerOn = false;

    PID liftPID;
    long pastTime;
    double desiredPosition;
    double currentPosition;
    long currentTime;
    double deltaTime;

    public int topEncoderHeight = -55;
    public int bottomEncoderHeight = 2;
    double pidVal = 0;
    int topWinchHeight = 10;

    //Initializes Climber with Talon SRX motor, CANSparkMax, PID for the telescope, and initial time
    public Climber(CANSparkMax coilMotor1_, CANSparkMax coilMotor2_, CANSparkMax telescopeMotor_){
        coilMotor1 = coilMotor1_;
        coilMotor2 = coilMotor2_;
        telescopeMotor = telescopeMotor_;
        liftPID = new PID(.01, 0, 0.03);
        pastTime = System.currentTimeMillis(); 
        currentPosition = 0;
        
    }
    public double  getPolyMotorPower(int step){
        double output = Math.pow(1.1, step*.27)/100 + .57; //some testing can be done to find the ideal function
        output = output>1 ? 1:output;
        output = currentPosition <= topEncoderHeight ? 0:output;
        return output;
    }
    public void resetClimbEncoder() {
        telescopeMotor.getEncoder().setPosition(0);
        coilMotor1.getEncoder().setPosition(0);
        coilMotor2.getEncoder().setPosition(0);
    }
    
    //Moves the telescope
    public void telescopeMove(double speed) {
        telescopeMotor.set(speed);
    }

    //Spins the motor to coil the winch
    public void coil(double speed) {
        coilMotor1.set(-speed);
        coilMotor2.set(-speed);
    }

    public void up() {
        currentPosition = telescopeMotor.getEncoder().getPosition();
        System.out.println("Encoder: " + currentPosition);
        currentTime = System.currentTimeMillis();
        deltaTime = currentTime - pastTime;
        currentLiftStep++;
        desiredPosition = topEncoderHeight;
        pidVal = liftPID.update(desiredPosition, currentPosition, deltaTime);
        telescopeMove(-getPolyMotorPower(currentLiftStep));
        pastTime = currentTime;
    }

    public void down() {
        currentPosition = telescopeMotor.getEncoder().getPosition();
        currentTime = System.currentTimeMillis();
        deltaTime = currentTime - pastTime;
        currentLiftStep--;
        desiredPosition = bottomEncoderHeight;
        pidVal = liftPID.update(desiredPosition, currentPosition, deltaTime);
        System.out.println(pidVal);
        telescopeMove(pidVal);
        pastTime = currentTime;
    }

    //Loop to react to button press
    public void loop() {

    }

    public boolean isClimbDone() {
        int marginOfError = 5;
        //if the telescope is moving up then the action is done once it is within a range of the top encoder value
        if (desiredPosition == topEncoderHeight) {
            if ((currentPosition >= topEncoderHeight - marginOfError) && (currentPosition <= topEncoderHeight + marginOfError)) {
                return true;
            }
        }

        //if the telescope is moving down then the action is done once it is within a range of the bottom encoder value
        if (desiredPosition == bottomEncoderHeight) {
            if ((currentPosition >= bottomEncoderHeight - marginOfError) && (currentPosition <= bottomEncoderHeight + marginOfError)) {
                return true;
            }
        }
        return false;
    }

    public boolean isWinchDone()
    {
        int marginOfError = 5;
        double winchPosition1 = coilMotor1.getEncoder().getPosition();
        double winchPosition2 = coilMotor2.getEncoder().getPosition();
        if (((winchPosition1 >= topWinchHeight - marginOfError) && (winchPosition1 <= topWinchHeight + marginOfError)) || ((winchPosition2 >= topWinchHeight - marginOfError) && (winchPosition2 <= topWinchHeight + marginOfError))) {
            return true;
        }
        return false;
    }
}