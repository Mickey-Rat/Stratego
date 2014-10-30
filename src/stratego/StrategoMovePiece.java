/*Authors: Kyle Clark, Andrew Gladkowski, Brian Sangster*/
package stratego;
import java.awt.*;
/*This class will be responsible for moving the piece(Animate), showing possible moves, and holding the atributes of each piece.*/
public class StrategoMovePiece {
    private final int defaultNum=8;
    private int unitNum;
    private Color color;
    StrategoMovePiece(int _unitNum, Color _color){
        unitNum=_unitNum;
        color=_color;
    }
    StrategoMovePiece(){
        unitNum=defaultNum;
    }
    public void setValue(int _val){
        unitNum=_val;
    }
    public int getValue(){
        return(unitNum);
    }
    public void setColor(Color _color){
        color = _color;
    }
}
