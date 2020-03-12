package frc.robot.controllers;

import frc.robot.actions.*;
import frc.robot.actions.auto.*;

import static frc.robot.util.AdditionalMath.*;

public enum AutoConfigs {

    /*

        Starting Positions:
        POWER_PORT: Starting directly aligned with the power port.
        ALLIANCE_TRENCH: Starting directly aligned with the 3 balls in the alliance's trench.
        MIDDLE_FIELD: Starting in the middle of the field length-wise.

        Gather Positions:
        POWER_PORT_GATHER_TRENCH: Gather the 3 balls in our alliance's trench.
        POWER_PORT_GATHER_RENDEZVOUS: Gather the set of 3 balls in the rendezvous zone.
        ALLIANCE_TRENCH_GATHER: Gather the 3 balls in our alliance's trench.
        MIDDLE_FIELD_GATHER: Gather the set of 3 balls in the rendezvous zone.

        Shoot Positions:
        NEAR_SHOOT: After gathering balls, return to the strating position on the initiation line, align, and shoot. (Jetson Nano)
        FAR_SHOOT: After gathering balls, immediately align and shoot. (Limelight)
    
    */

    POWER_PORT_BASIC(
        new VisionAlign(),
        new Shoot(),
        new Forward(1.0)
    ),
    ALLIANCE_TRENCH_BASIC(
        new VisionAlign(),
        new Forward(1.0)
    ),
    MIDDLE_FIELD_BASIC(
        new VisionAlign(),
        new Forward(1.0)
    ),
    
    POWER_PORT_GATHER_TRENCH(
        new VisionAlign(),
        new Forward(1.0)
    ),
    POWER_PORT_GATHER_RENDEZVOUS(
        new VisionAlign(),
        new Forward(1.0)
    ),
    ALLIANCE_TRENCH_GATHER(
        new VisionAlign(),
        new Forward(1.0)
    ),
    MIDDLE_FIELD_GATHER(
        new VisionAlign(),
        new Forward(1.0)
    ),
    
    POWER_PORT_FAR_SHOOT_TRENCH(
        new VisionAlign(),
        new Forward(1.0)
    ),
    POWER_PORT_NEAR_SHOOT_TRENCH(
        new VisionAlign(),
        new Forward(1.0)
    ),
    POWER_PORT_FAR_SHOOT_RENDEZVOUS(
        new VisionAlign(),
        new Forward(1.0)
    ),
    POWER_PORT_NEAR_SHOOT_RENDEZVOUS(
        new VisionAlign(),
        new Forward(1.0)
    ),
    ALLIANCE_TRENCH_FAR_SHOOT(
        new VisionAlign(),
        new Forward(1.0)
    ),
    ALLIANCE_TRENCH_NEAR_SHOOT(
        new VisionAlign(),
        new Forward(1.0)
    ),
    MIDDLE_FIELD_FAR_SHOOT(
        new VisionAlign(),
        new Forward(1.0)
    ),
    MIDDLE_FIELD_NEAR_SHOOT(
        new VisionAlign(),
        new Forward(1.0)
    );

    public final Action[] actionArray;

    private AutoConfigs(Action... actionArray) {
        this.actionArray = actionArray;
    }
}