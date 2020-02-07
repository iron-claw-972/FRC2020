package frc.robot.shuffleboard;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.*;
import edu.wpi.cscore.UsbCamera;

import java.util.*;

public class Dashboard {
    private static ShuffleboardTab tab;

    private static NetworkTableEntry voltageEntry;
    private static NetworkTableEntry shooterSpeedEntry;
    private static NetworkTableEntry WOFEntry;
    private static ShuffleboardLayout NMFLayout;
    private static NetworkTableEntry NMFEntry1, NMFEntry2, NMFEntry3, NMFEntry4, NMFEntry5;

    public static void init(UsbCamera camera) {
        tab = Shuffleboard.getTab("Shuffleboard");

        tab.add("Camera", camera)
            .withWidget(BuiltInWidgets.kCameraStream)
            .withPosition(0,0)
            .withProperties(Map.of("Show controls", false))
            .withSize(7,5);

        voltageEntry = tab.add("Voltage", 0)
            .withWidget(BuiltInWidgets.kNumberBar)
            .withPosition(7,0)
            .withSize(2,1)
            .withProperties(Map.of("Min", 10, "Max", 14))
            .getEntry();

        WOFEntry = tab.add("WOF Color", 0)
            .withPosition(7,1)
            .withSize(2,2) 
            .getEntry();

        shooterSpeedEntry = tab.add("Shooter Speed (RPM)", 0)
            .withWidget(BuiltInWidgets.kGraph)
            .withPosition(9,0)
            .withSize(2,3)
            .withProperties(Map.of("Min", 0, "Max", 600,"Visible Time", 10))
            .getEntry();
        
        NMFLayout = tab.getLayout("Count To Five Tutorial", BuiltInLayouts.kGrid)
            .withPosition(7, 3)
            .withSize(4, 2)
            .withProperties(Map.of("Number of Rows", 1, "Number of columns", 5, "Label Position", "TOP"));

        NMFEntry1 = NMFLayout.add("1", false)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(0, 0)
            .withSize(1, 1)
            .withProperties(Map.of("Color when true", "yellow", "Color when false", "#DDDDDD"))
            .getEntry();
        NMFEntry2 = NMFLayout.add("2", false)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(1, 0)
            .withSize(1, 1)
            .withProperties(Map.of("Color when true", "yellow", "Color when false", "#DDDDDD"))
            .getEntry();
        NMFEntry3 = NMFLayout.add("3", false)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(2, 0)
            .withSize(1, 1)
            .withProperties(Map.of("Color when true", "yellow", "Color when false", "#DDDDDD"))
            .getEntry();
        NMFEntry4 = NMFLayout.add("4", false)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(3, 0)
            .withSize(1, 1)
            .withProperties(Map.of("Color when true", "yellow", "Color when false", "#DDDDDD"))
            .getEntry();
        NMFEntry5 = NMFLayout.add("5", false)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(4, 0)
            .withSize(1, 1)
            .withProperties(Map.of("Color when true", "yellow", "Color when false", "#DDDDDD"))
            .getEntry(); 
    }

    public static void update() {
        voltageEntry.setDouble(edu.wpi.first.wpilibj.RobotController.getBatteryVoltage());
    }
}