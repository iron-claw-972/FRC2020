package frc.robot.controllers;

public interface CompetitionJoystick {
    public abstract boolean getButtonPressed(int id);

    public abstract boolean getAxisPressed(int id);
}