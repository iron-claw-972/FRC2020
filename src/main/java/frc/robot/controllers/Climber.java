package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.revrobotics.CANSparkMax;

import org.apache.commons.io.filefilter.CanReadFileFilter;

import frc.robot.util.*;

public class Climber
{
    public CANSparkMax coilMotor1;
    public CANSparkMax coilMotor2;
    public TalonSRX telescopeEncoderMotor;
    public CANSparkMax telescopeMotor;

    int currentLiftStep = 0;
    boolean PIDControllerOn = false;
    boolean PolyControllerOn = false;

    PID liftPID;
    long pastTime;
    double desiredPosition;
    double currentPosition;

    int topEncoderHeight = 14490;
    int bottomEncoderHeight = 50;
    double pidVal;

    //Initializes Climber with Talon SRX motor, CANSparkMax, PID for the telescope, and initial time
    public Climber(CANSparkMax coilMotor1_, CANSparkMax coilMotor2_, TalonSRX telescopeEncoderMotor_, CANSparkMax telescopeMotor_){
        coilMotor1 = coilMotor1_;
        coilMotor2 = coilMotor2_;
        telescopeMotor = telescopeMotor_;
        telescopeEncoderMotor = telescopeEncoderMotor_;
        telescopeEncoderMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        liftPID = new PID(.0001, 0, 0.0003);
        pastTime = System.currentTimeMillis(); 
    }
    public double  getPolyMotorPower(int step){
        double output = Math.pow(1.1, step*.27)/100 + .57; //some testing can be done to find the ideal function
        output = output>1 ? 1:output;
        output = currentPosition > topEncoderHeight ? 0:output;
        return output;
    }
    public void resetClimbEncoder() {
        telescopeEncoderMotor.setSelectedSensorPosition(0);
    }
    
    //Moves the telescope
    public void telescopeMove(double PIDVal) {
        telescopeMotor.set(PIDVal);
    }

    //Spins the motor to coil the winch
    public void coil(){
        coilMotor1.set(Context.coilSpeed);
        coilMotor2.set(Context.coilSpeed);
    }

    //Spins the motor to uncoil the winch
    public void uncoil(){
        coilMotor1.set(Context.uncoilSpeed);
        coilMotor2.set(Context.uncoilSpeed);
    }

    public void up() {
        currentPosition = telescopeEncoderMotor.getSelectedSensorPosition();
        long currentTime = System.currentTimeMillis();
        double deltaTime = currentTime - pastTime;
        currentLiftStep++;
        desiredPosition = topEncoderHeight;
        pidVal = liftPID.update(desiredPosition, currentPosition, deltaTime);
        telescopeMove(-getPolyMotorPower(currentLiftStep));
        pastTime = currentTime;
    }

    public void down() {
        currentPosition = telescopeEncoderMotor.getSelectedSensorPosition();
        long currentTime = System.currentTimeMillis();
        double deltaTime = currentTime - pastTime;
        currentLiftStep--;
        desiredPosition = bottomEncoderHeight;
        pidVal = liftPID.update(desiredPosition, currentPosition, deltaTime);
        telescopeMove(-pidVal);
        pastTime = currentTime;
    }

    public void idle() {
        telescopeMove(0);
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
}