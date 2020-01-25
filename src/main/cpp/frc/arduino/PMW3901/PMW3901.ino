#include <Wire.h>

#define I2C_ADDRESS 0x25

void setup()
{
  Serial.begin(115200);
  Wire.begin(I2C_ADDRESS);
}

byte catchPattern[] = { 0x24, 0x58, 0x3c, 0x00, 0x02, 0x1f, 0x09, 0x00};
byte inBytes[9];

bool foundPacket = false;
byte dataBytes = 0;

uint8_t surfaceQuality = 0;

int32_t MotionX = 0;
int32_t MotionY = 0;

int32_t PositionX = 0;
int32_t PositionY = 0;

union int32ByteConverter
{
  byte bytes[4];
  int32_t integer;
};

void loop()
{
  // put your main code here, to run repeatedly:
  while (Serial.available() > 0)
  {
    if (!foundPacket)
    {
      memcpy(inBytes, &inBytes[1], 8);
      inBytes[8] = Serial.read();
      if (allIsTrue(inBytes, catchPattern))
      {
        foundPacket = true;
        //Serial.println("Packet found");
      }
      for (int i = 0 ; i < 9; i++)
      { /*
          Serial.print(inBytes[i]);
          Serial.print("-");*/
      }
    }
    else
    {
      inBytes[dataBytes] = Serial.read();
      dataBytes++;
      if (dataBytes == 8)
      {
        uint32_t a = inBytes[1] + (inBytes[2] << 8) + (inBytes[3] << 16) + (inBytes[4] << 24);
        MotionX = (int32_t)a;

        a = inBytes[5] + (inBytes[6] << 8) + (inBytes[7] << 16) + (inBytes[8] << 24);
        MotionY = (int32_t)a;

        foundPacket = false;
        surfaceQuality = inBytes[0];
        //MotionX = byteArrToLong(motionXBytes.result);
        //MotionY = byteArrToLong(motionYBytes.result);

        PositionX += MotionX;
        PositionY += MotionY;

        dataBytes = 0;
      }
    }
  }
  //Serial.print("\n");
}

bool allIsTrue(byte A[], byte B[])
{
  bool is = true;
  for (int i = 0; i < 8; i++)
  {
    is = A[i + 1] == B[i];
    if (is == false)
    {
      return false;
    }
  }
  return true;
}

void receiveEvent()
{
  String data = "";

  // Data is sent one byte at a time, so piece it together
  // In the future, you should probably just send a number or something to avoid needing to piece together a string
  while ( Wire.available() > 0 )
  {
    char n=(char)Wire.read();
    if(((int)n)>((int)(' '))) data += n; 
  }
  
  if (data == ">$r")
  {
    int32ByteConverter converterX;
    converterX.integer = PositionX;

    int32ByteConverter converterY;
    converterY.integer = PositionY;
    byte dataToSend[] = { (byte)surfaceQuality, converterX.bytes[0], converterX.bytes[1], converterX.bytes[2], converterX.bytes[3],
    converterY.bytes[0], converterY.bytes[1], converterY.bytes[2], converterY.bytes[3]};
    for(int i = 0; i < sizeof(dataToSend); i++)
    {
      Wire.write(dataToSend[i]);
    }
    PositionX = 0;
    PositionY = 0;
  }
}
