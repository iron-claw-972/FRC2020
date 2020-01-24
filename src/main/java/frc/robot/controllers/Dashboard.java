package frc.robot.controllers;

import edu.wpi.first.wpilibj.smartdashboard.*;

public class Dashboard {
    public Dashboard() {
        
    }

    public static void putJoystick(double value) {
        SmartDashboard.putNumber("Joystick", value);
    }
}