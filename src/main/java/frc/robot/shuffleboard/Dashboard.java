package frc.robot.shuffleboard;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.cscore.UsbCamera;
import frc.robot.controllers.AutoOptions;
import frc.robot.controllers.TalonFXDrivetrain.Gear;
import frc.robot.util.Context;

import java.util.Map;

public class Dashboard {
    private static ShuffleboardTab tab;
    private static NetworkTableEntry voltageEntry, currentEntry, WOFEntry, timeEntry, shooterSpeedEntry;
    private static ShuffleboardLayout NMFLayout, autoLayout;
    private static NetworkTableEntry NMFEntry1, NMFEntry2, NMFEntry3, NMFEntry4, NMFEntry5;

    public static SendableChooser<Gear> gearChooser;
    public static SendableChooser<AutoOptions.Start> autoStartChooser;
    public static SendableChooser<AutoOptions.Port> autoPortChooser;
    public static SendableChooser<AutoOptions.PickUp> autoPickUpChooser;

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

        voltageEntry = tab.add("Voltage", 0)
            .withWidget(BuiltInWidgets.kNumberBar)
            .withPosition(7,0)
            .withSize(2,1)
            .withProperties(Map.of("Min", 10, "Max", 14))
            .getEntry();

        currentEntry = tab.add("Current Being Drawn", 0)
            .withWidget(BuiltInWidgets.kNumberBar)
            .withPosition(7,1)
            .withSize(2,1)
            .withProperties(Map.of("Min", 0, "Max", 100))
            .getEntry();

        WOFEntry = tab.add("WOF Color", "#000000")
            .withPosition(7,2)
            .withSize(1,1) 
            .getEntry();
        
        tab.add("Gear", gearChooser)
            .withWidget(BuiltInWidgets.kSplitButtonChooser)
            .withPosition(8,2)
            .withSize(1,1); 

        //Column 2

        timeEntry = tab.add("Time Left", "15 s")
            .withPosition(9,0)
            .withSize(2,1) 
            .getEntry();

        shooterSpeedEntry = tab.add("Shooter Speed (RPM)", 0)
            .withWidget(BuiltInWidgets.kGraph)
            .withPosition(9,1)
            .withSize(2,2)
            .withProperties(Map.of("Min", 0, "Max", 600,"Visible Time", 10))
            .getEntry();
        
        NMFLayout = tab.getLayout("Count To Five Tutorial", BuiltInLayouts.kGrid)
            .withPosition(7, 3)
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

        autoLayout = tab.getLayout("Auto Selection", BuiltInLayouts.kGrid)
            .withPosition(7, 4)
            .withSize(4, 1)
            .withProperties(Map.of("Number of Rows", 1, "Number of columns", 3, "Label Position", "TOP"));
        autoLayout.add("Starting Positions", autoStartChooser)
            .withWidget(BuiltInWidgets.kComboBoxChooser)
            .withPosition(0,0)
            .withSize(1,1);
        autoLayout.add("Target Port", autoPortChooser)
            .withWidget(BuiltInWidgets.kComboBoxChooser)
            .withPosition(1,0)
            .withSize(1,1);
        autoLayout.add("Pick Up Positions", autoPickUpChooser)
            .withWidget(BuiltInWidgets.kComboBoxChooser)
            .withPosition(2,0)
            .withSize(1,1);


        if(config) {
            configInit();
        }
    }

    public static void update() {
        voltageEntry.setDouble(edu.wpi.first.wpilibj.RobotController.getBatteryVoltage());
        currentEntry.setDouble(0.0);

        WOFEntry.setString(Context.WOFColors.get(Context.WOFTargetColor));

        //gearEntry.setValue(Context.robotController.drivetrain.gear);
        

        // boolean[] NMFArray = Context.robotController.ballPositions.getBallPositions();
        // NMFEntry1.setBoolean(NMFArray[0]);
        // NMFEntry2.setBoolean(NMFArray[1]);
        // NMFEntry3.setBoolean(NMFArray[2]);
        // NMFEntry4.setBoolean(NMFArray[3]);
        // NMFEntry5.setBoolean(NMFArray[4]);

        timeEntry.setString(Timer.getMatchTime() + " s");
    }

    private static void configInit() {
        configTab = Shuffleboard.getTab("Config");
    }

    private static void setChoosers() {
        gearChooser = new SendableChooser<Gear>();
        gearChooser.setDefaultOption("Lo", Gear.LOW);
        gearChooser.addOption("Hi", Gear.HIGH);

        autoStartChooser = new SendableChooser<AutoOptions.Start>();
        autoStartChooser.setDefaultOption("Left", AutoOptions.Start.LEFT);
        autoStartChooser.addOption("Middle", AutoOptions.Start.MIDDLE);
        autoStartChooser.addOption("Right", AutoOptions.Start.RIGHT);

        autoPortChooser = new SendableChooser<AutoOptions.Port>();
        autoPortChooser.addOption("Low", AutoOptions.Port.LOW);
        autoPortChooser.setDefaultOption("High", AutoOptions.Port.HIGH);

        autoPickUpChooser = new SendableChooser<AutoOptions.PickUp>();
        autoPickUpChooser.addOption("Alliance Trench", AutoOptions.PickUp.TRENCH_ALLIANCE);
        autoPickUpChooser.setDefaultOption("Enemy Trench", AutoOptions.PickUp.TRENCH_ENEMY);
        autoPickUpChooser.addOption("Rendezvous", AutoOptions.PickUp.RENDEZVOUS);
    }

    public static Gear getGear() {
        return gearChooser.getSelected();
    }
}