package frc.robot.controllers;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.*;

public class Dashboard {
    private ShuffleboardTab tab = Shuffleboard.getTab("SmartDashboard");

    private final NetworkTableEntry[] entries = new NetworkTableEntry[] {
        NetworkTableEntry
    };

    public Dashboard() {
        
    }

    public static void putJoystick(double value) {
        
    }
}