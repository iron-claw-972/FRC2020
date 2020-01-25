package frc.robot.controllers;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class OpticalLocalization
{
    final Byte LeftAddress = 0x25;
    final Byte RightAddress = 0x26;

    I2C LeftSensor = new I2C(Port.kOnboard, LeftAddress);
    I2C RightSensor = new I2C(Port.kOnboard, LeftAddress);
}