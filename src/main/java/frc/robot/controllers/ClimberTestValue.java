package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.util.*;

public class ClimberTestValue
{
    public TalonSRX telescopeEncoderMotor;
    public CANSparkMax telescopeMotor;
    double power;

    //Initializes Climber with Talon SRX motor, CANSparkMax, PID for the telescope, and initial time
    public ClimberTestValue(TalonSRX telescopeEncoderMotor_, CANSparkMax telescopeMotor_){
        telescopeMotor = telescopeMotor_;
        telescopeEncoderMotor = telescopeEncoderMotor_;
        telescopeEncoderMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
        power = 0;
    }

    public void resetClimbEncoder() {
        telescopeEncoderMotor.setSelectedSensorPosition(0);
    }
    
    //Moves the telescope
    public void telescopeMove(double loopValue) {
        telescopeMotor.set(loopValue);
    }

    //Loop to react to button press
    public void loop() {
        //Finds current encoder value of the wheel, the current time and the change in time since the last run
        double currentPosition = telescopeEncoderMotor.getSelectedSensorPosition();
        System.out.println("Encoder Value: " + currentPosition);
        //depending on button press sets desired position and updates the PID for the power
        if (Context.robotController.driverJoystick.getTestValues())
        {
            if (currentPosition < 14000)
            {
                if (((currentPosition - 5) <= currentPosition) && ((currentPosition + 5) >= currentPosition))
                {
                    power += 0.01;
                    System.out.println("Power: " + power + "\t Encoder: " + currentPosition);
                }
                telescopeMove(power);
            } 
        }
    }
}