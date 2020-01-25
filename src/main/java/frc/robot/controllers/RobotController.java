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
    public Intake intake;

    public RobotController () {
        drivetrain = new Drivetrain();
        autoDrive = new AutoDrive();
        navX = new AHRS(SerialPort.Port.kMXP);
        zmqServer = new ZMQServer();
        zmqServer.start();
        ntInterface = new NetworktablesInterface();
        driverJoystick = new DriverJoystick();
        intake = new Intake();

        Context.robotController = this;
    }
}