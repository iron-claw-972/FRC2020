package frc.robot.controllers;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.util.Context;
import frc.robot.util.PID;

public class LEDController
{
    TalonSRX talon;
    double power_set = 0.0f;
    PID pid_controller = new PID(1.0,0,0);

    final boolean DEBUG = true;

    double time = 0;
    double oldtime = 0-20;

    public LEDController(TalonSRX controller) {
        talon = controller;
    }

    public void loop(){
        time = Timer.getFPGATimestamp();
        double voltage = talon.getMotorOutputVoltage();
        double current = talon.getStatorCurrent();

        double output_power = voltage*current;

        double pidPower = pid_controller.update(power_set, output_power, time-oldtime);

        talon.set(ControlMode.PercentOutput, pidPower);
        oldtime = time;

        if(DEBUG){
            System.out.println("Voltage Output: " + voltage);
            System.out.println("Current Output: " + current);
            System.out.println("Current Power Output: " + output_power);
            System.out.println("Set Power Output: " + power_set);
        }
    }

    public void setPower(double power) // power in watts {
        power_set = power;
    }
}
