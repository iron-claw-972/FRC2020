package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.cscore.UsbCamera;

import frc.robot.util.Context;

public class RobotController {
    public TalonFXDrivetrain drivetrain;
    public AutoDrive autoDrive;
    public NavX navX;
    public NetworktablesInterface ntInterface;
    public DriverJoystick driverJoystick;
    public VisionAllignment visionAllignment;
    public Compressor compressor;
    public ShooterController shooterController;
    public Intake intake;
    public OpticalLocalization opticalLocalization;
    public NMFColorSensorController ballPositions;

    public TalonFX leftDriveMotor1;
    public TalonFX leftDriveMotor2;
    public TalonFX rightDriveMotor1;
    public TalonFX rightDriveMotor2;
    public TalonSRX rollingIntake;
    public TalonSRX beltIntake;
    
    public DriverStation driverStation;
    public PowerDistributionPanel pdp;
    public UsbCamera camera;

    public RobotController () {
        //----- Motors -----
        leftDriveMotor1 = new TalonFX(Context.leftMotor1ID);
        leftDriveMotor2 = new TalonFX(Context.leftMotor2ID);
        rightDriveMotor1 = new TalonFX(Context.rightMotor1ID);
        rightDriveMotor2 = new TalonFX(Context.rightMotor2ID);
        rollingIntake = new TalonSRX(-1);
        beltIntake = new TalonSRX(-1);

        //----- Pneumatics -----
        compressor = new Compressor();
        compressor.setClosedLoopControl(true);

        //----- Controllers -----
        /* Change this line when using a different drive train. Don't forget to change the motor ids in context */
        drivetrain = new TalonFXDrivetrain(leftDriveMotor1, leftDriveMotor2, rightDriveMotor1, rightDriveMotor2);
        autoDrive = new AutoDrive();
        navX = new NavX(new AHRS(SPI.Port.kMXP));
        ntInterface = new NetworktablesInterface();
        driverJoystick = new DriverJoystick();
        visionAllignment = new VisionAllignment();
        intake = new Intake();
        opticalLocalization = new OpticalLocalization();
        ballPositions = new NMFColorSensorController();

        driverStation = DriverStation.getInstance();

        Context.robotController = this;
    }

    public void initAll() {
    }

    public void loopAll() {
        ntInterface.loop();
        opticalLocalization.Update();
        visionAllignment.loop();
        ballPositions.loop();
        intake.loop();
    }
}
