package frc.robot.controllers;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.I2C.Port;
import frc.robot.util.Context;
import frc.robot.util.Vector;

public class OpticalLocalization{
    final boolean I2C_DEBUG = true;

    final byte LeftAddress = 25;
    final byte RightAddress = 26;
    final int ReplyLength = 10;

    I2C LeftSensor = new I2C(Port.kOnboard, LeftAddress);
    I2C RightSensor = new I2C(Port.kOnboard, LeftAddress);

    Integer LeftMovementX = 0, LeftMovementY = 0;
    double LeftQuality = 0;
    Integer RightMovementX = 0, RightMovementY = 0;
    double RightQuality = 0;

    Vector LeftMov = new Vector();
    Vector RightMov = new Vector();
    Vector TotalMov = new Vector();

    Vector OldLeftMov = new Vector();
    Vector OldRightMov = new Vector();

    public Vector Position = new Vector();
    public double Yaw = 0;
    public double Yaw_Delta = 0;

    public boolean IsNewLeftData = false;
    public boolean IsNewRightData = false;

    public void Update(){
        byte[] Data = new byte[ReplyLength]; // incoming byte data buffer
        LeftSensor.readOnly(Data, Data.length); //requesting arduino to send the data, write it into the buffer
        LeftQuality = Data[0]; // 0th byte is the surface quality
        LeftMovementX = (Integer)(int) ((int)Data[1] | ((int)Data[2] << 8) | ((int)Data[3] << 16) | ((int)Data[4] << 24)); // combine the bytes into an integer using unsigned byte stuff
        LeftMovementY = (Integer)(int) ((int)Data[5] | ((int)Data[6] << 8) | ((int)Data[7] << 16) | (int)(Data[8] << 24));

        IsNewLeftData = Data[9] >= 1; // check if the sensor data has been updated since last i2c data request
        LeftMov = new Vector((double)LeftMovementX, (double)LeftMovementY, Timer.getFPGATimestamp()); // 3rd argument as data received timestamp, used for interpolation

        RightSensor.readOnly(Data, Data.length); // same but on the right
        RightQuality = Data[0];
        RightMovementX = (Integer)(int) ((int)Data[1] | ((int)Data[2] << 8) | ((int)Data[3] << 16) | ((int)Data[4] << 24));
        RightMovementY = (Integer)(int) ((int)Data[5] | ((int)Data[6] << 8) | ((int)Data[7] << 16) | (int)(Data[8] << 24));

        IsNewRightData = Data[9] >= 1;
        RightMov = new Vector(RightMovementX, RightMovementY, Timer.getFPGATimestamp()); // 3rd argument as data received timestamp, used for interpolation

        if(IsNewLeftData){
            OldLeftMov = LeftMov;
        }
        else{
            LeftMov = OldLeftMov.multiply(Timer.getFPGATimestamp()/(Timer.getFPGATimestamp() - OldLeftMov.z));
        }// if no new data is available interpolate and assume the movement vector based on previous reading
        if(IsNewRightData){
            OldRightMov = RightMov;
        }
        else{
            RightMov = OldRightMov.multiply(Timer.getFPGATimestamp()/(Timer.getFPGATimestamp() - OldRightMov.z));
        }
        TotalMov = RightMov.add(LeftMov).multiply(0.5);
        Yaw_Delta = 2* Math.asin(LeftMov.subtract(RightMov).multiply(0.5).Length()/(Context.sensorSpacing));
        Yaw += Yaw_Delta;
        Position = Position.add(TotalMov.rotateXY(Yaw));
    }

    public boolean IsFreshLeft(){
        return IsNewLeftData;
    }

    public boolean IsFreshRight(){
        return IsNewRightData;
    }
}
