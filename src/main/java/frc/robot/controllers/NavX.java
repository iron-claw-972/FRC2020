package frc.robot.controllers;

import com.kauailabs.navx.frc.AHRS;

public class NavX
{
    public AHRS ahrs;

    public NavX(AHRS ahrs_) {
        ahrs = ahrs_;
    }

    public double getRawHeading(){
        return ahrs.getAngle();
    }

    public double getConstrainedHeading() {
        double heading = getRawHeading();

        // Constrains between -360 and 360
        heading %= 360; 
        
        // Constrains between -180 and 180
        if (heading >= 180) {
            heading -= 360;
        } else if (heading < -180) {
            heading += 360;
        }

        return heading;
    }

    public double getConstrainedHeading(double offset) {
        double heading = getRawHeading();

        // Constrains between -360 and 360
        heading %= 360; 

        // Add offset in case user wants
        heading += offset;
        
        // Constrains between -180 and 180
        if (heading >= 180) {
            heading -= 360;
        } else if (heading < -180) {
            heading += 360;
        }

        return heading;
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