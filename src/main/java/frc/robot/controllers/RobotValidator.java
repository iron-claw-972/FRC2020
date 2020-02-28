package frc.robot.controllers;

import java.util.ArrayList;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.SparkMax;

import frc.robot.util.Context;

public class RobotValidator {
    public ArrayList<Object> motors = new ArrayList<Object>();

    public RobotValidator () {
        motors.add(Context.robotController.leftDriveMotor1);
        motors.add(Context.robotController.leftDriveMotor2);
        motors.add(Context.robotController.rightDriveMotor1);
        motors.add(Context.robotController.rightDriveMotor2);
        motors.add(Context.robotController.leftDriveEncoderInterface);
        motors.add(Context.robotController.rightDriveEncoderInterface);
        motors.add(Context.robotController.omniNeo);
        motors.add(Context.robotController.nmfNeo);
        motors.add(Context.robotController.intakeTalon);
        motors.add(Context.robotController.telescopeMotor);
        motors.add(Context.robotController.coilMotor);
    }

    public void runTests() {
        for(Object motor : motors) {
            if(!checkMotor(motor)) {
                print_e(motor.getClass().toString() + " on CAN ID: " + getMotorID(motor) + " has failed validation");
            }
        }
    }

    public boolean checkMotor(Object motor) {
        if(motor.getClass().equals(TalonFX.class)) {
            return checkFalconFirmware((TalonFX) motor);
        } else if(motor.getClass().equals(TalonSRX.class)){
            return checkTalonFirmware((TalonSRX) motor);
        } else if(motor.getClass().equals(VictorSPX.class)){
            return checkVictorFirmware((VictorSPX) motor);
        } else if(motor.getClass().equals(SparkMax.class)){
            //TODO: Check Spark version
            return true;
        } else {
            print_e("Motor controller not recognized!");
            return false;
        }
    }

    public boolean checkFalconFirmware(TalonFX motor) {
        boolean isCorrectVersion = false;

        try {
            isCorrectVersion = motor.getFirmwareVersion() == Context.latestFalconFirmware;
        } catch (Exception e) {
            isCorrectVersion = false;
            print_e(e.toString());
        }

        return isCorrectVersion;
    }

    public boolean checkTalonFirmware(TalonSRX motor) {
        boolean isCorrectVersion = false;

        try {
            isCorrectVersion = motor.getFirmwareVersion() == Context.latestTalonFirmware;
        } catch (Exception e) {
            print_e(e.toString());
        }

        return isCorrectVersion;
    }

    public boolean checkVictorFirmware(VictorSPX motor) {
        boolean isCorrectVersion = false;

        try {
            isCorrectVersion = motor.getFirmwareVersion() == Context.latestVictorFirmware;
        } catch (Exception e) {
            print_e(e.toString());
        }

        return isCorrectVersion;
    }

    public int getMotorID(Object motor) {
        if(motor.getClass().equals(TalonFX.class)) {
            return ((TalonFX) motor).getDeviceID();
        } else if(motor.getClass().equals(TalonSRX.class)){
            return ((TalonSRX) motor).getDeviceID();
        } else if(motor.getClass().equals(VictorSPX.class)){
            return ((VictorSPX) motor).getDeviceID();
        } else if(motor.getClass().equals(SparkMax.class)){
            return ((SparkMax) motor).getChannel();
        } else {
            print_e("Motor controller not recognized!");
            return -99;
        }
    }

    public void print(String input) {
        System.out.println("RobotValidation: " + input);
    }

    public void print_e(String input) {
        System.out.println("ERROR RobotValidation: " + input);
    }

    public void print_e(String input, int errorID) {
        System.out.println("ERROR " + errorID + " RobotValidation: " + input);
    }
}