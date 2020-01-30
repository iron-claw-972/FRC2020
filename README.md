# FRC2020

This is the 2020 repository of the FRC team 972 Ironclaw.

The project board for this repository is located [here](https://github.com/iron-claw-972/FRC2020/projects/1). Feel free to assign yourself to tasks and to complete them.

# Code Structure

Overall the code structure is very basic and may be in some need of revising. Under ~\src\main\java\frc\robot there are two directories, described below. In addion there are two files, robot.java and main.java, which control robot functions at the highest level.

## Controllers

Pretty much everything that controls a specific action that the robot can take, this includes the conventional subsystems.

## Util

All of the general purpose classes that are used throughout the robot code.

## Online library import links

Do `ctrl-shift-p` to open the quick actions bar, then select manage vendor libraries. From there select `install new library (online)` and paste the urls below before pressing `ok`.

### Spark MAX:

http://www.revrobotics.com/content/sw/max/sdk/REVRobotics.json

### NavX

https://www.kauailabs.com/dist/frc/2020/navx_frc.json

### Talon SRX

http://devsite.ctr-electronics.com/maven/release/com/ctre/phoenix/Phoenix-latest.json


### TASKS:
- [ ] Shooter
   - [ ] Visison Allignment
    - [ ] Mount Camera
    - [ ] Get Distance Sensor
      - [ ] Find/Calculate optimal distance and speed of rotation
- [ ] Delta Timer for PID

### Google Java Style Guide
https://google.github.io/styleguide/javaguide.html
