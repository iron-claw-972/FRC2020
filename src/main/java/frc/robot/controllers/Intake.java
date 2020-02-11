package frc.robot.controllers;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.DriverStation;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorMatch;

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


    Joystick joy = new Joystick(0);
    TalonSRX rollingIntake = new TalonSRX(0);
    TalonSRX beltIntake = new TalonSRX(1);
    PID beltSpeed = new PID(1, 0, 0);
    DigitalInput limitSwitch = new DigitalInput(1);

    public Intake()
    {
        
    }

    public void init()
    {
        /*++++++.addColorMatch(kBlueTarget);
        m_colorMatcher.addColorMatch(kGreenTarget);
        m_colorMatcher.addColorMatch(kRedTarget);
        m_colorMatcher.addColorMatch(kYellowTarget);*/  
        
    }

    public void loop()
    {

        Color detectedColor = m_colorSensor.getColor();
        double[] hsv = RGBtoHSV(detectedColor.red, detectedColor.green, detectedColor.blue);

        /**
         * The sensor returns a raw IR value of the infrared light detected.
         */
        double IR = m_colorSensor.getIR();
        double hue = hsv[0];
        char color = "";
        
        if( (hue >= 0) && (hue<=60) )
        {
            color = 'R';
        }
        else if( (hue >= 61) && (hue<=120) )
        {
            color = 'Y';
        }
        else if( (hue >= 121) && (hue<=180) )
        {
            color = 'G';
        }
        else if( (hue >= 241) && (hue<=300) )
        {
            color = 'B';
        }
        else
        {
            color = 'N';
        }


        String gameData;
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        if(gameData.length() > 0)
        {
            if(color == gameData.charAt(0))
            {

            }
        }
        /**
         * Open Smart Dashboard or Shuffleboard to see the color detected by the 
         * sensor.
         */
        SmartDashboard.putNumber("Red", detectedColor.red);
        SmartDashboard.putNumber("Green", detectedColor.green);
        SmartDashboard.putNumber("Blue", detectedColor.blue);
        SmartDashboard.putNumber("IR", IR);
        SmartDashboard.putString("Color", String(color))

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
    public static double[] RGBtoHSV(double r, double g, double b){

        double h, s, v;
    
        double min, max, delta;
    
        min = Math.min(Math.min(r, g), b);
        max = Math.max(Math.max(r, g), b);
    
        // V
        v = max;
    
         delta = max - min;
    
        // S
         if( max != 0 )
            s = delta / max;
         else {
            s = 0;
            h = -1;
            return new double[]{h,s,v};
         }
    
        // H
         if( r == max )
            h = ( g - b ) / delta; // between yellow & magenta
         else if( g == max )
            h = 2 + ( b - r ) / delta; // between cyan & yellow
         else
            h = 4 + ( r - g ) / delta; // between magenta & cyan
    
         h *= 60;    // degrees
    
        if( h < 0 )
            h += 360;
    
        return new double[]{h,s,v};
    }
}