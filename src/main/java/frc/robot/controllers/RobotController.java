package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.cscore.UsbCamera;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.*;

import frc.robot.util.Context;
import frc.robot.controllers.drive.*;
import frc.robot.execution.ParallelScheduler;
import frc.robot.execution.SequentialScheduler;;

public class RobotController {
    public CANSparkMax telescopeMotor;
    public TalonSRX coilMotor1;
    public TalonSRX coilMotor2;
    public TalonFXDrivetrain drivetrain;
    public AutoDrive autoDrive;
    public NavX navX;
    public NetworktablesInterface ntInterface;
    public DriverJoystick driverJoystick;
    // public VisionAllignment visionAllignment;
    public Compressor compressor;
    public ShooterController shooterController;
    public Intake intake;
    public OpticalLocalization opticalLocalization;
    public Climber climber;
  
    public SequentialScheduler sequentialScheduler;
    public ParallelScheduler parallelScheduler;
    public NMFController nmfController;

    public TalonSRX leftDriveMotor1;
    public TalonFX leftDriveMotor2;
    public TalonFX rightDriveMotor1;
    public TalonFX rightDriveMotor2;

    public TalonSRX leftDriveEncoderInterface;
    public TalonSRX rightDriveEncoderInterface;
    
    public CANSparkMax nmfNeo;
    public CANSparkMax omniNeo;

    public DoubleSolenoid intakeFlipSolenoid;
    public TalonSRX intakeTalon;

    public DriverStation driverStation;
    public PowerDistributionPanel pdp;
    public UsbCamera camera;

    public RobotController () {
        //----- Motors -----
        leftDriveMotor1 = new TalonSRX(Context.leftMotor1ID);
        /*
        leftDriveMotor2 = new TalonFX(Context.leftMotor2ID);
        rightDriveMotor1 = new TalonFX(Context.rightMotor1ID);
        rightDriveMotor2 = new TalonFX(Context.rightMotor2ID);
        leftDriveEncoderInterface = new TalonSRX(Context.leftEncoderInterfaceID);
        rightDriveEncoderInterface = new TalonSRX(Context.rightEncoderInterfaceID);

        omniNeo = new CANSparkMax(Context.omniSparkID, MotorType.kBrushless);
        nmfNeo = new CANSparkMax(Context.nmfSparkID, MotorType.kBrushless);
        intakeTalon = new TalonSRX(Context.intakeMotorId);

        //----- Pneumatics -----
        intakeFlipSolenoid = new DoubleSolenoid(Context.intakeFlipChannelA, Context.intakeFlipChannelB);
        
        compressor = new Compressor();
        compressor.setClosedLoopControl(true);
        */
        //----- Controllers -----
        /* Change this line when using a different drive train. Don't forget to change the motor ids in context */
        telescopeMotor = new CANSparkMax(Context.climberMotorID, MotorType.kBrushless);
        coilMotor1 = new TalonSRX (Context.coilMotor1ID);
        coilMotor2 = new TalonSRX (Context.coilMotor2ID);
        /*drivetrain = new TalonFXDrivetrain(leftDriveMotor1, leftDriveMotor2, rightDriveMotor1, rightDriveMotor2, leftDriveEncoderInterface, rightDriveEncoderInterface);
        autoDrive = new AutoDrive();
        navX = new NavX(new AHRS(SPI.Port.kMXP));
        ntInterface = new NetworktablesInterface();
        */
        driverJoystick = new DriverJoystick();
        /*
        intake = new Intake(intakeTalon, intakeFlipSolenoid);
        nmfController = new NMFController(nmfNeo, omniNeo);
        opticalLocalization = new OpticalLocalization();
        */
        climber = new Climber(coilMotor1, coilMotor2, leftDriveMotor1, telescopeMotor);
        /*
        sequentialScheduler = new SequentialScheduler();

        driverStation = DriverStation.getInstance();
        */
        Context.robotController = this;
    }

    public void initAll() {
        climber.resetClimbEncoder();
    }

    public void loopAll() {
        climber.loop();  
        /*
        ntInterface.loop();
        opticalLocalization.Update();
        intake.loop();
        nmfController.loop();
        */
    }
}
