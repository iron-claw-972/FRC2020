package frc.robot.controllers;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

public class NavX
{
    public AHRS ahrs;

    public NavX() {
        ahrs = new AHRS(SPI.Port.kMXP);
    }

    public double getHeading(){
        return ahrs.getAngle();
    }

    public double getBarometricPressure(){
        return ahrs.getBarometricPressure();
    }

    public double getTempC(){
        return ahrs.getTempC();
    }

    public void reset(){
        ahrs.reset();
    }
}