/*Authors: Kyle Clark, Andrew Gladkowski, Brian Sangster*/
package stratego;
import java.awt.*;
/*This class will be responsible for the pieces, their values, and their classes(no atributes).*/
public class StrategoPiece {
    private Color color;
    private StrategoMovePiece unit = new StrategoMovePiece();
    private boolean pickUp; 
    StrategoPiece(Color _color, int _unitNum)
    {
        color = _color;
        unit.setValue(_unitNum);
        unit.setColor(color);
        pickUp = false;
    }
    public void setColor(Color _color)
    {
        color=_color;
    }
    public void setValue(int _value)
    {
        unit.setValue(_value);
    }
    public Color getColor()
    {
        return (color);
    }
    public int getValue(){
        return(unit.getValue());
    }
    public void setPickedUp(boolean _pickUp){
        pickUp=_pickUp;
    }
    public boolean getPickedUp(){
        return(pickUp);
    }
}
