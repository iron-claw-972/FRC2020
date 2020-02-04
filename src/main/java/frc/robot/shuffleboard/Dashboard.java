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
    private static NetworkTableEntry WOFEntry;
    private static ShuffleboardLayout NMFLayout;
    private static NetworkTableEntry NMFEntry1, NMFEntry2, NMFEntry3, NMFEntry4, NMFEntry5;

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
            
        WOFEntry = tab.add("WOF Color", 0)
            .withPosition(9,2)
            .withSize(2,2) 
            .getEntry();
        
        NMFLayout = tab.getLayout("Counting To Five Tutorial", BuiltInLayouts.kGrid)
            .withPosition(7, 4)
            .withSize(4, 2)
            .withProperties(Map.of("Number of Rows", 1, "Number of columns", 5));
        NMFEntry1 = NMFLayout.add()
    }

    public static void update() {
        voltageEntry.setDouble(edu.wpi.first.wpilibj.RobotController.getBatteryVoltage());
    }
}