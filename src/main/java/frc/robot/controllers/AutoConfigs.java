package frc.robot.controllers;

import frc.robot.actions.*;

public enum AutoConfigs {
    AUTO_1(),
    AUTO_2();

    public final Action[] actionArray;

    private AutoConfigs(Action... actionArray) {
        this.actionArray = actionArray;
    }
}