package frc.robot.controllers;

import frc.robot.util.Context;

public class RobotController
{
    public NeoDrivetrain drivetrain;
    public AutoDrive autoDrive;
    public NavX navX;
    public ZMQServer zmqServer;
    public NetworktablesInterface ntInterface;
    public DriverJoystick driverJoystick;
    public VisionAllignment visionAllignment;

    public RobotController () {
        drivetrain = new NeoDrivetrain();
        autoDrive = new AutoDrive();
        navX = new NavX();
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