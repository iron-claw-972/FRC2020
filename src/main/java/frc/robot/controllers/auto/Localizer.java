package frc.robot.controllers.auto;

import java.util.ArrayList;
import java.util.List;

import com.acmerobotics.roadrunner.drive.TankDrive;
import com.acmerobotics.roadrunner.drive.TankDrive.TankLocalizer;
import com.acmerobotics.roadrunner.geometry.Pose2d;

import frc.robot.util.Context;

public class Localizer {
	
	private TankDrive tankDrive;
	
	public Localizer(double trackWidth) {
		
		tankDrive = new TankDrive(0, 0, 0, trackWidth) {
            @Override
            protected double getRawExternalHeading() {
                // Also not using external heading for localization
                return 0.0;
            }
        
            @Override
            public void setMotorPowers(double leftPower, double rightPower) {
                // Not using their code to control the motors
            }
        
            @Override
            public List<Double> getWheelPositions() {
                List<Double> output = new ArrayList<Double>();
                output.add(Context.robotController.drivetrain.getLeftDist());
                output.add(Context.robotController.drivetrain.getRightDist());
                
                return output;
            }
        };
        
        resetLocalization();
	}
	
    public void resetLocalization() {
        /* Disables the use of external heading */
        tankDrive.setLocalizer(new TankLocalizer(tankDrive, false));
    }
    
    public void update() {
    	tankDrive.updatePoseEstimate();
    }
    
    public Pose2d getPoseEstimate() {
    	return tankDrive.getPoseEstimate();
    }

}