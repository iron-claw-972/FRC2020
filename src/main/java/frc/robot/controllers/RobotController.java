package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import frc.robot.util.Context;

public class RobotController {
    public TalonFXDrivetrain drivetrain;
    public AutoDrive autoDrive;
    public NavX navX;
    public ZMQServer zmqServer;
    public NetworktablesInterface ntInterface;
    public DriverJoystick driverJoystick;
    public VisionAllignment visionAllignment;
    public Compressor compressor;
    public Intake intake;
    public OpticalLocalization opticalLocalization;
    private TalonSRX leftEncoderInterface, rightEncoderInterface;

    public RobotController () {
        /* Change this line when using a different drive train. Don't forget to change the motor ids in context */
        leftEncoderInterface = new TalonSRX(Context.leftEncoderInterfaceID);
        leftEncoderInterface.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
        rightEncoderInterface = new TalonSRX(Context.rightEncoderInterfaceID);
        rightEncoderInterface.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);

        drivetrain = new TalonFXDrivetrain(leftEncoderInterface, rightEncoderInterface);
        autoDrive = new AutoDrive();
        // navX = new NavX();
        zmqServer = new ZMQServer();
        zmqServer.start();
        ntInterface = new NetworktablesInterface();
        driverJoystick = new DriverJoystick();
        visionAllignment = new VisionAllignment();
        compressor = new Compressor();
        intake = new Intake();
        opticalLocalization = new OpticalLocalization();

        Context.robotController = this;
    }

    public void initAll() {
    }

    public void loopAll() {
        ntInterface.run();
        opticalLocalization.Update();
        visionAllignment.loop();
    }
}