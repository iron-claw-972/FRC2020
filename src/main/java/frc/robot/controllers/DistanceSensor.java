package frc.robot.controllers;

import edu.wpi.first.wpilibj.PWM;

class DistanceSensor
{
    int ID;
    PWM pwmSensor;

    public DistanceSensor(int PWM_ID)
    {
        ID = PWM_ID;
        pwmSensor = new PWM(ID);
    }

    public double GetDistance()
    {
        return pwmSensor.getRaw(); // definitely in units
    }
}



// shaking my smh this class is insanely complicated :\