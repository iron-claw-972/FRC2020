package frc.robot.actions;

import static org.junit.Assert.*;
import org.junit.*;

import static org.mockito.Mockito.*;
import frc.robot.controllers.RobotController;
import frc.robot.util.Context;

public class AlignTest
{
    @Before
    public void setup()
    {
        VisionAlign testVision = new VisionAlign();
        Context.robotController = mock(RobotController.class);
        when(Context.alignmentThreshold).thenReturn(0.0);
        when(Context.robotController.navX.getRawHeading()).thenReturn(0.0);
        when(Context.robotController.ntInterface.tx).thenReturn(0.0);
        when(Context.robotController.ntInterface.ty).thenReturn(0.0);
        when(Context.robotController.ntInterface.targetAcquired).thenReturn(true);
        
    }

    @Test
    public void localizationTest()
    {
        

    }

}