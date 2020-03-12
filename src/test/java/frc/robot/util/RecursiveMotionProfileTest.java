package frc.robot.util;

import org.junit.Test;

import frc.robot.util.*;

public class RecursiveMotionProfileTest {

    double desiredVel;

    double currentVel;
    double lastVel;
    double currentAccel;

    double deltaTime;

    @Test
    public void ConvergenceTest1() {

        RecursiveMotionProfile RMP = new RecursiveMotionProfile(5, 10, 20, 0.1);
        
        currentVel = 0;
        lastVel = 0;
        currentAccel = 0;
        desiredVel = 10;

        for(int i = 0; i < 100; i++) {

            RMP.updateParameters(desiredVel, currentVel, currentAccel);
            currentVel = RMP.getVelNext();
            
        }

    }

}