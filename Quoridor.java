import javax.swing.JFrame;
import java.awt.EventQueue;

public class Quoridor extends JFrame {

  public Quoridor () { //constructor method
    add(new Board());
    pack();
    setTitle("Quoridor");
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        JFrame ex = new Quoridor();
        ex.setVisible(true);
      }
    });
  }
}
