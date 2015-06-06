import javax.swing.JPanel;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class Board extends JPanel implements MouseListener {

  // true => red's turn; false => blue's turn
  static boolean redTurn = false;
  static boolean gameOver = false;

  //initial positions
  //squares are numbered 1-81, 1 = upper-left, 9 = upper-right, etc.
  static int redPos = 5;
  static int bluePos = 77;

  //players start with ten walls apiece
  static int redWallsLeft = 10;
  static int blueWallsLeft = 10;

  //variables useful for turning mouse-clicks into moves
  //for placing walls
  static int xWallCrd;
  static int yWallCrd;
  static int wallLocation;
  //for moving dots
  static int xDotCrd;
  static int yDotCrd;
  static int dotLocation;
  static int oldPos;
  static int opponentPos;
  static int moveDirection;

  //is there a wall in a given position?
  //walls start in the upper-left-hand corner of a given square
  //from there they go either down or to the right
  static boolean[] vWalls = new boolean[100];
  static boolean[] hWalls = new boolean[100];

  private Image wallH;
  private Image wallV;
  private Image blueDot;
  private Image redDot;

  private void loadImages() {
    ImageIcon iia = new ImageIcon("wallH.png");
    wallH = iia.getImage();
    ImageIcon iid = new ImageIcon("wallV.png");
    wallV = iid.getImage();
    ImageIcon iib = new ImageIcon("blueDot.png");
    blueDot = iib.getImage();
    ImageIcon iic = new ImageIcon("redDot.png");
    redDot = iic.getImage();
  }

  public Board() {
    loadImages();
    setPreferredSize(new Dimension(550, 550));
    setBackground(Color.white);
    addMouseListener(this);
    turnEnd();
  }

  @Override
  public void paintComponent(Graphics g) {

    super.paintComponent(g);
    drawBoard(g); 

    //draw any walls
    for (int i = 0; i < 100; i++) {
      if (hWalls[i]) {
        g.drawImage(wallH,50*((i)%9)+10,50*((i)/9)+45,null);
      }
      if (vWalls[i]) {
        g.drawImage(wallV,50*((i-1)%9)+45,50*((i-1)/9)+60,null);
      }
    }

    //draw the dots
    g.drawImage(redDot,56+((redPos-1)%9)*50,56+((redPos-1)/9)*50,null);
    g.drawImage(blueDot,56+((bluePos-1)%9)*50,56+((bluePos-1)/9)*50,null);

  }

  //draws the grid for the board
  private void drawBoard(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;

    for (int i = 0; i < 10; i++) {
      g2d.drawLine(50+50*i,50,50+50*i,500);
    }
    for (int i = 0; i < 10; i++) {
      g2d.drawLine(50,50+50*i,500,50+50*i);
    }
  }

  //implementing the MouseListener
  public void mousePressed(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}

  public void mouseClicked(MouseEvent e) {

    xWallCrd = (e.getX()+5) / 50;
    yWallCrd = ((e.getY()+5) / 50) - 1;
    wallLocation = 9*yWallCrd + xWallCrd;
    xDotCrd = (e.getX()) / 50;
    yDotCrd = ((e.getY()) / 50) - 1;
    dotLocation = 9*yDotCrd + xDotCrd;

    //did someone click somewhere on the game board?
    if (e.getX()>=40 && e.getX()<=510 && e.getY()>=40 && e.getY()<=510 && !gameOver) {

      //add a vertical wall (if you clicked near a vertical line)
      if ((e.getX()+5)%50 <= 10 && (e.getY()+5)%50 > 12) {
        // This long conditional ensures: 
        // that you can't put two walls in the same location
        if (!vWalls[wallLocation] && 
          // that walls don't appear out of bounds
          wallLocation%9!=1 && wallLocation<=72 && 
          // that walls don't run into each other
          !vWalls[Math.max(0,wallLocation-9)] && !vWalls[wallLocation+9] && !hWalls[wallLocation+8] && 
          // and that you can't place a wall when you're out of walls
          ((redTurn && redWallsLeft > 0)||(!redTurn && blueWallsLeft > 0))) {
          //end of conditional

          //tentatively add the wall
          vWalls[wallLocation] = true;

          //check to make sure that a path remains
          if (BreadthFirstSearch.BreadthFirstSearch(redPos,82) && BreadthFirstSearch.BreadthFirstSearch(bluePos,0)) {
            //if so, reduce the walls-remaining count, toggle the turn
            if (redTurn) redWallsLeft--;
            else blueWallsLeft--;
            turnEnd();
          }
          //if no path remains, remove the wall (and do not reduce the wall-count or toggle the turn)
          else {
            vWalls[wallLocation] = false;
          }
        }
      }

      //add a horizontal wall (if you clicked near a horizontal line)
      else if ((e.getY()+5)%50 <= 10 && (e.getX()+5)%50 > 12) {
        // This long conditional ensures: 
        // that you can't put two walls in the same location
        if (!hWalls[wallLocation] && 
          // that walls don't appear out of bounds
          wallLocation%9>0 && wallLocation>9 && wallLocation<82 &&
          // that walls don't run into each other 
          !hWalls[Math.max(0,wallLocation-1)] && !hWalls[wallLocation+1] && !vWalls[Math.max(0,wallLocation-8)] && 
          // that you can't place a wall when you're out of walls
          ((redTurn && redWallsLeft > 0) || (!redTurn && blueWallsLeft > 0))) {
          //end of conditional

          //tentatively add the wall
          hWalls[wallLocation] = true;

          //check to make sure that a path remains
          if (BreadthFirstSearch.BreadthFirstSearch(redPos,82) && BreadthFirstSearch.BreadthFirstSearch(bluePos,0)) {
            //if so, reduce the walls-remaining count, toggle the turn
            if (redTurn) redWallsLeft--;
            else blueWallsLeft--;
            turnEnd();
          }
          //if no path remains, remove the wall (and do not reduce the wall-count or toggle the turn)
          else {
            hWalls[wallLocation] = false;
          }
        }
      }

      //clicked near the middle of a square? move a dot
      else if (e.getX()%50>=10 && e.getX()%50<=40 && e.getY()%50>=10 && e.getY()%50<=40) {
        
        //The next few lines allow us to subsequently define dot-movement without knowing whose turn it is.
        //Which dot actually moves is resolved in the move() function.
        if (redTurn) {
          oldPos = redPos;
          opponentPos = bluePos;
        }
        else {
          oldPos = bluePos;
          opponentPos = redPos;
        }
        moveDirection = dotLocation-oldPos;

        //move a dot
        switch (moveDirection) {
          //simple moves to adjacent squares
          case 1: 
            if (rightOk(oldPos) && opponentPos!=dotLocation) move();
            break;
          case -1: 
            if (leftOk(oldPos) && opponentPos!=dotLocation) move();
            break;
          case 9: 
            if (downOk(oldPos) && opponentPos!=dotLocation) move();
            break;
          case -9: 
            if (upOk(oldPos) && opponentPos!=dotLocation) move();
            break;
          
          //jump over opponent
          case 2:
            if (rightOk(oldPos) && rightOk(oldPos+1) && opponentPos==oldPos+1) move();
            break;
          case -2:
            if (leftOk(oldPos) && leftOk(oldPos-1) && opponentPos==oldPos-1) move();
            break;
          case 18:
            if (downOk(oldPos) && downOk(oldPos+9) && opponentPos==oldPos+9) move();
            break;
          case -18:
            if (upOk(oldPos) && upOk(oldPos-9) && opponentPos==oldPos-9) move();
            break;

          //diagonal moves are allowed iff jumping over opponent is blocked
          case 8:
            if ((leftOk(oldPos) && downOk(oldPos-1) && opponentPos==oldPos-1 && !leftOk(opponentPos)) || 
              (downOk(oldPos) && leftOk(oldPos+9) && opponentPos==oldPos+9 && !downOk(opponentPos))) 
              move();
            break;
          case 10:
            if ((rightOk(oldPos) && downOk(oldPos+1) && opponentPos==oldPos+1 && !rightOk(opponentPos)) || 
              (downOk(oldPos) && rightOk(oldPos+9) && opponentPos==oldPos+9 && !downOk(opponentPos))) 
              move();
            break;
          case -8:
            if ((rightOk(oldPos) && upOk(oldPos+1) && opponentPos==oldPos+1 && !rightOk(opponentPos)) || 
              (upOk(oldPos) && rightOk(oldPos-9) && opponentPos==oldPos-9 && !upOk(opponentPos))) 
              move();
            break;
          case -10:
            if ((leftOk(oldPos) && upOk(oldPos-1) && opponentPos==oldPos-1 && !leftOk(opponentPos)) || 
              (upOk(oldPos) && leftOk(oldPos-9) && opponentPos==oldPos-9 && !upOk(opponentPos))) 
              move();
            break;
        }
      }
      repaint();
    }
  }

  //is it permissible to move in the direction specified?
  //these functions just simplify the code elsewhere
  static boolean leftOk (int position) {
    if (position%9!=1 && !vWalls[position] && !vWalls[Math.max(0,position-9)]) {
      return true;
    }
    else return false;
  }
  static boolean rightOk (int position) {
    if (position%9!=0 && !vWalls[position+1] && !vWalls[Math.max(0,position-8)]) {
      return true;
    }
    else return false;
  }
  static boolean upOk (int position) {
    if (position>9 && position<82 && !hWalls[position] && !hWalls[Math.max(0,position-1)]) {
      return true;
    }
    else return false;
  }
  static boolean downOk (int position) {
    if (position>0 && position<=72 && !hWalls[Math.min(82,position+9)] && !hWalls[Math.min(82,position+8)]) {
      return true;
    }
    else return false;
  }

  //move the proper dot
  void move() {
    if (redTurn) redPos = dotLocation;
    else bluePos = dotLocation;
    turnEnd();
  }

  //toggle whose turn it is, check for game end, print some useful info
  static void turnEnd() {
    redTurn = !redTurn;
    if (redPos>=72) {
      System.out.println("Red wins!");
      gameOver = true;
    }
    else if (bluePos<=9) {
      System.out.println("Blue wins!");
      gameOver=true;
    }
    else {
      System.out.println("-----");
      System.out.println("Walls remaining:");
      System.out.println("  Red: "+redWallsLeft);
      System.out.println("  Blue: "+blueWallsLeft);
      System.out.println(redTurn ? "Red's turn" : "Blue's turn");
    }
  }
}
