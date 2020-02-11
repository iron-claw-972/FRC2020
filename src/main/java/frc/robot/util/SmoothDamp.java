package frc.robot.util;

public class SmoothDamp { 
    public double curVel = 0, stepSize = 0, deadbandLimit, maxOut, minOut;
    public boolean handleDeadband = false, limitOutput = false;

    public SmoothDamp(double stepSize_)
    {
        stepSize = stepSize_;
    }

    public SmoothDamp(double stepSize_, double deadbandLimit_)
    {
        stepSize = stepSize_;
        deadbandLimit = deadbandLimit_;
        handleDeadband = true;
    }

    public void setOutputLimits(double minOut_, double maxOut_)
    {
        limitOutput = true;
        minOut = minOut_;
        maxOut = maxOut_;
    }

    public double update(double tgtSpeed)
    {
        double outputVel;

        if(handleDeadband && Math.abs(curVel) < deadbandLimit)
        {
            tgtSpeed = 0;
        }

        if((tgtSpeed - curVel)>stepSize)
        {
            curVel+=stepSize;
        }
        else if((curVel-tgtSpeed) > stepSize)
        {
            curVel-=stepSize;
        }

        if(limitOutput)
        {
            if(curVel > maxOut)
            {
                outputVel = maxOut;
            }
            else if(curVel < minOut)
            {
                outputVel = minOut;
            }
            else
            {
                outputVel = curVel;
            }
        }
        else
        {
            outputVel = curVel;
        }

        return outputVel;
    }
}