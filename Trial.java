public class Trial {
	public static void main (String[] args) {
		for (int i = 0; i <= 82; i++) {
			for (int j = 0; j <= 82; j++) {
				if (!BreadthFirstSearch.BreadthFirstSearch(i,j)) 
					System.out.println(i+", "+j+": "+BreadthFirstSearch.BreadthFirstSearch(i,j));
			}
		}
	}
}