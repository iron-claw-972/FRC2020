package frc.robot.controllers;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.*;
import edu.wpi.first.wpilibj.*;

import java.util.*;

public class Dashboard {
    private static ShuffleboardTab tab;

    private static ShuffleboardLayout drivetrainLayout;

    private static NetworkTableEntry leftEncoderEntry;
    private static NetworkTableEntry rightEncoderEntry;

    public static void init() {
        tab = Shuffleboard.getTab("Dashboard");

        drivetrainLayout = tab.getLayout("Drivetrain", BuiltInLayouts.kGrid)
            .withPosition(0,0)
            .withSize(3,3)
            .withProperties(Map.of("Number of rows", 2))
            .withProperties(Map.of("Number of columns", 2))
            .withProperties(Map.of("Label Position", "TOP"));

        leftEncoderEntry = drivetrainLayout.add("Left", 0)
            .withPosition(0,0)
            .withSize(2,1)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", -1, "max", 1))
            .getEntry();

        rightEncoderEntry = drivetrainLayout.add("Right", 0)
            .withPosition(1,0)
            .withSize(2,1)
            .withWidget(BuiltInWidgets.kNumberSlider)
            .withProperties(Map.of("min", -1, "max", 1))
            .getEntry();
    }

    public static void putJoystick(Joystick joy) {
        leftEncoderEntry.setDouble(joy.getRawAxis(0));
        rightEncoderEntry.setDouble(joy.getRawAxis(2));
    }
}