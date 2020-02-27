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
        Context.robotController = mock(RobotController.class);
        when(Context.alignmentThreshold).thenReturn(0.0);
        VisionAlign testVision = new VisionAlign();
        when(Context.robotController.ntInterface.tx).thenReturn(0.0);
        when(Context.robotController.ntInterface.ty).thenReturn(0.0);
        testVision.grabLimelightData();
        
    }

    @Test
    public void localizationTest()
    {


    }

}