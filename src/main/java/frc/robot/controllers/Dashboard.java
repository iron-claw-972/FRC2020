package frc.robot.controllers;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.*;
import edu.wpi.first.wpilibj.*;

import java.util.*;

public class Dashboard {
    private static ShuffleboardTab tab;

    private static NetworkTableEntry cameraEntry;
    private static NetworkTableEntry voltageEntry;
    private static NetworkTableEntry shooterSpeedEntry;

    public static void init() {
        tab = Shuffleboard.getTab("Dashboard");

        cameraEntry = tab.add("Camera", 0)
            .withPosition(0,0)
            .withSize(7,5)
            .getEntry();

        voltageEntry = tab.add("Voltage", 0)
            .withWidget(BuiltInWidgets.kNumberBar)
            .withPosition(7,0)
            .withSize(2,1)
            .withProperties(Map.of("Min", 11.5, "Max", 13.5))
            .getEntry();
    }

    public static void update() {
        voltageEntry.setDouble(edu.wpi.first.wpilibj.RobotController.getBatteryVoltage());
    }
}