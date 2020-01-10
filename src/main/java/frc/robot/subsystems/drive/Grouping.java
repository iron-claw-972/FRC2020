package frc.robot.subsystems.drive;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

class Grouping
{
    public final TalonSRX left1 = new TalonSRX(0);
    public final TalonSRX right1 = new TalonSRX(2);
    public final TalonSRX right2 = new TalonSRX(3);
    public final TalonSRX left2 = new TalonSRX(1);

    public void GroupMotors()
    {
        left2.follow(left1);
        right2.follow(right1);
    }
}