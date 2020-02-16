package frc.robot.controllers;

// import edu.wpi.first.wpilibj.Encoder;
import frc.robot.util.Context;
import frc.robot.util.PID;
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.*;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
 
import com.revrobotics.*;

public class NMFColorSensorController {
    private I2C.Port i2cPort = Context.NavXi2cPort;
    public String previousColor = "None";
    public String currentColor = "None";
    public int currentPosition = 0; //current sector sensing in 
    public boolean[] ballPositions = {false, false, false, false, false}; //list of each sector and if it has a ball or not
    public boolean currentSectorYellow = false;

    private final ColorSensorV3 m_colorSensor; //sometimes reads as error, still builds. Same issue for other colorsensor references

    /**
     * A Rev Color Match object is used to register and detect known colors. This can 
     * be calibrated ahead of time or during operation.
     * 
     * This object uses a simple euclidian distance to estimate the closest match
     * with given confidence range.
     */

    public NMFColorSensorController(){
      m_colorSensor = new ColorSensorV3(i2cPort);
    }

    public boolean isYellow(double r, double g, double b){
      if (r<.4 && b<.16) {
        return true;
      } else {
        return false;
      }
    }

    public boolean isBlue(double r, double g, double b){
      if (b>.3) {
        return true;
      } else {
        return false;
      }
    }

    public void loop(){
      Color detectedColor = m_colorSensor.getColor();
      //System.out.println(detectedColor.red +" "+ detectedColor.green + " "+ detectedColor.blue);
      if (isYellow(detectedColor.red, detectedColor.green, detectedColor.blue)){ //detect color
        currentColor = "yellow";
      }
      else{
        if (isBlue(detectedColor.red, detectedColor.green, detectedColor.blue)){
            currentColor = "blue";
        }
        else{
            currentColor = "none";
        }
        
      }
      if (currentColor!=previousColor){ //update when color changes
        if (currentPosition==5){ currentPosition=0;}
        switch (currentColor){
          case ("blue"): {
            if (currentPosition<5){currentPosition++;}
            else{currentPosition=0;}
            currentSectorYellow = false;
            break;
            
          }
          case ("yellow"): {
            //System.out.println(currentColor);
            if (currentPosition==5){ currentPosition=0;}
            ballPositions[currentPosition] = true;
            currentSectorYellow = true;
            break;
            
          }
          case("none"): {
            if (currentPosition==5){ currentPosition=0;}
            if (!currentSectorYellow){
              ballPositions[currentPosition] = false;
            }
            break;
            
          }
      }
        for(boolean bool : ballPositions) {
          System.out.print(bool + " ");
        }
        System.out.println();
      }
      
      previousColor = currentColor;
    }

    public boolean[] getBallPositions(){
        return ballPositions;
    }
    public int getBallCount(){
      return ballPositions.length;
    }
}
