import javax.swing.JFrame;

public class GameFrame extends JFrame{

    GameFrame(){

        this.add(new GamePanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack(); //if we add components, the JFrame will fit around them
        this.setVisible(true);
        this.setLocationRelativeTo(null);//frame appears in middle of screen
    }
}