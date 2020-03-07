package frc.robot.controllers;

import frc.robot.actions.*;
import frc.robot.actions.auto.*;

public enum AutoConfigs {

    POWER_PORT_BASIC(
        new VisionAlign(),
        new Forward(1.0)
    ),
    ALLIANCE_TRENCH_BASIC(
        new VisionAlign(),
        new Forward(1.0)
    ),
    MIDDLE_FIELD_BASIC(
        new VisionAlign(),
        new Forward(1.0)
    );

    public final Action[] actionArray;

    private AutoConfigs(Action... actionArray) {
        this.actionArray = actionArray;
    }
}