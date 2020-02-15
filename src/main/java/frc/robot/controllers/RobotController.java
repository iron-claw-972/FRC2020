package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;

import edu.wpi.first.wpilibj.Compressor;
import frc.robot.util.Context;

public class RobotController {
    public CANSparkMax telescopeMotor;
    public TalonSRX coilMotor;
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
    public Climber climber;

    public RobotController () {
        /* Change this line when using a different drive train. Don't forget to change the motor ids in context */
        telescopeMotor = new CANSparkMax(Context.climberMotorID, MotorType.kBrushless);
        coilMotor = new TalonSRX (Context.climberMotorID);
        drivetrain = new TalonFXDrivetrain();
        autoDrive = new AutoDrive();
        navX = new NavX();
        zmqServer = new ZMQServer();
        zmqServer.start();
        ntInterface = new NetworktablesInterface();
        driverJoystick = new DriverJoystick();
        visionAllignment = new VisionAllignment();
        compressor = new Compressor();
        intake = new Intake();
        opticalLocalization = new OpticalLocalization();
        climber = new Climber(coilMotor, telescopeMotor);

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