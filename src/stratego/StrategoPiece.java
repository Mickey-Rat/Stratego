/*Authors: Kyle Clark, Andrew Gladkowski, Brian Sangster*/
package stratego;
import java.awt.*;
/*This class will be responsible for the pieces, their values, and their classes(no atributes).*/
public class StrategoPiece {
    private Color color;
    private int val;
    private boolean pickUp; 
    private boolean HasFlag;
    StrategoPiece(Color _color, int _unitNum)
    {
        color = _color;
        val = _unitNum;
        pickUp = false;
    }
    public void setColor(Color _color)
    {
        color=_color;
    }
    public void setValue(int _value)
    {
        val = _value;
    }
    public Color getColor()
    {
        return (color);
    }
    public int getValue(){
        return(val);
    }
    public void setPickedUp(boolean _pickUp){
        pickUp=_pickUp;
    }
    public boolean getPickedUp(){
        return(pickUp);
    }
    public void setHasFlag(boolean _hasflag){
        HasFlag=_hasflag;
    }
    public boolean getHasFlag(){
        return(HasFlag);
    }
}
