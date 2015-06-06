import javax.swing.JPanel;
import javax.swing.ImageIcon;

// import java.awt.event.ActionListener; //remember: implements ActionListener?
// import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class Board1 extends JPanel implements MouseListener {

  private Image wallH;
  private Image wallV;
  private Image blueDot;
  private Image redDot;

  //initial positions
  int redPos = 5;
  int bluePos = 77;

  //players start with ten walls apiece
  int redWallsLeft = 10;
  int blueWallsLeft = 10;

  //is there a wall in a given position?
  //walls start in the upper-left-hand corner of a given square
  //from there they go either down or to the right
  boolean[] vWalls = new boolean[100];
  boolean[] hWalls = new boolean[100];

  // true => red's turn; false => blue's turn
  boolean redTurn = true;

  //array for all of the possible moves
  //if directConnections[x][y] == true, then it's permissible to move from square x to square y
  // boolean[][] directConnections = new boolean[83][83];

  public Board1() {
    loadImages();
    setPreferredSize(new Dimension(550, 550));
    setBackground(Color.white);
    addMouseListener(this);
  }

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

    int xWallCrd = (e.getX()+5) / 50;
    int yWallCrd = ((e.getY()+5) / 50) - 1;
    int wallLocation = 9*yWallCrd + xWallCrd;
    int xDotCrd = (e.getX()) / 50;
    int yDotCrd = ((e.getY()) / 50) - 1;
    int dotLocation = 9*yDotCrd + xDotCrd;

    //did someone click somewhere on the game board?
    if (e.getX() >= 40 && e.getX() <= 510 && e.getY() >= 40 && e.getY() <= 510) {

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

          // directConnections[wallLocation-1][wallLocation] = false;
          // directConnections[wallLocation][wallLocation-1] = false;
          // directConnections[wallLocation+8][wallLocation+9] = false;
          // directConnections[wallLocation+9][wallLocation+8] = false;

          //check to make sure that a path remains
          if (BreadthFirstSearch(redPos,82) && BreadthFirstSearch(bluePos,0)) {
            // //if so, add the wall
            // vWalls[wallLocation] = true;
            //reduce the walls-remaining count
            if (redTurn) redWallsLeft--;
            else blueWallsLeft--;
            //toggle the turn
            redTurn = !redTurn;
          }
          //if no path remains, add those connections back (and do not add the wall, reduce the wall-count, toggle the turn)
          else {
            vWalls[wallLocation] = false;
            // directConnections[wallLocation-1][wallLocation] = true;
            // directConnections[wallLocation][wallLocation-1] = true;
            // directConnections[wallLocation+8][wallLocation+9] = true;
            // directConnections[wallLocation+9][wallLocation+8] = true;
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

          //remove the appropriate direct connections
            hWalls[wallLocation] = true;
          // directConnections[wallLocation-9][wallLocation] = false;
          // directConnections[wallLocation][wallLocation-9] = false;
          // directConnections[wallLocation-8][wallLocation+1] = false;
          // directConnections[wallLocation+1][wallLocation-8] = false;

          //check to make sure that a path remains
          if (BreadthFirstSearch(redPos,82) && BreadthFirstSearch(bluePos,0)) {
            // //if so, add the wall
            // hWalls[wallLocation] = true;
            //reduce the walls-remaining count
            if (redTurn) redWallsLeft--;
            else blueWallsLeft--;
            //toggle the turn
            redTurn = !redTurn;
          }
          //if no path remains, add those connections back (and do not add the wall, reduce the wall-count, toggle the turn)
          else {
            hWalls[wallLocation] = false;
            // directConnections[wallLocation-9][wallLocation] = true;
            // directConnections[wallLocation][wallLocation-9] = true;
            // directConnections[wallLocation-8][wallLocation+1] = true;
            // directConnections[wallLocation+1][wallLocation-8] = true;
          }
        }
      }

      //move a dot (if you clicked near the middle of a square)
      else if (e.getX()%50>=10 && e.getX()%50<=40 && e.getY()%50>=10 && e.getY()%50<=40) {
        
        // //allow for jumps and diagonals if the pieces are next to each other
        // if (directConnections[redPos][bluePos]) {
        //   directConnections[redPos][bluePos] = false;
        //   directConnections[bluePos][redPos] = false;
        //   // int difference = redPos-bluePos;
        //   // switch (difference) {
        //     // case 1: 
        //       if (redTurn) {
        //         if (!directConnections[bluePos][bluePos-1]) {
        //           directConnections[redPos][bluePos+9] = true;
        //           directConnections[redPos][bluePos-9] = true;
        //           if (directConnections[redPos][dotLocation]) {
        //             redPos = dotLocation;
        //             redTurn = false;
        //           }
        //           directConnections[redPos][bluePos+9] = false;
        //           directConnections[redPos][bluePos-9] = false;                  
        //         }
        //         else {
        //           directConnections[redPos][bluePos-1] = true;
        //           if (directConnections[redPos][dotLocation]) {
        //             redPos = dotLocation;
        //             redTurn = false;
        //           }
        //           directConnections[redPos][bluePos - 1] = false;
        //         }
        //       }
        //     //   break;
        //     // case -1: 
        //     // case 9: 
        //     // case -9: 
        //   // }
        // }

      //   //move a red dot: the simple case
      //   if (redTurn) {
      //     if (directConnections[redPos][dotLocation]) {
      //       redPos = dotLocation;
      //       redTurn = false;
      //     }
      //   }
      //   //move a blue dot
      //   else if (!redTurn) {
      //     if (directConnections[bluePos][dotLocation]) {
      //       bluePos = dotLocation;
      //       redTurn = true;
      //     }
      //   }
      // }

      System.out.println("Walls remaining:");
      System.out.println("  Red: "+redWallsLeft);
      System.out.println("  Blue: "+blueWallsLeft);
      System.out.println(redTurn ? "Red's turn" : "Blue's turn");
      repaint();
    }
  }

  /* this method determines whether there is a path from one square to another
  *it is used to implement the rule that you cannot place a wall that totally
  *blocks in an opponent */
  boolean BreadthFirstSearch (int start, int end) {

    //The toCheckList array functions as a queue (FIFO)
    //toCheckHead and toCheckTail serve as bookmarks within toCheckList
    //All of these are declared at the top of the page. 

    //Add the starting point to the queue
    toCheckList[0] = start;

    //keep checking as long as there is something left in the queue 
    while (toCheckHead < toCheckTail) {
      //found the end?
      if (toCheckList[toCheckHead] == end) {
        //We're finished, so wipe the queue to prepare for future searches
        for (int i = 0; i < toCheckList.length; i++) {
          toCheckList[i] = 0;
        }
        toCheckTail = 1;
        toCheckHead = 0;

        //yes, found it
        return true;
      }

      //otherwise, add to the queue all of the squares that are accessible from toCheckList[toCheckHead]
      //that is, unless there is a wall in the way, or we're on the edge of the board, or we'd be making a loop
      //to the right
      //turn all of this loop stuff into a method and repeat it
      loop = false;
      for (int j = 0; j < toCheckList.length; j++) {
        if (toCheckList[j] == toCheckList[Math.min(82,toCheckHead+1)]) {
          loop = true;
          break;
        }
      }
      if(!vWalls[Math.min(82,toCheckList[toCheckHead]+1)] && !vWalls[Math.max(0,toCheckList[toCheckHead]-8)] && toCheckList[toCheckHead]%9!=0 && !loop) {
        toCheckList[toCheckTail] = toCheckList[Math.min(82,toCheckHead+1)];
        toCheckTail++;
      }
      //to the left
      loop = false;
      for (int j = 0; j < toCheckList.length; j++) {
        if (toCheckList[j] == toCheckList[Math.max(0,toCheckHead-1)]) {
          loop = true;
          break;
        }
      }
      if (!vWalls[toCheckList[toCheckHead]] && !vWalls[Math.max(0,toCheckList[toCheckHead]-9)] && toCheckList[toCheckHead]%9!=1 && !loop) {
        toCheckList[toCheckTail] = toCheckList[Math.min(0,toCheckHead-1)];
        toCheckTail++;
      }
      //below
      loop = false;
      for (int j = 0; j < toCheckList.length; j++) {
        if (toCheckList[j] == toCheckList[Math.max(82,toCheckHead+9)]) {
          loop = true;
          break;
        }
      }
      if (!hWalls[Math.min(82,toCheckList[toCheckHead]+9)] && !hWalls[Math.min(82,toCheckList[toCheckHead]+8)] && toCheckList[toCheckHead]<=81 && !loop) {
        toCheckList[toCheckTail] = toCheckList[Math.max(82,toCheckHead+9)];
        toCheckTail++;
      }
      //above
      loop = false;
      for (int j = 0; j < toCheckList.length; j++) {
        if (toCheckList[j] == toCheckList[Math.max(0,toCheckHead-9)]) {
          loop = true;
          break;
        }
      }
      if (!hWalls[toCheckList[toCheckHead]] && !hWalls[Math.max(0,toCheckList[toCheckHead]-1)] && toCheckList[toCheckHead]>=1 && !loop) {
        toCheckList[toCheckTail] = toCheckList[Math.min(0,toCheckHead-9)];
        toCheckTail++;
      }

      toCheckHead++;
    }

    //We're finished, so wipe the queue to prepare for new searches
    for (int i = 0; i < toCheckList.length; i++) {
      toCheckList[i] = 0;
    }
    toCheckTail = 1;
    toCheckHead = 0;

    //Never found the end
    return false;
  }

  // @Override
  // public void actionPerformed(ActionEvent e) {
  // }
}
}
