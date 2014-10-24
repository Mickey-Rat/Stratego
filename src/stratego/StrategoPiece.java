/*Authors: Kyle Clark, Andrew Gladkowski, Brian Sangster*/
package stratego;
import java.awt.*;
/*This class will be responsible for the pieces, their values, and their classes(no atributes).*/
public class StrategoPiece {
    private Color color;
    StrategoPiece(Color _color)
    {
        color = _color;
    }
    public void setColor(Color _color)
    {
        color=_color;
    }
    Color getColor()
    {
        return (color);
    }
}
