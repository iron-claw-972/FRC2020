package frc.robot.actions;
import static org.junit.Assert.*;
import org.junit.*;
import static org.mockito.Mockito.*;
import frc.robot.controllers.NavX;
import frc.robot.controllers.NetworktablesInterface;
import frc.robot.controllers.RobotController;
import frc.robot.util.Context;

public class AlignTest
{
    @Before
    public void setup()
    {
        Context.robotController = mock(RobotController.class);
        Context.robotController.navX = mock(NavX.class);
        Context.robotController.ntInterface = mock(NetworktablesInterface.class);
        when(Context.robotController.navX.getRawHeading()).thenReturn(0.0);
        Context.robotController.ntInterface.tx = 0.0;
        Context.robotController.ntInterface.ty = 0.0;
        Context.robotController.ntInterface.targetAcquired = true;
        
    }

    @Test
    public void localizationTest()
    {
        VisionAlign testVision = new VisionAlign();
        assertEquals((int)testVision.rotationLocalized, 0, 0.1);

    }

}