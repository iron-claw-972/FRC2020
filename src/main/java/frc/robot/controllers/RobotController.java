package frc.robot.controllers;

import frc.robot.util.Context;
import frc.robot.util.DriverJoystick;

public class RobotController
{
    public Drivetrain drivetrain;
    public AutoDrive autoDrive;
    public NavX navX;
    public ZMQServer zmqServer;
    public NetworktablesInterface ntInterface;
    public DriverJoystick driverJoystick;

    public RobotController () {
        drivetrain = new Drivetrain();
        autoDrive = new AutoDrive();
        navX = new NavX();
        zmqServer = new ZMQServer();
        zmqServer.start();
        ntInterface = new NetworktablesInterface();
        driverJoystick = new DriverJoystick();

        Context.robotController = this;
    }
}