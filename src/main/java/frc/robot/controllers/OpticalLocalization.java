package frc.robot.controllers;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class OpticalLocalization
{
    final boolean I2C_DEBUG = true;

    final byte LeftAddress = 25;
    final byte RightAddress = 26;
    final int ReplyLength = 10;

    I2C LeftSensor = new I2C(Port.kOnboard, LeftAddress);
    I2C RightSensor = new I2C(Port.kOnboard, LeftAddress);

    public Integer LeftMovementX = 0, LeftMovementY = 0;
    public double LeftQuality = 0;
    public Integer RightMovementX = 0, RightMovementY = 0;
    public double RightQuality = 0;

    public Long LeftPosX = 0L;
    public Long LeftPosY = 0L;
    public Long RightPosX, RightPosY;

    public boolean IsNewLeftData = false;
    public boolean IsNewRightData = false;

    public void Update()
    {
        byte[] Data = new byte[ReplyLength]; // incoming byte data buffer
        LeftSensor.readOnly(Data, Data.length); //requesting arduino to send the data, write it into the buffer
        LeftQuality = Data[0]; // 0th byte is the surface quality
        LeftMovementX = (Integer)(int) ((int)Data[1] | ((int)Data[2] << 8) | ((int)Data[3] << 16) | ((int)Data[4] << 24)); // combine the bytes into an integer using unsigned byte stuff
        LeftMovementY = (Integer)(int) ((int)Data[5] | ((int)Data[6] << 8) | ((int)Data[7] << 16) | (int)(Data[8] << 24));

        IsNewLeftData = Data[9] >= 1; // check if the sensor data has been updated since last i2c data request

        RightSensor.readOnly(Data, Data.length); // same but on the right
        RightQuality = Data[0];
        RightMovementX = (Integer)(int) ((int)Data[1] | ((int)Data[2] << 8) | ((int)Data[3] << 16) | ((int)Data[4] << 24));
        RightMovementY = (Integer)(int) ((int)Data[5] | ((int)Data[6] << 8) | ((int)Data[7] << 16) | (int)(Data[8] << 24));

        IsNewRightData = Data[9] >= 1;
    }

    public boolean IsFreshLeft()
    {
        return IsNewLeftData;
    }

    public boolean IsFreshRight()
    {
        return IsNewRightData;
    }
}