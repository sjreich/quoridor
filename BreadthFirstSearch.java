public class BreadthFirstSearch {
  /* this method determines whether there is a path from one square to another
  *it is used to implement the rule that you cannot place a wall that totally
  *blocks in an opponent */

  //holds the queue
  static int[] toCheckList = new int[100];
  
  //bookmarks within toCheckList
  static int toCheckTail = 1;
  static int toCheckHead = 0;

  static public boolean BreadthFirstSearch (int start, int end) {
    //The toCheckList array functions as a queue (FIFO)
    //toCheckHead and toCheckTail serve as bookmarks within toCheckList
    //All of these are declared at the top of the page. 

    //Set up the queue and add the starting point to the queue
    wipe();
    toCheckList[0] = start;

    //keep checking as long as there is something left in the queue 
    while (toCheckHead < toCheckTail) {
      int currentSquare = toCheckList[toCheckHead];

      //found the end?
      if (currentSquare == end) {

        //yes, found it
        return true;
      }

      //otherwise, add to the queue all of the squares that are accessible from toCheckList[toCheckHead]
      //that is, unless there is a wall in the way, or we're on the edge of the board, or we'd be making a loop
      //to the left
      int leftOfCurrent = Math.max(0,currentSquare-1);
      if (Board.leftOk(currentSquare) && !loop(leftOfCurrent)) {
        toCheckList[toCheckTail] = leftOfCurrent;
        toCheckTail++;
      }

      //to the right
      int rightOfCurrent = Math.min(82,currentSquare+1);
      if(Board.rightOk(currentSquare) && !loop(rightOfCurrent)) {
        toCheckList[toCheckTail] = rightOfCurrent;
        toCheckTail++;
      }

      //below
      int belowCurrent = Math.min(82,currentSquare+9);
      if (Board.downOk(currentSquare) && !loop(belowCurrent)){
        toCheckList[toCheckTail] = belowCurrent;
        toCheckTail++;
      }

      //above
      int aboveCurrent = Math.max(0,currentSquare-9);
      if (Board.upOk(currentSquare) && !loop(aboveCurrent)) {
        toCheckList[toCheckTail] = aboveCurrent;
        toCheckTail++;
      }

      //ad hoc solution for the top
      if (currentSquare<=9 && !loop(0)) {
        toCheckList[toCheckTail] = 0;
        toCheckTail++;
      }

      //ad hoc solution for the bottom
      if (currentSquare>=73 && !loop(82)) {
        toCheckList[toCheckTail] = 82;
        toCheckTail++;
      }

      toCheckHead++;
    }

    //Never found the end
    return false;
  }

  static boolean loop(int newSquare) {
    for (int j = 0; toCheckList[j]>=0; j++) {
      if (toCheckList[j] == newSquare) {
        return true;
      }
    }
    return false;
  }

  static void wipe() {
    //wipes the queue to prepare for future searches
    //needs to wipe to -1 to avoid conflicts with square 0.
    for (int i = 0; i < toCheckList.length; i++) {
      toCheckList[i] = -1;
    }
    toCheckTail = 1;
    toCheckHead = 0;
  }
}