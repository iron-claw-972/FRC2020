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

    public boolean IsNewData = false;

    public void Update()
    {
        byte[] Data = new byte[ReplyLength];
        LeftSensor.readOnly(Data, Data.length);
        LeftQuality = Data[0];
        LeftMovementX = (Integer)(int) ((int)Data[1] | ((int)Data[2] << 8) | ((int)Data[3] << 16) | ((int)Data[4] << 24));
        LeftMovementY = (Integer)(int) ((int)Data[5] | ((int)Data[6] << 8) | ((int)Data[7] << 16) | (int)(Data[8] << 24));
        for(int i =0; i<ReplyLength; i++)
        {
            //System.out.println(String.format("0x%08X", Data[i]));
        }
        if(Data[9] == 1) IsNewData = true;
        else IsNewData = false;
    }

    public boolean IsFresh()
    {
        return IsNewData;
    }
}