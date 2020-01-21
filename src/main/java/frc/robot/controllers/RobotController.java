package frc.robot.controllers;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SerialPort;
import frc.robot.util.Context;

public class RobotController
{
    public Drivetrain drivetrain;
    public AutoDrive autoDrive;
    public AHRS navX;
    public ZMQServer zmqServer;
    public NetworktablesInterface ntInterface;
    public DriverJoystick driverJoystick;
    public VisionAllignment visionAllignment;

    public RobotController () {
        drivetrain = new Drivetrain();
        autoDrive = new AutoDrive();
        navX = new AHRS(SerialPort.Port.kMXP);
        zmqServer = new ZMQServer();
        zmqServer.start();
        ntInterface = new NetworktablesInterface();
        driverJoystick = new DriverJoystick();
        visionAllignment = new VisionAllignment();

        Context.robotController = this;
    }

    public void initAll()
    {

    }

    public void loopAll()
    {
        ntInterface.run();
        visionAllignment.loop();
    }
}