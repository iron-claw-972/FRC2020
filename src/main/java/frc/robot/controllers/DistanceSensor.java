package frc.robot;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;

class DistanceSensor {
    int ID;
    DigitalInput DIO;

    Counter counter; // fml if I knew about this before...

    public DistanceSensor(int Channel_ID) {
        ID = Channel_ID;
        DIO = new DigitalInput(ID);
        counter = new Counter(DIO);
    }

    public double GetDistance()
    {
        return (counter.getPeriod())*100000-290; //given that 10us is 1cm and that the rise and fall variables are times in seconds, we can safely say that 1 second pulse width is 100,000cm
        //290 is calibrated value with a meter stick
        //return -1; //negative distance hopefully people understand that this is not a possibility and is an error code
    }
}



// shaking my smh this class is insanely complicated :\
