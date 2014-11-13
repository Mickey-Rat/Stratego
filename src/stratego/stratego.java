/*Authors: Kyle Clark, Andrew Gladkowski, Brian Sangster*/
package stratego;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JFrame;

import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.GradientPaint;

public class stratego extends JFrame implements Runnable {
    static final int XBORDER = 20;
    static final int YBORDER = 20;
    static final int YTITLE = 25;
    static final int WINDOW_WIDTH = 1040;
    static final int WINDOW_HEIGHT = 1040;    
    boolean animateFirstTime = true;
    int xsize = -1;
    int ysize = -1;
    Image image;
    Graphics2D g;
    static Image icon = Toolkit.getDefaultToolkit().getImage("./flag.GIF");
    static Image bg = Toolkit.getDefaultToolkit().getImage("./board 2.PNG");
    static Image sc = Toolkit.getDefaultToolkit().getImage("./Start Screen.PNG");
    static Image helpBackround = Toolkit.getDefaultToolkit().getImage("./pigeonexplosion.GIF");
    static Image helpMenu = Toolkit.getDefaultToolkit().getImage("./help menu.GIF");
    
    boolean player1Turn;
    boolean lastTurn;
    final static int NUM_ROWS = 10;
    final static int NUM_COLUMNS = 10;  
    StrategoPiece board[][] = new StrategoPiece[NUM_ROWS][NUM_COLUMNS];
    boolean firstRedTurn;
    boolean firstBluTurn;
    
    //unit values
    final int numUnits = 11;
    final int UnitValue[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    final int Commander = 1;
    final int General = 2;
    final int Leutenant = 3;
    final int Captain = 4;
    final int Sergant = 5;
    final int Gunny = 6;
    final int Scout = 7;
    final int Miner = 8;
    final int Spy = 9;
    final int Bomb = 10;
    final int Flag = 11;
    int lastRow;
    int lastCol;
    int timer;
    int timeCount;
    int font;
    
    //red units
    Image redCommander;
    Image redGeneral;
    Image redLeutenant;
    Image redCaptain;
    Image redSergant;
    Image redGunny;
    Image redScout;
    Image redMiner;
    Image redSpy;
    Image redBomb;
    Image redFlag;
    Image redTemplate;
    int redFlagzi;
    int redFlagzx;
    
    //blu units
    Image bluCommander;
    Image bluGeneral;
    Image bluLeutenant;
    Image bluCaptain;
    Image bluSergant;
    Image bluGunny;
    Image bluScout;
    Image bluMiner;
    Image bluSpy;
    Image bluBomb;
    Image bluFlag;
    Image bluTemplate;
    int bluFlagzi;
    int bluFlagzx;
    
    //soundsand stuff
    sound bgsound=null;
    boolean start;
    boolean help;
    boolean pause;
    
    
    public static void main(String[] args) {
        stratego frame = new stratego();
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setTitle("Andvance and Conquer");
        frame.setIconImage(icon);
        frame.setResizable(false);
    }

    public stratego() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                
   
                if (e.BUTTON1 == e.getButton() ) {
                    if(player1Turn!=lastTurn){
                        return;
                    }
                    
//Calculate the width and height of each board square.
                    int ydelta = getHeight2()/NUM_ROWS;
                    int xdelta = getWidth2()/NUM_COLUMNS;

                    int xpos = e.getX();
                    int ypos = e.getY();
                    
//Check to make sure we have selected within the space of the board.
                    if (xpos < getX(0) || xpos > getX(getWidth2()) ||
                        ypos < getY(0) || ypos > getY(getHeight2()))
                        return;
                    
//Determine which column has been selected.
                    int zcol = 0;
                    boolean keepGoing = true;
                    while (keepGoing)
                    {
                        if (xpos < getX((zcol+1)*xdelta))
                        {
                            keepGoing = false;
                        }   
                      else
                            zcol++;
                    
                    }
                    int zrow = 0;
                    boolean loop = true;
                    while (loop)
                    {
                        if (ypos < getY((zrow+1)*ydelta))
                        {
                            loop = false;
                        }   
                      else
                            zrow++;
                    
                    }
                    if(!start){
                        if(board[lastRow][lastCol]!=null)
                        {
                            if(board[zrow][zcol]==null || board[zrow][zcol].getColor() != board[lastRow][lastCol].getColor()){
                                if(board[lastRow][lastCol].getValue() == Scout){
                                    if(((zrow != lastRow && zcol == lastCol) || (zrow == lastRow && zcol != lastCol))){
                                        int check;
                                         int tempCheck;
                                        if(zrow < lastRow){
                                            check = lastRow - zrow;
                                            tempCheck = 0;
                                            for(int index = lastRow - 1;index < lastRow && index > zrow;index --){
                                                if(board[index][zcol] == null){
                                                    tempCheck ++;
                                                }
                                            }
                                            if(tempCheck == check - 1){
                                                    if(board[zrow][zcol]!=null && (board[zrow][zcol].getValue() != board[lastRow][lastCol].getValue())){

                                                    if(board[zrow][zcol].getColor() != board[lastRow][lastCol].getColor() && board[zrow][zcol].getValue() == Flag && board[zrow][zcol].getValue()!=Bomb){
                                                        board[zrow][zcol] = board[lastRow][lastCol];
                                                        board[lastRow][lastCol] = null;
                                                        lastRow=0;
                                                        lastCol=0;
                                                        player1Turn=!player1Turn;
                                                        board[zrow][zcol].setPickedUp(false);
                                                        board[zrow][zcol].setHasFlag(true);
                                                    }
                                                    else if(board[zrow][zcol].getValue() > board[lastRow][lastCol].getValue() && board[zrow][zcol].getValue()!=Bomb){
                                                        if(board[zrow][zcol].getHasFlag())
                                                        {
                                                            if(board[zrow][zcol].getColor() == Color.red)
                                                                board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                            if(board[zrow][zcol].getColor() == Color.blue)
                                                                board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                        }
                                                        board[zrow][zcol] = board[lastRow][lastCol];
                                                        board[lastRow][lastCol] = null;
                                                        lastRow=0;
                                                        lastCol=0;
                                                        player1Turn=!player1Turn;
                                                        board[zrow][zcol].setPickedUp(false);
                                                    }
                                                    else if(board[zrow][zcol].getValue()<board[lastRow][lastCol].getValue()){
                                                        if(board[zrow][zcol].getHasFlag())
                                                        {
                                                            if(board[zrow][zcol].getColor() == Color.red)
                                                                board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                            if(board[zrow][zcol].getColor() == Color.blue)
                                                                board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                        }
                                                        board[lastRow][lastCol] = null; 
                                                        lastRow=0;
                                                        lastCol=0;
                                                        player1Turn=!player1Turn;
                                                        board[zrow][zcol].setPickedUp(false);
                                                    }
                                                    else if(board[zrow][zcol].getValue()==Bomb && board[lastRow][lastCol].getValue()!=Miner){
                                                        board[lastRow][lastCol] = null; 
                                                        lastRow=0;
                                                        lastCol=0;
                                                        player1Turn=!player1Turn;
                                                        board[zrow][zcol].setPickedUp(false);
                                                    }


                                                }
                                                else if(board[zrow][zcol]!=null && board[zrow][zcol].getValue()==board[lastRow][lastCol].getValue()){
                                                    if(board[zrow][zcol].getHasFlag() || board[lastRow][lastCol].getHasFlag())
                                                    {
                                                        if(board[zrow][zcol].getColor() == Color.red)
                                                            board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                        if(board[zrow][zcol].getColor() == Color.blue)
                                                            board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                        if(board[lastRow][lastCol].getColor() == Color.red)
                                                            board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                        if(board[lastRow][lastCol].getColor() == Color.blue)
                                                            board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                    }
                                                        board[lastRow][lastCol] = null; 
                                                        board[zrow][zcol]=null;
                                                        lastRow=0;
                                                        lastCol=0;
                                                        player1Turn=!player1Turn;
                                                        board[zrow][zcol].setPickedUp(false);
                                                    }
                                                else{
                                                    if(board[zrow][zcol].getHasFlag())
                                                    {
                                                        if(board[zrow][zcol].getColor() == Color.red)
                                                            board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                        if(board[zrow][zcol].getColor() == Color.blue)
                                                            board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                    }
                                                    
                                                    board[zrow][zcol] = board[lastRow][lastCol];
                                                    board[lastRow][lastCol] = null;
                                                    lastRow=0;
                                                    lastCol=0;
                                                    player1Turn=!player1Turn; 
                                                    board[zrow][zcol].setPickedUp(false);
                                                }
                                            }
                                        }
                                        else if(zrow > lastRow){
                                            check = zrow - lastRow;
                                            tempCheck = 0;
                                            for(int index = lastRow + 1;index > lastRow && index < zrow;index ++){
                                                if(board[index][zcol] == null){
                                                    tempCheck ++;
                                                }
                                            }
                                            if(tempCheck == check - 1){
                                                if(board[zrow][zcol]!=null && (board[zrow][zcol].getValue() != board[lastRow][lastCol].getValue())){

                                                    if(board[zrow][zcol].getColor() != board[lastRow][lastCol].getColor() && board[zrow][zcol].getValue() == Flag && board[zrow][zcol].getValue()!=Bomb){
                                                        board[zrow][zcol] = board[lastRow][lastCol];
                                                        board[lastRow][lastCol] = null;
                                                        lastRow=0;
                                                        lastCol=0;
                                                        player1Turn=!player1Turn;
                                                        board[zrow][zcol].setPickedUp(false);
                                                        board[zrow][zcol].setHasFlag(true);
                                                    }
                                                    else if(board[zrow][zcol].getValue() > board[lastRow][lastCol].getValue() && board[zrow][zcol].getValue()!=Bomb){
                                                        if(board[zrow][zcol].getHasFlag())
                                                        {
                                                            if(board[zrow][zcol].getColor() == Color.red)
                                                                board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                            if(board[zrow][zcol].getColor() == Color.blue)
                                                                board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                        }
                                                        board[zrow][zcol] = board[lastRow][lastCol];
                                                        board[lastRow][lastCol] = null;
                                                        lastRow=0;
                                                        lastCol=0;
                                                        player1Turn=!player1Turn;
                                                        board[zrow][zcol].setPickedUp(false);
                                                    }
                                                    else if(board[zrow][zcol].getValue()<board[lastRow][lastCol].getValue()){
                                                        if(board[zrow][zcol].getHasFlag())
                                                        {
                                                            if(board[zrow][zcol].getColor() == Color.red)
                                                                board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                            if(board[zrow][zcol].getColor() == Color.blue)
                                                                board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                        }
                                                        board[lastRow][lastCol] = null; 
                                                        lastRow=0;
                                                        lastCol=0;
                                                        player1Turn=!player1Turn;
                                                        board[zrow][zcol].setPickedUp(false);
                                                    }
                                                    else if(board[zrow][zcol].getValue()==Bomb && board[lastRow][lastCol].getValue()!=Miner){
                                                        if(board[zrow][zcol].getHasFlag())
                                                        {
                                                            if(board[zrow][zcol].getColor() == Color.red)
                                                                board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                            if(board[zrow][zcol].getColor() == Color.blue)
                                                                board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                        }
                                                        board[lastRow][lastCol] = null; 
                                                        lastRow=0;
                                                        lastCol=0;
                                                        player1Turn=!player1Turn;
                                                        board[zrow][zcol].setPickedUp(false);
                                                    }


                                                }
                                                else if(board[zrow][zcol]!=null && board[zrow][zcol].getValue()==board[lastRow][lastCol].getValue()){
                                                        if(board[zrow][zcol].getHasFlag() || board[lastRow][lastCol].getHasFlag())
                                                    {
                                                        if(board[zrow][zcol].getColor() == Color.red)
                                                            board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                        if(board[zrow][zcol].getColor() == Color.blue)
                                                            board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                        if(board[lastRow][lastCol].getColor() == Color.red)
                                                            board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                        if(board[lastRow][lastCol].getColor() == Color.blue)
                                                            board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                    }
                                                        board[lastRow][lastCol] = null; 
                                                        board[zrow][zcol]=null;
                                                        lastRow=0;
                                                        lastCol=0;
                                                        player1Turn=!player1Turn;
                                                        board[zrow][zcol].setPickedUp(false);
                                                    }
                                                else{
                                                    if(board[zrow][zcol].getHasFlag())
                                                    {
                                                        if(board[zrow][zcol].getColor() == Color.red)
                                                            board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                        if(board[zrow][zcol].getColor() == Color.blue)
                                                            board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                    }
                                                    board[zrow][zcol] = board[lastRow][lastCol];
                                                    board[lastRow][lastCol] = null;
                                                    lastRow=0;
                                                    lastCol=0;
                                                    player1Turn=!player1Turn; 
                                                    board[zrow][zcol].setPickedUp(false);
                                                }
                                            }
                                        }
                                        else if(zcol < lastCol){
                                            check = lastCol - zcol;
                                            tempCheck = 0;
                                            for(int index = lastCol - 1;index < lastCol && index > zcol;index --){
                                                if(board[zrow][index] == null){
                                                    tempCheck ++;
                                                }
                                            }
                                            if(tempCheck == check - 1){
                                                if(board[zrow][zcol]!=null && (board[zrow][zcol].getValue() != board[lastRow][lastCol].getValue())){

                                                    if(board[zrow][zcol].getColor() != board[lastRow][lastCol].getColor() && board[zrow][zcol].getValue() == Flag && board[zrow][zcol].getValue()!=Bomb){
                                                        board[zrow][zcol] = board[lastRow][lastCol];
                                                        board[lastRow][lastCol] = null;
                                                        lastRow=0;
                                                        lastCol=0;
                                                        player1Turn=!player1Turn;
                                                        board[zrow][zcol].setPickedUp(false);
                                                        board[zrow][zcol].setHasFlag(true);
                                                    }
                                                    else if(board[zrow][zcol].getValue() > board[lastRow][lastCol].getValue() && board[zrow][zcol].getValue()!=Bomb){
                                                        if(board[zrow][zcol].getHasFlag())
                                                        {
                                                            if(board[zrow][zcol].getColor() == Color.red)
                                                                board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                            if(board[zrow][zcol].getColor() == Color.blue)
                                                                board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                        }
                                                        board[zrow][zcol] = board[lastRow][lastCol];
                                                        board[lastRow][lastCol] = null;
                                                        lastRow=0;
                                                        lastCol=0;
                                                        player1Turn=!player1Turn;
                                                        board[zrow][zcol].setPickedUp(false);
                                                    }
                                                    else if(board[zrow][zcol].getValue()<board[lastRow][lastCol].getValue()){
                                                        if(board[zrow][zcol].getHasFlag())
                                                        {
                                                            if(board[zrow][zcol].getColor() == Color.red)
                                                                board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                            if(board[zrow][zcol].getColor() == Color.blue)
                                                                board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                        }
                                                        board[lastRow][lastCol] = null; 
                                                        lastRow=0;
                                                        lastCol=0;
                                                        player1Turn=!player1Turn;
                                                        board[zrow][zcol].setPickedUp(false);
                                                    }
                                                    else if(board[zrow][zcol].getValue()==Bomb && board[lastRow][lastCol].getValue()!=Miner){
                                                        if(board[zrow][zcol].getHasFlag())
                                                        {
                                                            if(board[zrow][zcol].getColor() == Color.red)
                                                                board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                            if(board[zrow][zcol].getColor() == Color.blue)
                                                                board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                        }
                                                        board[lastRow][lastCol] = null; 
                                                        lastRow=0;
                                                        lastCol=0;
                                                        player1Turn=!player1Turn;
                                                        board[zrow][zcol].setPickedUp(false);
                                                    }


                                                }
                                                else if(board[zrow][zcol]!=null && board[zrow][zcol].getValue()==board[lastRow][lastCol].getValue()){
                                                    if(board[zrow][zcol].getHasFlag() || board[lastRow][lastCol].getHasFlag())
                                                    {
                                                        if(board[zrow][zcol].getColor() == Color.red)
                                                            board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                        if(board[zrow][zcol].getColor() == Color.blue)
                                                            board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                        if(board[lastRow][lastCol].getColor() == Color.red)
                                                            board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                        if(board[lastRow][lastCol].getColor() == Color.blue)
                                                            board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                    }
                                                        board[lastRow][lastCol] = null; 
                                                        board[zrow][zcol]=null;
                                                        lastRow=0;
                                                        lastCol=0;
                                                        player1Turn=!player1Turn;
                                                        board[zrow][zcol].setPickedUp(false);
                                                    }
                                                else{
                                                    if(board[zrow][zcol].getHasFlag())
                                                    {
                                                        if(board[zrow][zcol].getColor() == Color.red)
                                                            board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                        if(board[zrow][zcol].getColor() == Color.blue)
                                                            board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                    }
                                                    board[zrow][zcol] = board[lastRow][lastCol];
                                                    board[lastRow][lastCol] = null;
                                                    lastRow=0;
                                                    lastCol=0;
                                                    player1Turn=!player1Turn;
                                                    board[zrow][zcol].setPickedUp(false);
                                                }
                                            }
                                        }
                                        else if(zcol > lastCol){
                                            check = zcol - lastCol;
                                            tempCheck = 0;
                                            for(int index = lastCol + 1;index > lastCol && index < zcol;index ++){
                                                if(board[zrow][index] == null){
                                                    tempCheck ++;
                                                }
                                            }
                                            if(tempCheck == check - 1){
                                                if(board[zrow][zcol]!=null && (board[zrow][zcol].getValue() != board[lastRow][lastCol].getValue())){

                                                    if(board[zrow][zcol].getColor() != board[lastRow][lastCol].getColor() && board[zrow][zcol].getValue() == Flag && board[zrow][zcol].getValue()!=Bomb){
                                                        board[zrow][zcol] = board[lastRow][lastCol];
                                                        board[lastRow][lastCol] = null;
                                                        lastRow=0;
                                                        lastCol=0;
                                                        player1Turn=!player1Turn;
                                                        board[zrow][zcol].setPickedUp(false);
                                                        board[zrow][zcol].setHasFlag(true);
                                                    }
                                                    else if(board[zrow][zcol].getValue() > board[lastRow][lastCol].getValue() && board[zrow][zcol].getValue()!=Bomb){
                                                        if(board[zrow][zcol].getHasFlag())
                                                        {
                                                            if(board[zrow][zcol].getColor() == Color.red)
                                                                board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                            if(board[zrow][zcol].getColor() == Color.blue)
                                                                board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                        }
                                                        board[zrow][zcol] = board[lastRow][lastCol];
                                                        board[lastRow][lastCol] = null;
                                                        lastRow=0;
                                                        lastCol=0;
                                                        player1Turn=!player1Turn;
                                                        board[zrow][zcol].setPickedUp(false);
                                                    }
                                                    else if(board[zrow][zcol].getValue()<board[lastRow][lastCol].getValue()){
                                                        if(board[zrow][zcol].getHasFlag())
                                                        {
                                                            if(board[zrow][zcol].getColor() == Color.red)
                                                                board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                            if(board[zrow][zcol].getColor() == Color.blue)
                                                                board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                        }
                                                        board[lastRow][lastCol] = null; 
                                                        lastRow=0;
                                                        lastCol=0;
                                                        player1Turn=!player1Turn;
                                                        board[zrow][zcol].setPickedUp(false);
                                                    }
                                                    else if(board[zrow][zcol].getValue()==Bomb && board[lastRow][lastCol].getValue()!=Miner){
                                                        if(board[zrow][zcol].getHasFlag())
                                                        {
                                                            if(board[zrow][zcol].getColor() == Color.red)
                                                                board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                            if(board[zrow][zcol].getColor() == Color.blue)
                                                                board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                        }
                                                        board[lastRow][lastCol] = null; 
                                                        lastRow=0;
                                                        lastCol=0;
                                                        player1Turn=!player1Turn;
                                                        board[zrow][zcol].setPickedUp(false);
                                                    }


                                                }
                                                else if(board[zrow][zcol]!=null && board[zrow][zcol].getValue()==board[lastRow][lastCol].getValue()){
                                                    if(board[zrow][zcol].getHasFlag() || board[lastRow][lastCol].getHasFlag())
                                                    {
                                                        if(board[zrow][zcol].getColor() == Color.red)
                                                            board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                        if(board[zrow][zcol].getColor() == Color.blue)
                                                            board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                        if(board[lastRow][lastCol].getColor() == Color.red)
                                                            board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                        if(board[lastRow][lastCol].getColor() == Color.blue)
                                                            board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                    }
                                                        board[lastRow][lastCol] = null; 
                                                        board[zrow][zcol]=null;
                                                        lastRow=0;
                                                        lastCol=0;
                                                        player1Turn=!player1Turn;
                                                        board[zrow][zcol].setPickedUp(false);
                                                    }
                                                else{
                                                    if(board[zrow][zcol].getHasFlag())
                                                        {
                                                            if(board[zrow][zcol].getColor() == Color.red)
                                                                board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                            if(board[zrow][zcol].getColor() == Color.blue)
                                                                board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                        }
                                                    board[zrow][zcol] = board[lastRow][lastCol];
                                                    board[lastRow][lastCol] = null;
                                                    lastRow=0;
                                                    lastCol=0;
                                                    player1Turn=!player1Turn; 
                                                    board[zrow][zcol].setPickedUp(false);
                                                }
                                            }
                                        }
                                    }
                                }
                                else if(((zrow == lastRow + 1 || zrow == lastRow - 1) && zcol == lastCol) || ((zcol == lastCol + 1 || zcol == lastCol - 1) && zrow == lastRow)){
                                    if(board[zrow][zcol]!=null && (board[zrow][zcol].getValue() != board[lastRow][lastCol].getValue())){
                                        if(board[zrow][zcol].getColor() != board[lastRow][lastCol].getColor() && board[zrow][zcol].getValue() == Flag && board[zrow][zcol].getValue()!=Bomb){
                                            board[zrow][zcol] = board[lastRow][lastCol];
                                            board[lastRow][lastCol] = null;
                                            lastRow=0;
                                            lastCol=0;
                                            player1Turn=!player1Turn;
                                            board[zrow][zcol].setPickedUp(false);
                                            board[zrow][zcol].setHasFlag(true);
                                        }
                                        else if(board[zrow][zcol].getValue()==Commander && board[lastRow][lastCol].getValue()==Spy){
                                            if(board[zrow][zcol].getHasFlag())
                                            {
                                                if(board[zrow][zcol].getColor() == Color.red)
                                                    board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                if(board[zrow][zcol].getColor() == Color.blue)
                                                    board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                            }
                                            board[zrow][zcol] = board[lastRow][lastCol];
                                            board[lastRow][lastCol] = null;
                                            lastRow=0;
                                            lastCol=0;
                                            player1Turn=!player1Turn;
                                            board[zrow][zcol].setPickedUp(false);
                                        }
                                        else if(board[zrow][zcol].getValue() > board[lastRow][lastCol].getValue() && board[zrow][zcol].getValue()!=Bomb){
                                            if(board[zrow][zcol].getHasFlag())
                                            {
                                                if(board[zrow][zcol].getColor() == Color.red)
                                                    board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                if(board[zrow][zcol].getColor() == Color.blue)
                                                    board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                            }
                                            board[zrow][zcol] = board[lastRow][lastCol];
                                            board[lastRow][lastCol] = null;
                                            lastRow=0;
                                            lastCol=0;
                                            player1Turn=!player1Turn;
                                            board[zrow][zcol].setPickedUp(false);
                                        }
                                        else if(board[zrow][zcol].getValue()==Bomb && board[lastRow][lastCol].getValue()==Miner){
                                            if(board[zrow][zcol].getHasFlag())
                                            {
                                                if(board[zrow][zcol].getColor() == Color.red)
                                                    board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                if(board[zrow][zcol].getColor() == Color.blue)
                                                    board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                            }
                                            board[zrow][zcol] = board[lastRow][lastCol];
                                            board[lastRow][lastCol] = null;
                                            lastRow=0;
                                            lastCol=0;
                                            player1Turn=!player1Turn;
                                            board[zrow][zcol].setPickedUp(false);
                                        }
                                        else if(board[zrow][zcol]!=null && board[zrow][zcol].getValue()<board[lastRow][lastCol].getValue()){
                                            if(board[zrow][zcol].getHasFlag())
                                            {
                                                if(board[zrow][zcol].getColor() == Color.red)
                                                    board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                if(board[zrow][zcol].getColor() == Color.blue)
                                                    board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                            }
                                            board[lastRow][lastCol] = null; 
                                            lastRow=0;
                                            lastCol=0;
                                            player1Turn=!player1Turn;
                                            board[zrow][zcol].setPickedUp(false);
                                        }
                                        else if(board[zrow][zcol].getValue()==Bomb && board[lastRow][lastCol].getValue()!=Miner){
                                            if(board[zrow][zcol].getHasFlag())
                                            {
                                                if(board[zrow][zcol].getColor() == Color.red)
                                                    board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                if(board[zrow][zcol].getColor() == Color.blue)
                                                    board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                            }
                                            board[lastRow][lastCol] = null; 
                                            lastRow=0;
                                            lastCol=0;
                                            player1Turn=!player1Turn;
                                            board[zrow][zcol].setPickedUp(false);
                                        }


                                    }
                                    else if(board[zrow][zcol]!=null && board[zrow][zcol].getValue()==board[lastRow][lastCol].getValue()){
                                        if(board[zrow][zcol].getHasFlag() || board[lastRow][lastCol].getHasFlag())
                                            {
                                                if(board[zrow][zcol].getColor() == Color.red)
                                                    board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                if(board[zrow][zcol].getColor() == Color.blue)
                                                    board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                                if(board[lastRow][lastCol].getColor() == Color.red)
                                                    board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                                if(board[lastRow][lastCol].getColor() == Color.blue)
                                                    board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                            }
                                            board[lastRow][lastCol] = null; 
                                            board[zrow][zcol]=null;
                                            lastRow=0;
                                            lastCol=0;
                                            player1Turn=!player1Turn;
                                            board[zrow][zcol].setPickedUp(false);
                                        }
                                    else{
                                        if(board[zrow][zcol].getHasFlag())
                                        {
                                            if(board[zrow][zcol].getColor() == Color.red)
                                                board[bluFlagzi][bluFlagzx] = new StrategoPiece(Color.blue,Flag);
                                            if(board[zrow][zcol].getColor() == Color.blue)
                                                board[redFlagzi][redFlagzx] = new StrategoPiece(Color.red,Flag);
                                        }
                                        board[zrow][zcol] = board[lastRow][lastCol];
                                        board[lastRow][lastCol] = null;
                                        lastRow=0;
                                        lastCol=0;
                                        player1Turn=!player1Turn; 
                                        board[zrow][zcol].setPickedUp(false);
                                    }
                                }
                            }
                        }

                        System.out.println("zcol: "+zcol);
                        System.out.println("zrow: "+zrow);


                        if(board[zrow][zcol]!=null && board[zrow][zcol].getColor() == Color.red && player1Turn && board[zrow][zcol].getValue() != Bomb)
                        {
                            board[zrow][zcol].setPickedUp(true);
                            lastCol = zcol;
                            lastRow = zrow;
                        }
                        if(board[zrow][zcol]!=null && board[zrow][zcol].getColor() == Color.blue && !player1Turn && board[zrow][zcol].getValue() != Bomb)
                        {
                            board[zrow][zcol].setPickedUp(true);
                            lastCol = zcol;
                            lastRow = zrow;
                        }

                        if(player1Turn&&firstRedTurn){
                            if(zrow==0){
                                board[zrow][zcol] = new StrategoPiece(Color.red,Flag);
                                firstRedTurn=!firstRedTurn;
                                REDreset();
                                player1Turn=!player1Turn;
                                return;
                            }
                        }
                        if(!player1Turn&&firstBluTurn){
                            if(zrow==NUM_ROWS-1){
                                board[zrow][zcol] = new StrategoPiece(Color.blue,Flag);
                                firstBluTurn=!firstBluTurn;
                                BLUreset();
                                player1Turn=!player1Turn;
                                return;
                            }
                        }

                    }
                }

                if (e.BUTTON3 == e.getButton()) {

                }
                repaint();
            }
        });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
                    
        repaint();
      }
    });

    addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {
        repaint();
      }
    });

        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if (e.VK_UP == e.getKeyCode()) {
                } else if (e.VK_DOWN == e.getKeyCode()) {
                } else if (e.VK_LEFT == e.getKeyCode()) {
                } else if (e.VK_RIGHT == e.getKeyCode()) {
                }else if (e.VK_H == e.getKeyCode() && (start || pause)) {
                    help=!help;
                }else if (e.VK_S == e.getKeyCode() && start && !help) {
                    start=!start;
                }else if (e.VK_P == e.getKeyCode() && !start) {
                    pause = !pause;
                    if(help){
                        help=!help;
                    }
                } else if (e.VK_ESCAPE == e.getKeyCode()) {
                    reset();
                }
                repaint();
            }
        });
        init();
        start();
    }
    Thread relaxer;
////////////////////////////////////////////////////////////////////////////
    public void init() {
        requestFocus();
    }
////////////////////////////////////////////////////////////////////////////
    public void destroy() {
    }
////////////////////////////////////////////////////////////////////////////
    public void paint(Graphics gOld) {
        if (image == null || xsize != getSize().width || ysize != getSize().height) {
            xsize = getSize().width;
            ysize = getSize().height;
            image = createImage(xsize, ysize);
            g = (Graphics2D) image.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
//fill background
        
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, xsize, ysize);

        int x[] = {getX(0), getX(getWidth2()), getX(getWidth2()), getX(0), getX(0)};
        int y[] = {getY(0), getY(0), getY(getHeight2()), getY(getHeight2()), getY(0)};
//fill border
        g.setColor(Color.lightGray);
        g.fillPolygon(x, y, 4);
// draw border
        g.setColor(Color.black);
        g.drawPolyline(x, y, 5);

        if (animateFirstTime) {
            gOld.drawImage(image, 0, 0, null);
            return;
        }
        
//Calculate the width and height of each board square.
        int ydelta = getHeight2()/NUM_ROWS;
        int xdelta = getWidth2()/NUM_COLUMNS;
//draw board
        if(!start){
            if(!pause){
                g.drawImage(bg,getX(0),getY(0),
                        getWidth2(),getHeight2(),this);

         //draw grid
                g.setColor(Color.black);
                for (int zi = 1;zi<NUM_ROWS;zi++)
                {
                    g.drawLine(getX(0),getY(zi*ydelta),
                            getX(getWidth2()),getY(zi*ydelta));
                }

                for (int zi = 1;zi<NUM_COLUMNS;zi++)
                {
                    g.drawLine(getX(zi*xdelta),getY(0),
                            getX(zi*xdelta),getY(getHeight2()));
                }


        //Draw the piece.
                for (int zi = 0; zi < NUM_ROWS; zi++)
                {
                    for (int zx = 0; zx < NUM_COLUMNS; zx++)
                    {
                        if (board[zi][zx]!=null){
                            if(player1Turn==lastTurn){
                                if(board[zi][zx].getPickedUp()){
                                    g.setColor(Color.yellow);
                                    g.fillRect(getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta);
                                }
                                if(player1Turn){
                                //red drawings
                                if (board[zi][zx].getValue() == Commander && board[zi][zx].getColor() == Color.red)
                                    g.drawImage(redCommander,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                if (board[zi][zx].getValue() == General && board[zi][zx].getColor() == Color.red)
                                    g.drawImage(redGeneral,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                if (board[zi][zx].getValue() == Leutenant && board[zi][zx].getColor() == Color.red)
                                    g.drawImage(redLeutenant,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                if (board[zi][zx].getValue() == Captain && board[zi][zx].getColor() == Color.red)
                                    g.drawImage(redCaptain,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                if (board[zi][zx].getValue() == Sergant && board[zi][zx].getColor() == Color.red)
                                    g.drawImage(redSergant,getX(zx * xdelta), getY(zi * ydelta),xdelta, ydelta, this);
                                if (board[zi][zx].getValue() == Gunny && board[zi][zx].getColor() == Color.red)
                                    g.drawImage(redGunny,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                if (board[zi][zx].getValue() == Scout && board[zi][zx].getColor() == Color.red)
                                    g.drawImage(redScout,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                if (board[zi][zx].getValue() == Miner && board[zi][zx].getColor() == Color.red)
                                    g.drawImage(redMiner,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                if (board[zi][zx].getValue() == Spy && board[zi][zx].getColor() == Color.red)
                                    g.drawImage(redSpy,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                if (board[zi][zx].getValue() == Bomb && board[zi][zx].getColor() == Color.red)
                                    g.drawImage(redBomb,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                if (board[zi][zx].getValue() == Flag && board[zi][zx].getColor() == Color.red)
                                    g.drawImage(redFlag,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                }
                                else if (board[zi][zx].getColor()==Color.red){
                                    g.setColor(board[zi][zx].getColor());
                                    g.drawImage(redTemplate,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                }

                                //blu drawings
                                if(!player1Turn){
                                if (board[zi][zx].getValue() == Commander && board[zi][zx].getColor() == Color.blue)
                                    g.drawImage(bluCommander,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                if (board[zi][zx].getValue() == General && board[zi][zx].getColor() == Color.blue)
                                    g.drawImage(bluGeneral,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                if (board[zi][zx].getValue() == Leutenant && board[zi][zx].getColor() == Color.blue)
                                    g.drawImage(bluLeutenant,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                if (board[zi][zx].getValue() == Captain && board[zi][zx].getColor() == Color.blue)
                                    g.drawImage(bluCaptain,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                if (board[zi][zx].getValue() == Sergant && board[zi][zx].getColor() == Color.blue)
                                    g.drawImage(bluSergant,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                if (board[zi][zx].getValue() == Gunny && board[zi][zx].getColor() == Color.blue)
                                    g.drawImage(bluGunny,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                if (board[zi][zx].getValue() == Scout && board[zi][zx].getColor() == Color.blue)
                                    g.drawImage(bluScout,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                if (board[zi][zx].getValue() == Miner && board[zi][zx].getColor() == Color.blue)
                                    g.drawImage(bluMiner,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                if (board[zi][zx].getValue() == Spy && board[zi][zx].getColor() == Color.blue)
                                    g.drawImage(bluSpy,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                if (board[zi][zx].getValue() == Bomb && board[zi][zx].getColor() == Color.blue)
                                    g.drawImage(bluBomb,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                if (board[zi][zx].getValue() == Flag && board[zi][zx].getColor() == Color.blue)
                                    g.drawImage(bluFlag,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                }
                                else if (board[zi][zx].getColor()==Color.blue){
                                    g.setColor(board[zi][zx].getColor());
                                    g.drawImage(bluTemplate, getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                                }

                            }
                            else if (board[zi][zx].getColor()==Color.red){
                                    g.drawImage(redTemplate,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                            }
                            else if (board[zi][zx].getColor()==Color.blue){
                                    g.drawImage(bluTemplate,getX(zx * xdelta), getY(zi * ydelta), xdelta, ydelta, this);
                            }
                        }   

                }
                }
                if(player1Turn!=lastTurn){
                        g.setColor(Color.yellow);
                        g.setFont(new Font("Algerian",Font.CENTER_BASELINE,40));
                        if (!lastTurn)
                            g.drawString("Player 1's Turn in:",getX(getWidth2()/2 - 180),getYNormal((int)(getHeight2()/1.75)));
                        if (lastTurn)
                            g.drawString("Player 2's Turn in:",getX(getWidth2()/2 - 180),getYNormal((int)(getHeight2()/1.75)));
                        g.setFont(new Font("Algerian",Font.CENTER_BASELINE,font));
                        g.drawString(""+timer,getX(getWidth2()/2-(int)(font/2.5)),getYNormal((int)(getHeight2()/2.125)));

                        if(timeCount % 100 == 99){
                            if(timer<=1){
                                lastTurn=player1Turn;
                                timer=4;
                                timeCount=0;
                            }
                            timer--;
                            font=100;
                        }
                        timeCount++;
                        font--;
                }
            }
        }
        if(start && !help){
        g.drawImage(sc,getX(0),getY(0),
                getWidth2(),getHeight2(),this);
        }
        if(pause){
            g.drawImage(helpBackround,getX(0),getY(0),
                getWidth2(),getHeight2(),this);
        }
        if(help){
            g.drawImage(helpBackround,getX(0),getY(0),
                getWidth2(),getHeight2(),this);
            g.drawImage(helpMenu,getX(0),getY(0),
                getWidth2(),getHeight2(),this); 
        }
//         g.drawImage(sc,getX(0),getY(0),
//            getWidth2(),getHeight2(),this);
        gOld.drawImage(image, 0, 0, null);
    }
////////////////////////////////////////////////////////////////////////////
// needed for     implement runnable
    public void run() {
        while (true) {
            animate();
            repaint();
            double seconds = .01;    //time that 1 frame takes.
            int miliseconds = (int) (1000.0 * seconds);
            try {
                Thread.sleep(miliseconds);
            } catch (InterruptedException e) {
            }
        }
    }
  
    public void reset() {

        for (int zi = 0;zi<NUM_ROWS;zi++)
        {
            for (int zx = 0;zx<NUM_COLUMNS;zx++)
            {
                board[zi][zx] = null;
            }
        }
        //red images
        redCommander = Toolkit.getDefaultToolkit().getImage("./red_commander.GIF");
        redGeneral = Toolkit.getDefaultToolkit().getImage("./red_general.GIF");
        redLeutenant = Toolkit.getDefaultToolkit().getImage("./red_Leutennant.GIF");
        redCaptain = Toolkit.getDefaultToolkit().getImage("./red_captain.GIF");
        redSergant = Toolkit.getDefaultToolkit().getImage("./red_sergant.GIF");
        redGunny = Toolkit.getDefaultToolkit().getImage("./red_gunny.GIF");
        redScout = Toolkit.getDefaultToolkit().getImage("./red_private.GIF");
        redMiner = Toolkit.getDefaultToolkit().getImage("./red_miner.GIF");
        redSpy = Toolkit.getDefaultToolkit().getImage("./red_spy.GIF");
        redBomb = Toolkit.getDefaultToolkit().getImage("./red_bomb.GIF");
        redFlag = Toolkit.getDefaultToolkit().getImage("./red_flag.GIF");
        redTemplate = Toolkit.getDefaultToolkit().getImage("./red_template.GIF");
        
        //blu images
        bluCommander = Toolkit.getDefaultToolkit().getImage("./blu_commander.GIF");
        bluGeneral = Toolkit.getDefaultToolkit().getImage("./blu_general.GIF");
        bluLeutenant = Toolkit.getDefaultToolkit().getImage("./blu_Leutennant.GIF");
        bluCaptain = Toolkit.getDefaultToolkit().getImage("./blu_captain.GIF");
        bluSergant = Toolkit.getDefaultToolkit().getImage("./blu_sergeant.GIF");
        bluGunny = Toolkit.getDefaultToolkit().getImage("./blu_gunny.GIF");
        bluScout = Toolkit.getDefaultToolkit().getImage("./blu_private.GIF");
        bluMiner = Toolkit.getDefaultToolkit().getImage("./blu_miner.GIF");
        bluSpy = Toolkit.getDefaultToolkit().getImage("./blu_spy.GIF");
        bluBomb = Toolkit.getDefaultToolkit().getImage("./blu_bomb.GIF");
        bluFlag = Toolkit.getDefaultToolkit().getImage("./blu_flag.GIF");
        bluTemplate = Toolkit.getDefaultToolkit().getImage("./blu_template.GIF");
        
        player1Turn=true;
        lastTurn=!player1Turn;
        lastRow=0;
        lastCol=0;
        timeCount=0;
        timer=3;
        font=100;
        firstRedTurn=true;
        firstBluTurn=true;
        start = true;
        help = false;
        pause = false;
        redFlagzi=0;
        redFlagzx=0;
        bluFlagzi=0;
        bluFlagzx=0;
    }
/////////////////////////////////////////////////////////////////////////
    public void animate() {

        if (animateFirstTime) {
            animateFirstTime = false;
            if (xsize != getSize().width || ysize != getSize().height) {
                xsize = getSize().width;
                ysize = getSize().height;
            }

            reset();
            bgsound = new sound("fates.wav");
        }
        
    }
////////////////////////////////////////////////////////////////////////////
    public void REDreset() {
        //Team RED's Pieces
        int bombs = 0;
        int commander = 0;
        int general = 0;
        int leutenant = 0;
        int captain = 0;
        int sergant = 0;
        int gunny = 0;
        int scout = 0;
        int miner = 0;
        int spy = 0;
        for (int zi = 0;zi<2;zi++)
        {
            for (int zx = 0;zx<NUM_COLUMNS;zx++)
            {
                if(bombs<6){
                    int x = (int)(Math.random() * 2);
                    int y = (int)(Math.random() * NUM_ROWS);
                    boolean keepLooping = true;
                    while(keepLooping){
                        if(board[x][y] != null){
                            x = (int)(Math.random() * 4);
                            y = (int)(Math.random() * NUM_ROWS);
                        }
                        else{
                            board[x][y] = new StrategoPiece(Color.red,Bomb);
                            keepLooping = false;
                        }
                    }
                    bombs++;
                }
                if(commander<1){
                    int x = (int)(Math.random() * 2);
                    int y = (int)(Math.random() * NUM_ROWS);
                    boolean keepLooping = true;
                    while(keepLooping){
                        if(board[x][y] != null){
                            x = (int)(Math.random() * 4);
                            y = (int)(Math.random() * NUM_ROWS);
                        }
                        else{
                            board[x][y] = new StrategoPiece(Color.red,Commander);
                            keepLooping = false;
                        }
                    }
                    commander++;
                }
                if(general<1){
                    int x = (int)(Math.random() * 2);
                    int y = (int)(Math.random() * NUM_ROWS);
                    boolean keepLooping = true;
                    while(keepLooping){
                        if(board[x][y] != null){
                            x = (int)(Math.random() * 4);
                            y = (int)(Math.random() * NUM_ROWS);
                        }
                        else{
                            board[x][y] = new StrategoPiece(Color.red,General);
                            keepLooping = false;
                        }
                    }
                    general++;
                }
                if(leutenant<2){
                    int x = (int)(Math.random() * 2);
                    int y = (int)(Math.random() * NUM_ROWS);
                    boolean keepLooping = true;
                    while(keepLooping){
                        if(board[x][y] != null){
                            x = (int)(Math.random() * 4);
                            y = (int)(Math.random() * NUM_ROWS);
                        }
                        else{
                            board[x][y] = new StrategoPiece(Color.red,Leutenant);
                            keepLooping = false;
                        }
                    }
                    leutenant++;
                }
                if(captain<3){
                    int x = (int)(Math.random() * 2);
                    int y = (int)(Math.random() * NUM_ROWS);
                    boolean keepLooping = true;
                    while(keepLooping){
                        if(board[x][y] != null){
                            x = (int)(Math.random() * 4);
                            y = (int)(Math.random() * NUM_ROWS);
                        }
                        else{
                            board[x][y] = new StrategoPiece(Color.red,Captain);
                            keepLooping = false;
                        }
                    }
                    captain++;
                }
                if(sergant<4){
                    int x = (int)(Math.random() * 2);
                    int y = (int)(Math.random() * NUM_ROWS);
                    boolean keepLooping = true;
                    while(keepLooping){
                        if(board[x][y] != null){
                            x = (int)(Math.random() * 4);
                            y = (int)(Math.random() * NUM_ROWS);
                        }
                        else{
                            board[x][y] = new StrategoPiece(Color.red,Sergant);
                            keepLooping = false;
                        }
                    }
                    sergant++;
                }
                if(gunny<4){
                    int x = (int)(Math.random() * 2);
                    int y = (int)(Math.random() * NUM_ROWS);
                    boolean keepLooping = true;
                    while(keepLooping){
                        if(board[x][y] != null){
                            x = (int)(Math.random() * 4);
                            y = (int)(Math.random() * NUM_ROWS);
                        }
                        else{
                            board[x][y] = new StrategoPiece(Color.red,Gunny);
                            keepLooping = false;
                        }
                    }
                    gunny++;
                }
                if(scout<12){
                    int x = (int)(Math.random() * 2);
                    int y = (int)(Math.random() * NUM_ROWS);
                    boolean keepLooping = true;
                    while(keepLooping){
                        if(board[x][y] != null){
                            x = (int)(Math.random() * 4);
                            y = (int)(Math.random() * NUM_ROWS);
                        }
                        else{
                            board[x][y] = new StrategoPiece(Color.red,Scout);
                            keepLooping = false;
                        }
                    }
                    scout++;
                }
                if(miner<5){
                    int x = (int)(Math.random() * 2);
                    int y = (int)(Math.random() * NUM_ROWS);
                    boolean keepLooping = true;
                    while(keepLooping){
                        if(board[x][y] != null){
                            x = (int)(Math.random() * 4);
                            y = (int)(Math.random() * NUM_ROWS);
                        }
                        else{
                            board[x][y] = new StrategoPiece(Color.red,Miner);
                            keepLooping = false;
                        }
                    }
                    miner++;
                }
                if(spy<1){
                    int x = (int)(Math.random() * 2);
                    int y = (int)(Math.random() * NUM_ROWS);
                    boolean keepLooping = true;
                    while(keepLooping){
                        if(board[x][y] != null){
                            x = (int)(Math.random() * 4);
                            y = (int)(Math.random() * NUM_ROWS);
                        }
                        else{
                            board[x][y] = new StrategoPiece(Color.red,Spy);
                            keepLooping = false;
                        }
                    }
                    spy++;
                }
            }
        }
    }
////////////////////////////////////////////////////////////////////////////
    public void BLUreset() {
        //Team BLU's Pieces
        int bombB = 0;
        int commanderB = 0;
        int generalB = 0;
        int leutenantB = 0;
        int captainB = 0;
        int sergantB = 0;
        int gunnyB = 0;
        int scoutB = 0;
        int minerB = 0;
        int spyB = 0;
        for (int zi = 0;zi<2;zi++)
        {
            for (int zx = 0;zx<NUM_COLUMNS;zx++)
            {
                if(bombB<6){
                    int x = (int)(NUM_COLUMNS-(Math.random() * 2));
                    int y = (int)(Math.random() * NUM_ROWS);
                    boolean keepLooping = true;
                    while(keepLooping){
                        if(board[x][y] != null){
                            x = (int)(NUM_COLUMNS-(Math.random() * 4));
                            y = (int)(Math.random() * NUM_ROWS);
                        }
                        else{
                            board[x][y] = new StrategoPiece(Color.blue,Bomb);
                            keepLooping = false;
                        }
                    }
                    bombB++;
                }
                if(commanderB<1){
                    int x = (int)(NUM_COLUMNS-(Math.random() * 2));
                    int y = (int)(Math.random() * NUM_ROWS);
                    boolean keepLooping = true;
                    while(keepLooping){
                        if(board[x][y] != null){
                            x = (int)(NUM_COLUMNS-(Math.random() * 4));
                            y = (int)(Math.random() * NUM_ROWS);
                        }
                        else{
                            board[x][y] = new StrategoPiece(Color.blue,Commander);
                            keepLooping = false;
                        }
                    }
                    commanderB++;
                }
                if(generalB<1){
                    int x = (int)(NUM_COLUMNS-(Math.random() * 2));
                    int y = (int)(Math.random() * NUM_ROWS);
                    boolean keepLooping = true;
                    while(keepLooping){
                        if(board[x][y] != null){
                            x = (int)(NUM_COLUMNS-(Math.random() * 4));
                            y = (int)(Math.random() * NUM_ROWS);
                        }
                        else{
                            board[x][y] = new StrategoPiece(Color.blue,General);
                            keepLooping = false;
                        }
                    }
                    generalB++;
                }
                if(leutenantB<2){
                    int x = (int)(NUM_COLUMNS-(Math.random() * 2));
                    int y = (int)(Math.random() * NUM_ROWS);
                    boolean keepLooping = true;
                    while(keepLooping){
                        if(board[x][y] != null){
                            x = (int)(NUM_COLUMNS-(Math.random() * 4));
                            y = (int)(Math.random() * NUM_ROWS);
                        }
                        else{
                            board[x][y] = new StrategoPiece(Color.blue,Leutenant);
                            keepLooping = false;
                        }
                    }
                    leutenantB++;
                }
                if(captainB<3){
                    int x = (int)(NUM_COLUMNS-(Math.random() * 2));
                    int y = (int)(Math.random() * NUM_ROWS);
                    boolean keepLooping = true;
                    while(keepLooping){
                        if(board[x][y] != null){
                            x = (int)(NUM_COLUMNS-(Math.random() * 4));
                            y = (int)(Math.random() * NUM_ROWS);
                        }
                        else{
                            board[x][y] = new StrategoPiece(Color.blue,Captain);
                            keepLooping = false;
                        }
                    }
                    captainB++;
                }
                if(sergantB<4){
                    int x = (int)(NUM_COLUMNS-(Math.random() * 2));
                    int y = (int)(Math.random() * NUM_ROWS);
                    boolean keepLooping = true;
                    while(keepLooping){
                        if(board[x][y] != null){
                            x = (int)(NUM_COLUMNS-(Math.random() * 4));
                            y = (int)(Math.random() * NUM_ROWS);
                        }
                        else{
                            board[x][y] = new StrategoPiece(Color.blue,Sergant);
                            keepLooping = false;
                        }
                    }
                    sergantB++;
                }
                if(gunnyB<4){
                    int x = (int)(NUM_COLUMNS-(Math.random() * 2));
                    int y = (int)(Math.random() * NUM_ROWS);
                    boolean keepLooping = true;
                    while(keepLooping){
                        if(board[x][y] != null){
                            x = (int)(NUM_COLUMNS-(Math.random() * 4));
                            y = (int)(Math.random() * NUM_ROWS);
                        }
                        else{
                            board[x][y] = new StrategoPiece(Color.blue,Gunny);
                            keepLooping = false;
                        }
                    }
                    gunnyB++;
                }
                if(scoutB<12){
                    int x = (int)(NUM_COLUMNS-(Math.random() * 2));
                    int y = (int)(Math.random() * NUM_ROWS);
                    boolean keepLooping = true;
                    while(keepLooping){
                        if(board[x][y] != null){
                            x = (int)(NUM_COLUMNS-(Math.random() * 4));
                            y = (int)(Math.random() * NUM_ROWS);
                        }
                        else{
                            board[x][y] = new StrategoPiece(Color.blue,Scout);
                            keepLooping = false;
                        }
                    }
                    scoutB++;
                }
                if(minerB<5){
                    int x = (int)(NUM_COLUMNS-(Math.random() * 2));
                    int y = (int)(Math.random() * NUM_ROWS);
                    boolean keepLooping = true;
                    while(keepLooping){
                        if(board[x][y] != null){
                            x = (int)(NUM_COLUMNS-(Math.random() * 4));
                            y = (int)(Math.random() * NUM_ROWS);
                        }
                        else{
                            board[x][y] = new StrategoPiece(Color.blue,Miner);
                            keepLooping = false;
                        }
                    }
                    minerB++;
                }
                if(spyB<1){
                    int x = (int)(NUM_COLUMNS-(Math.random() * 2));
                    int y = (int)(Math.random() * NUM_ROWS);
                    boolean keepLooping = true;
                    while(keepLooping){
                        if(board[x][y] != null){
                            x = (int)(NUM_COLUMNS-(Math.random() * 4));
                            y = (int)(Math.random() * NUM_ROWS);
                        }
                        else{
                            board[x][y] = new StrategoPiece(Color.blue,Spy);
                            keepLooping = false;
                        }
                    }
                    spyB++;
                }
            }
        }
    }
////////////////////////////////////////////////////////////////////////////
    public void start() {
        if (relaxer == null) {
            relaxer = new Thread(this);
            relaxer.start();
        }
    }
////////////////////////////////////////////////////////////////////////////
    public void stop() {
        if (relaxer.isAlive()) {
            relaxer.stop();
        }
        relaxer = null;
    }
/////////////////////////////////////////////////////////////////////////
    public int getX(int x) {
        return (x + XBORDER);
    }
    public double getX(double x) {
        return (x + XBORDER);
    }
    public int getY(int y) {
        return (y + YBORDER + YTITLE);
    }
    public double getY(double y) {
        return (y + YBORDER + YTITLE);
    }
    public int getYNormal(int y) {
        return (-y + YBORDER + YTITLE+getHeight2());
    }
    public double getYNormal(double y) {
        return (-y + YBORDER + YTITLE+getHeight2());
    }
    public int getWidth2() {
        return (xsize - getX(0) - XBORDER);
    }
    public int getHeight2() {
        return (ysize - getY(0) - YBORDER);
    }
}
