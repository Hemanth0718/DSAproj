import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.tree.*;

class Explorer extends JPanel implements ActionListener
{
    JTextField jtf;
    JTextArea jta;
    JTree tree;
    JButton refresh;
    JTable jtb;
    JScrollPane jsp;
    JScrollPane jspTable;

    String currDirectory=null;

    final String[] colHeads={"File Name","SIZE(in Bytes)","Read Only","Hidden"};
    String[][]data={{"","","","",""}};

    Explorer(String path)
    {

        jtf=new JTextField();
        jta=new JTextArea(5,30);
        refresh=new JButton("Refresh");

        File temp=new File(path);
        DefaultMutableTreeNode top=createTree(temp);


        tree=new JTree(top);

        jsp=new JScrollPane(tree);

        final String[] colHeads={"File Name","SIZE(in Bytes)","Read Only","Hidden"};
        String[][]data={{"","","","",""}};
        jtb=new JTable(data, colHeads);
        jspTable=new JScrollPane(jtb);

        setLayout(new BorderLayout());
        add(jtf,BorderLayout.NORTH);
        add(jsp,BorderLayout.WEST);
        add(jspTable,BorderLayout.CENTER);
        add(refresh,BorderLayout.SOUTH);

        tree.addMouseListener(
                new MouseAdapter()
                {
                    public void mouseClicked(MouseEvent e)
                    {
                        doMouseClicked(e);
                    }
                });
        jtf.addActionListener(this);
        refresh.addActionListener(this);
    }

    public void actionPerformed(ActionEvent ev)
    {
        File temp=new File(jtf.getText());
        DefaultMutableTreeNode newtop=createTree(temp);
        if(newtop!=null)
            tree=new JTree(newtop);
        remove(jsp);
        jsp=new JScrollPane(tree);
        setVisible(false);
        add(jsp,BorderLayout.WEST);
        tree.addMouseListener(
                new MouseAdapter()
                {
                    public void mouseClicked(MouseEvent e)
                    {
                        doMouseClicked(e);
                    }
                });

        setVisible(true);
    }


    DefaultMutableTreeNode createTree(File temp)
    {
        DefaultMutableTreeNode top=new DefaultMutableTreeNode(temp.getPath());
        if(!(temp.exists() && temp.isDirectory()))
            return top;

        fillTree(top,temp.getPath());

        return top;
    }

    void fillTree(DefaultMutableTreeNode root, String filename)
    {
        File temp=new File(filename);

        if(!(temp.exists() && temp.isDirectory()))
            return;
        File[] filelist=temp.listFiles();

        for (File file : filelist) {
            if (!file.isDirectory())
                continue;
            final DefaultMutableTreeNode tempDmtn = new DefaultMutableTreeNode(file.getName());
            root.add(tempDmtn);
            final String newfilename = filename + "\\" + file.getName();
            Thread t = new Thread(() -> fillTree(tempDmtn, newfilename));
            if (t == null) {
                System.out.println("no more thread allowed " + newfilename);
                return;
            }
            t.start();
        }
    }

    void doMouseClicked(MouseEvent me)
    {
        TreePath tp=tree.getPathForLocation(me.getX(),me.getY());
        if(tp==null) return;
        String s=tp.toString();
        s=s.replace("[","");
        s=s.replace("]","");
        s=s.replace(", ","\\");
        jtf.setText(s);
        showFiles(s);


    }
    void showFiles(String filename)
    {
        File temp=new File(filename);
        data=new String[][]{{"","","",""}};
        remove(jspTable);
        jtb=new JTable(data, colHeads);
        jspTable=new JScrollPane(jtb);
        setVisible(false);
        add(jspTable,BorderLayout.CENTER);
        setVisible(true);

        if(!temp.exists()) return;
        if(!temp.isDirectory()) return;

        File[] filelist=temp.listFiles();
        int fileCounter=0;
        assert filelist != null;
        data=new String[filelist.length][4];
        for (File file : filelist) {
            if (file.isDirectory())
                continue;
            data[fileCounter][0] = file.getName();
            data[fileCounter][1] = file.length() + "";
            data[fileCounter][2] = !file.canWrite() + "";
            data[fileCounter][3] = file.isHidden() + "";
            fileCounter++;
        }

        String dataTemp[][]=new String[fileCounter][4];
        System.arraycopy(data, 0, dataTemp, 0, fileCounter);
        data=dataTemp;


        remove(jspTable);
        jtb=new JTable(data, colHeads);
        jspTable=new JScrollPane(jtb);
        setVisible(false);
        add(jspTable,BorderLayout.CENTER);
        setVisible(true);
    }

}

