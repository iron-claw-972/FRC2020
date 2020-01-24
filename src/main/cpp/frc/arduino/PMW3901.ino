void setup()
{
  // put your setup code here, to run once:
  Serial1.begin(115200);
  Serial.begin(115200);
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

void loop()
{
  // put your main code here, to run repeatedly:
  while (Serial1.available() > 0)
  {
    if (!foundPacket)
    {
      memcpy(inBytes, &inBytes[1], 8);
      inBytes[8] = Serial1.read();
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
      inBytes[dataBytes] = Serial1.read();
      dataBytes++;
      if (dataBytes == 8)
      {
        uint32_t a = inBytes[1] + (inBytes[2] << 8) + (inBytes[3] << 16) + (inBytes[4] << 24);
        MotionX = (int32_t)a;

        a = inBytes[5] + (inBytes[6] << 8) + (inBytes[7] << 16) + (inBytes[8] << 24);
        MotionY = (int32_t)a;

        foundPacket = false;
        Serial.print("Surface Quality: ");
        surfaceQuality = inBytes[0];
        Serial.println(surfaceQuality);

        //MotionX = byteArrToLong(motionXBytes.result);
        //MotionY = byteArrToLong(motionYBytes.result);

        PositionX += MotionX;
        PositionY += MotionY;

        Serial.print("X: ");
        Serial.print(PositionX);
        Serial.print(" Y: ");
        Serial.println(PositionY);
        
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

long byteArrToLong(byte Arr[])
{
  long value = Arr[3];
  value *= 256 + Arr[2];
  value *= 256 + Arr[1];
  value *= 256 + Arr[0];
  return value;
}
