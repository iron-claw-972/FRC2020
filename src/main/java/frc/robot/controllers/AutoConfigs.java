package frc.robot.controllers;

import frc.robot.actions.*;
import frc.robot.actions.auto.*;

public enum AutoConfigs {
    /*
    CENTER: On initiation line directly in front of power port
    ALLIANCE_TRENCH: On initiation line in line with the center of your alliance's trench
    OPPONENT_TRENCH: On initiation line in line with the center of your opponent's trench

    BASIC: SHOOT 3 BALLS, then move off initiation line
    */

    CENTER_BASIC(
        //new VisionAlign(),
        new Forward(1.0)
    );

    public final Action[] actionArray;

    private AutoConfigs(Action... actionArray) {
        this.actionArray = actionArray;
    }
}