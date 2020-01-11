package frc.robot.controllers;

import frc.robot.util.Context;

public class RobotController
{
    public Drivetrain drivetrain;
    public AutoDrive autoDrive;
    public NavX navX;
    public ZMQServer zmqServer;
    public NetworktablesInterface ntInterface;

    public RobotController () {
        drivetrain = new Drivetrain();
        autoDrive = new AutoDrive();
        navX = new NavX();
        zmqServer = new ZMQServer();
        zmqServer.start();
        ntInterface = new NetworktablesInterface();

        Context.robotController = this;
    }
}