package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import frc.robot.util.*;

public class ClimberTestValue
{
    public CANSparkMax telescopeMotor;
    double power;

    //Initializes Climber with Talon SRX motor, CANSparkMax, PID for the telescope, and initial time
    public ClimberTestValue(CANSparkMax telescopeMotor_){
        telescopeMotor = telescopeMotor_;
        power = 0;
    }

    public void resetClimbEncoder() {
        telescopeMotor.getEncoder().setPosition(0);
    }
    
    //Moves the telescope
    public void telescopeMove(double loopValue) {
        telescopeMotor.set(-loopValue);
    }

    //Loop to react to button press
    public void loop() {
        //Finds current encoder value of the wheel, the current time and the change in time since the last run
        double currentPosition = telescopeMotor.getEncoder().getPosition();
        //depending on button press sets desired position and updates the PID for the power
        if (Context.robotController.driverJoystick.getTestValues())
        {
            if (currentPosition < 40)
            {
                if (((currentPosition - 2) <= currentPosition) && ((currentPosition + 2) >= currentPosition))
                {
                    power += 0.001;
                    System.out.println("Power: " + power + "    Encoder: " + currentPosition);
                }
                telescopeMove(power);
            } 
        }
        else
        {
            telescopeMove(0);
        }
    }
}