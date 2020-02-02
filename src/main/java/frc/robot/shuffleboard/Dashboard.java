package frc.robot.shuffleboard;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.*;

import java.util.*;

public class Dashboard {
    private static ShuffleboardTab tab;

    private static NetworkTableEntry cameraEntry;
    private static NetworkTableEntry voltageEntry;
    private static NetworkTableEntry shooterSpeedEntry;
    private static NetworkTableEntry gearEntry;

    public static void init() {
        tab = Shuffleboard.getTab("Shuffleboard");

        cameraEntry = tab.add("Camera", 0)
            .withPosition(0,0)
            .withSize(7,5)
            .getEntry();

        voltageEntry = tab.add("Voltage", 0)
            .withWidget(BuiltInWidgets.kNumberBar)
            .withPosition(7,0)
            .withSize(2,1)
            .withProperties(Map.of("Min", 10, "Max", 14))
            .getEntry();

        shooterSpeedEntry = tab.add("Shooter Speed (RPM)", 0)
            .withWidget(BuiltInWidgets.kGraph)
            .withPosition(7,1)
            .withSize(2,2)
            .withProperties(Map.of("Min", 0, "Max", 600,"Visible Time", 10))
            .getEntry();

        gearEntry = tab.add("Gear", false)
            .withWidget(BuiltInWidgets.kToggleSwitch)
            .withPosition(9,0)
            .withSize(2,1) 
            .getEntry();
    }

    public static void update() {
        voltageEntry.setDouble(edu.wpi.first.wpilibj.RobotController.getBatteryVoltage());
    }
}