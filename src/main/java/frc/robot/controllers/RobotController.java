package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import frc.robot.util.Context;

public class RobotController {
    public NavX navX;
    public NetworktablesInterface ntInterface;
    public DriverJoystick driverJoystick;
    public Compressor compressor;
    public ShooterController shooterController;
    public OpticalLocalization opticalLocalization;

    public TalonFX leftDriveMotor1;
    public TalonFX leftDriveMotor2;
    public TalonFX rightDriveMotor1;
    public TalonFX rightDriveMotor2;
    public TalonSRX rollingIntake;
    public TalonSRX beltIntake;

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

        Context.robotController = this;
    }

    public void initAll() {
    }

    public void loopAll() {
        ntInterface.loop();
        opticalLocalization.Update();
    }
}