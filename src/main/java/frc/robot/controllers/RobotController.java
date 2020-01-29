package frc.robot.controllers;

import frc.robot.util.Context;

public class RobotController
{
    public Drivetrain drivetrain;
    public AutoDrive autoDrive;
    public NavX navX;
    public ZMQServer zmqServer;
    public NetworktablesInterface ntInterface;
    public DriverJoystick driverJoystick;
    public VisionAllignment visionAllignment;
    public OpticalLocalization opticalLocalization;

    public RobotController () {
        drivetrain = new Drivetrain();
        autoDrive = new AutoDrive();
        navX = new NavX();
        zmqServer = new ZMQServer();
        zmqServer.start();
        ntInterface = new NetworktablesInterface();
        driverJoystick = new DriverJoystick();
        visionAllignment = new VisionAllignment();
        opticalLocalization = new OpticalLocalization();

        Context.robotController = this;
    }

    public void initAll()
    {

    }

    public void loopAll()
    {
        ntInterface.run();
        opticalLocalization.Update();
        visionAllignment.loop();
    }
}