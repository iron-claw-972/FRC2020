package frc.robot.controllers;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.*;

import java.util.*;

public class Dashboard {
    private ShuffleboardTab tab;

    private ShuffleboardLayout drivetrainLayout;

    private static NetworkTableEntry leftEncoderEntry;

    public Dashboard() {
        tab = Shuffleboard.getTab("SmartDashboard");

        drivetrainLayout = tab.getLayout("Drivetrain", BuiltInLayouts.kGrid)
            .withPosition(0,0)
            .withSize(2,4)
            .withProperties(Map.of("Number of rows", 2, "Number of Columns", 2, "Label Position", "TOP"));
        leftEncoderEntry = drivetrainLayout.add("Left", 0)
            .withSize(1,1)
            .withWidget(BuiltInWidgets.kEnc)
            .withProperties(Map.of("min", -1, "max", 1))
            .getEntry();
    }

    public static void putJoystick(double value) {
        leftEncoderEntry.setDouble(value);
    }
}