package main.java.frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;



public class Limelight {

    public double tx; 
    public double ty;
    public double distance;

    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    NetworkTable table = inst.getTable("limelight");
    NetworkTableEntry xEntry = table.getEntry("tx");
    NetworkTableEntry yEntry = table.getEntry("tx");

    public Limelight(){
        inst.startClientTeam(TEAM);  // where TEAM=190, 294, etc, or use inst.startClient("hostname") or similar
        inst.startDSClient();  // recommended if running on DS computer; this gets the robot IP from the DS
    }

    public void run(){
        double x = xEntry.getDouble(0.0);
        double y = yEntry.getDouble(0.0);
        System.out.println("tx: " + x + " ty: " + y);
    }
}