package frc.robot.shuffleboard;

import edu.wpi.first.shuffleboard.api.plugin.*;
import edu.wpi.first.shuffleboard.api.data.*;
import edu.wpi.first.shuffleboard.api.widget.ComponentType;
import edu.wpi.first.shuffleboard.api.theme.*;

import java.util.*;

@Requires(group = "edu.wpi.first.shuffleboard", name = "Base", minVersion = "1.0.0")
@Description(
    group = "frc.robot", 
    name = "CustomPlugin", 
    version = "1.2.3", 
    summary = "A good plugin"
)
public class CustomPlugin extends Plugin {

    private static final Theme customTheme = new Theme(CustomPlugin.class, "Iron Claw Theme", "/path/to/stylesheet");

    @Override
    public List<ComponentType> getComponents() {
        return List.of(

        );
    }

    public List<Theme> getThemes() {
        return List.of(customTheme);
    }

}