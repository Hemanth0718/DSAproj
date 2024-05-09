import Panels.Display;

import javax.swing.*;

public class Main extends JFrame {
    public Display display;
    Main() {
        display = new Display();
        add(display,"Center");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(750,500);
        setVisible(true);
    }

    public static void main(String[] arg){
        new Main();
    }
}