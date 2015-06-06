public class IsThereAPath {

    static boolean IsThereAPath(int[][] connections, int start, int end) {
      boolean answer = Search(connections, start, end);
      wipe();
      return answer;
    }

    //for avoiding infinite loops
    static int[][] checked = new int[100][2]; //list of pairs we've checked
    static int position = 0; //position within the checked array

    static boolean Search(int[][] connections, int start, int end) {
      //include the current start-end pair in the checked list
      checked[position][0] = start;
      checked[position][1] = end;
      position++;

      for (int i = 0; i < connections.length; i++) {

        if (connections[i][0] == start && connections[i][1] == end) {
          return true;
        }

        //if not, check out what else branches out from here
        else if (connections[i][0] == start) {
          
          //avoid going in circles by iterating through the list of checked pairs.
          //Check to see if any of the checked pairs is the pair you are about to check.
          boolean alreadyChecked = false;
          for (int j = 0; j < position; j++) {
            if (checked[j][0] == connections[i][1]) {
              alreadyChecked = true;
              break;
            }
          }
          //if you found it deeper in, pass that result back
          if (!alreadyChecked && Search(connections, connections[i][1],end)) {
            return true;
          }
        }
      }
      return false;
    }

    static void wipe() {
      for (int i = 0; i <= position; i++) {
        checked[i][0] = 0;
        checked[i][1] = 0;
      }
      position = 0;
    }

    // public static void main(String[] args) {
    //   System.out.println("1,4: "+Search(anArray,1,4));
    //   System.out.println("3,5: "+Search(anArray,3,5));
    //   System.out.println("3,2: "+Search(anArray,3,2));
    //   System.out.println("2,1: "+Search(anArray,2,1));
    //   System.out.println("7,6: "+Search(anArray,7,6));
    //   System.out.println("3,9: "+Search(anArray,3,9));
    //   System.out.println("3,7: "+Search(anArray,3,7));
    // }
}
