import javax.swing.*;

class ExplorerTest extends JFrame
{

    ExplorerTest(String path)
    {
        super("Windows Exploder - Javatpoint");
        add(new Explorer(path),"Center");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400,400);
        setVisible(true);
    }

    public static void main(String[] args)
    {
        new ExplorerTest("C:\\Users\\srich\\Desktop");
    }
}