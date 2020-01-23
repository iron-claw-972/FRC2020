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

union ByteToLong
{
  byte arr[4];
  int32_t result;
};

void loop()
{
  // put your main code here, to run repeatedly:
  while(Serial1.available()>0)
  {
    if(!foundPacket)
    {
      memcpy(inBytes, &inBytes[1], 8); 
      inBytes[8] = Serial1.read();
      if(allIsTrue(inBytes, catchPattern))
      {
        foundPacket = true;
        //Serial.println("Packet found");
      }
      for(int i = 0 ; i< 9; i++)
      {/*
        Serial.print(inBytes[i]);
        Serial.print("-");*/
      }
    }
    else
    {
      inBytes[dataBytes] = Serial1.read();
      dataBytes++;
      if(dataBytes == 8)
      {
        ByteToLong motionXBytes;
        motionXBytes.arr[0] = inBytes[1];
        motionXBytes.arr[1] = inBytes[2];
        motionXBytes.arr[2] = inBytes[3];
        motionXBytes.arr[3] = inBytes[4];

        ByteToLong motionYBytes;
        motionYBytes.arr[0] = inBytes[5];
        motionYBytes.arr[1] = inBytes[6];
        motionYBytes.arr[2] = inBytes[7];
        motionYBytes.arr[3] = inBytes[8];
        
        foundPacket = false;
        Serial.print("Surface Quality: ");
        surfaceQuality = inBytes[0];
        Serial.println(surfaceQuality);

        MotionX = byteArrToLong(motionXBytes.result);
        MotionY = byteArrToLong(motionYBytes.result);

        Serial.print("X: ");
        Serial.print(MotionX);
        Serial.print(" Y: ");
        Serial.println(MotionY);
        
        dataBytes = 0;
      }
    }
  }
  //Serial.print("\n");
}

bool allIsTrue(byte A[], byte B[])
{
  bool is = true;
  for(int i = 0; i < 8; i++)
  {
    is = A[i+1] == B[i];
    if(is ==false)
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
