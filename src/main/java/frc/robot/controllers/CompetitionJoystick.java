package frc.robot.controllers;

public interface CompetitionJoystick {
    public abstract boolean getButtonPressed(int id);

    public abstract boolean getButtonReleased(int id);

    public abstract boolean getAxisPressed(int id);

    public abstract boolean getAxisReleased(int id);

    public abstract boolean getDpadPressed(int id);

    public abstract boolean getDpadReleased(int id);
}