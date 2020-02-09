package frc.robot.controllers;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;

import frc.robot.util.*;

public class Intake
{
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
    /*private final ColorMatch m_colorMatcher = new ColorMatch();
    Color detectedColor = m_colorSensor.getColor();

    private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);*/


    public TalonSRX rollingIntake;
    public TalonSRX beltIntake;
    public PID beltSpeed = new PID(1, 0, 0);
    public DigitalInput limitSwitch = new DigitalInput(1);

    public Intake(TalonSRX rollingIntake_, TalonSRX beltIntake_)
    {
        this.rollingIntake = rollingIntake_;
        this.beltIntake = beltIntake_;
    }

    public void init()
    {
        /*m_colorMatcher.addColorMatch(kBlueTarget);
        m_colorMatcher.addColorMatch(kGreenTarget);
        m_colorMatcher.addColorMatch(kRedTarget);
        m_colorMatcher.addColorMatch(kYellowTarget);*/  
    }

    public void loop()
    {

        Color detectedColor = m_colorSensor.getColor();

        /**
         * The sensor returns a raw IR value of the infrared light detected.
         */
        double IR = m_colorSensor.getIR();

        /**
         * Open Smart Dashboard or Shuffleboard to see the color detected by the 
         * sensor.
         */
        SmartDashboard.putNumber("Red", detectedColor.red);
        SmartDashboard.putNumber("Green", detectedColor.green);
        SmartDashboard.putNumber("Blue", detectedColor.blue);
        SmartDashboard.putNumber("IR", IR);

        /**
         * In addition to RGB IR values, the color sensor can also return an 
         * infrared proximity value. The chip contains an IR led which will emit
         * IR pulses and measure the intensity of the return. When an object is 
         * close the value of the proximity will be large (max 2047 with default
         * settings) and will approach zero when the object is far away.
         * 
         * Proximity can be used to roughly approximate the distance of an object
         * or provide a threshold for when an object is close enough to provide
         * accurate color values.
         */
        int proximity = m_colorSensor.getProximity();

        SmartDashboard.putNumber("Proximity", proximity);
        /*Color detectedColor = m_colorSensor.getColor();

        
        String colorString;
        ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
        
        SmartDashboard.putNumber("Red", detectedColor.red);
        SmartDashboard.putNumber("Green", detectedColor.green);
        SmartDashboard.putNumber("Blue", detectedColor.blue);
        SmartDashboard.putNumber("Confidence", match.confidence);*/
        //SmartDashboard.putString("Detected Color", colorString);
    }

    /*public void roll()
    {
        rollingIntake.set(ControlMode.PercentOutput, joy.getRawAxis(0));
    }
    
    public void belt()
    {
        if(true)
        {
            beltIntake.set(ControlMode.PercentOutput, 0.5);
        }
    }*/
}