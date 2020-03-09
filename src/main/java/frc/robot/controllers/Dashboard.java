package frc.robot.controllers;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.*;
import edu.wpi.first.shuffleboard.plugin.base.data.fms.FmsInfo;
import edu.wpi.first.shuffleboard.plugin.base.widget.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.cscore.UsbCamera;
import frc.robot.controllers.drive.TalonFXDrivetrain.Gear;
import frc.robot.util.Context;

import edu.wpi.first.wpilibj.DriverStation;

import java.util.Map;

public class Dashboard {
    private static ShuffleboardTab tab;
    private static NetworkTableEntry fmsEntry, voltageEntry, currentEntry, timeEntry, shooterSpeedEntry, autoEntry;
    private static ShuffleboardLayout NMFLayout, WOFLayout, alignLayout;
    private static NetworkTableEntry NMFEntry1, NMFEntry2, NMFEntry3, NMFEntry4, NMFEntry5;
    private static NetworkTableEntry WOFEntryB, WOFEntryG, WOFEntryR, WOFEntryY;
    private static NetworkTableEntry alignFailEntry, aligningEntry, alignSucceedEntry;

    public static SendableChooser<Gear> gearChooser;
    public static SendableChooser<AutoOptions.Start> autoChooser;

    private static final boolean config = true;
    private static ShuffleboardTab configTab;

    public static void init(UsbCamera camera) {
        setChoosers();

        tab = Shuffleboard.getTab("Shuffleboard");

        tab.add("Camera", camera)
            .withWidget(BuiltInWidgets.kCameraStream)
            .withPosition(0,0)
            .withProperties(Map.of("Show controls", false, "Show crosshair", false))
            .withSize(7,5);

        // fmsEntry = tab.add("FMS", getFmsInfo())
        //     .withWidget("Basic FMS Info")
        //     .withPosition(0,0)
        //     .withSize(3,1)
        //     .withProperties(Map.of("Show controls", false, "Show crosshair", false))
        //     .getEntry();

        voltageEntry = tab.add("Voltage", 0)
            .withWidget(BuiltInWidgets.kNumberBar)
            .withPosition(7,1)
            .withSize(1,1)
            .withProperties(Map.of("Min", 11, "Max", 14))
            .getEntry();

        currentEntry = tab.add("Current", 0)
            .withWidget(BuiltInWidgets.kNumberBar)
            .withPosition(8,1)
            .withSize(1,1)
            .withProperties(Map.of("Min", 0, "Max", 100))
            .getEntry();

        WOFLayout = tab.getLayout("WOF Color", BuiltInLayouts.kGrid)
            .withPosition(7,2)
            .withSize(1,1) 
            .withProperties(Map.of("Number of Rows", 2, "Number of columns", 2, "Label Position", "HIDDEN"));;
        WOFEntryB = WOFLayout.add("B", false)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(0, 0)
            .withSize(1, 1)
            .withProperties(Map.of("Color when true", Context.WOFColors.get('B'), "Color when false", Context.WOFColors.get('N')))
            .getEntry();
        WOFEntryG = WOFLayout.add("G", false)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(1, 0)
            .withSize(1, 1)
            .withProperties(Map.of("Color when true", Context.WOFColors.get('G'), "Color when false", Context.WOFColors.get('N')))
            .getEntry();
        WOFEntryR = WOFLayout.add("R", false)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(0, 1)
            .withSize(1, 1)
            .withProperties(Map.of("Color when true", Context.WOFColors.get('R'), "Color when false", Context.WOFColors.get('N')))
            .getEntry();
        WOFEntryY = WOFLayout.add("Y", false)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(1, 1)
            .withSize(1, 1)
            .withProperties(Map.of("Color when true", Context.WOFColors.get('Y'), "Color when false", Context.WOFColors.get('N')))
            .getEntry();

        tab.add("Gear", gearChooser)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(8,2)
            .withSize(1,1); 

        timeEntry = tab.add("Time Left", "15 s")
            .withPosition(10,0)
            .withSize(1,1) 
            .getEntry();

        shooterSpeedEntry = tab.add("Shooter Speed (RPM)", 0)
            .withWidget(BuiltInWidgets.kGraph)
            .withPosition(9,2)
            .withSize(2,2)
            .withProperties(Map.of("Min", 0, "Max", 600,"Visible Time", 10))
            .getEntry();

        alignLayout = tab.getLayout("Alignment Status", BuiltInLayouts.kGrid)
            .withPosition(9,1)
            .withSize(2,1) 
            .withProperties(Map.of("Number of Rows", 1, "Number of columns", 3, "Label Position", "TOP"));;
        alignFailEntry = alignLayout.add("Failed", true)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(0, 0)
            .withSize(1, 1)
            .withProperties(Map.of("Color when true", "#FF0000", "Color when false", "#DDDDDD"))
            .getEntry();
        aligningEntry = alignLayout.add("Aligning", true)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(1, 0)
            .withSize(1, 1)
            .withProperties(Map.of("Color when true", "#FFFF00", "Color when false", "#DDDDDD"))
            .getEntry();
        alignSucceedEntry = alignLayout.add("Success", true)
            .withWidget(BuiltInWidgets.kBooleanBox)
            .withPosition(2, 0)
            .withSize(1, 1)
            .withProperties(Map.of("Color when true", "#00FF00", "Color when false", "#DDDDDD"))
            .getEntry();
        
        NMFLayout = tab.getLayout("Counting To Five Tutorial", BuiltInLayouts.kGrid)
            .withPosition(7, 4)
            .withSize(4, 1)
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

        tab.add("Auto Configs", autoChooser)
            .withWidget(BuiltInWidgets.kComboBoxChooser)
            .withPosition(7,3)
            .withSize(2,1);


        if(config) {
            configInit();
        }
    }

    public static void update() {
        voltageEntry.setDouble(edu.wpi.first.wpilibj.RobotController.getBatteryVoltage());
        currentEntry.setDouble(0.0);
        // fmsEntry.setValue(getFmsInfo());

        WOFEntryB.setBoolean(Context.WOFTargetColor == 'B');
        WOFEntryG.setBoolean(Context.WOFTargetColor == 'G');
        WOFEntryR.setBoolean(Context.WOFTargetColor == 'R');
        WOFEntryY.setBoolean(Context.WOFTargetColor == 'Y');

        //gearEntry.setValue(Context.robotController.drivetrain.gear);

        // boolean[] NMFArray = Context.robotController.ballPositions.getBallPositions();
        // NMFEntry1.setBoolean(NMFArray[0]);
        // NMFEntry2.setBoolean(NMFArray[1]);
        // NMFEntry3.setBoolean(NMFArray[2]);
        // NMFEntry4.setBoolean(NMFArray[3]);
        // NMFEntry5.setBoolean(NMFArray[4]);

        timeEntry.setString(Math.max(0, (int) Math.ceil(Timer.getMatchTime())) + " s");
    }

    private static void configInit() {
        configTab = Shuffleboard.getTab("Config");
    }

    private static void setChoosers() {
        gearChooser = new SendableChooser<Gear>();
        gearChooser.setDefaultOption("Lo", Gear.LOW);
        gearChooser.addOption("Hi", Gear.HIGH);

        autoChooser = new SendableChooser<AutoOptions.Start>();
        autoChooser.setDefaultOption("Left", AutoOptions.Start.LEFT);
        autoChooser.addOption("Middle", AutoOptions.Start.MIDDLE);
        autoChooser.addOption("Right", AutoOptions.Start.RIGHT);
    }

    private static FmsInfo getFmsInfo() {
        DriverStation ds = DriverStation.getInstance();

        return new FmsInfo(
            Map.of(
                "GameSpecificMessage", ds.getGameSpecificMessage(),
                "EventName", ds.getEventName(),
                "MatchNumber", ds.getMatchNumber(),
                "ReplayNumber", ds.getReplayNumber(),
                "MatchType", ds.getMatchType().ordinal(),
                "IsRedAlliance", ds.getAlliance() == DriverStation.Alliance.Red,
                "StationNumber", ds.getLocation(),
                "FMSControlData", 0
            )
        );
    }
}