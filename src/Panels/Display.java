package Panels;

import Menus.AddStudent;
import DSA.Tree;
import DSA.Node;
import Menus.SubjectMenu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static DSA.Tree.findNode;

public class Display extends JPanel implements ActionListener {
    JTree tree;
    JScrollPane jsp;
    JTable jtb;
    JScrollPane jspTable;
    Tree Htree = new Tree();
    String[] colHeads = { "Exam", "Marks", "Attendance" };
    String[][] data;

    JButton newButton;
    JButton deleteButton;
    JButton refreshButton;
    JButton updateButton;

    boolean delete = false;

    public Display() {
        Htree.CreateTree();
        tree = new JTree(addToTree(Htree.root));
        jsp = new JScrollPane(tree);
        setLayout(new BorderLayout());

        data = new String[][] { { "", "", "" } };
        jtb = new JTable(data, colHeads);
        jspTable = new JScrollPane(jtb);
        tree.setPreferredSize(new Dimension(200, -1));

        newButton = new JButton("Add");
        newButton.setBounds(0, 0, 40, 40);
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddStudent addStudentPanel = new AddStudent();

                JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(Display.this), "Add Student",
                        true);
                dialog.setContentPane(addStudentPanel);
                dialog.setSize(300, 300);
                dialog.setVisible(true);

                SubjectMenu sub = new SubjectMenu(addStudentPanel.name, addStudentPanel.branch,
                        addStudentPanel.department, addStudentPanel.semester, addStudentPanel.year);
                dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(new Display()), "Add Subject", true);
                dialog.setContentPane(sub);
                dialog.setSize(500, 300);
                dialog.setVisible(true);

            }
        });

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(40, 0, 40, 40);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                delete = !delete;
            }
        });

        refreshButton = new JButton("Refresh");
        refreshButton.setBounds(80, 0, 40, 40);
        refreshButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Htree.root = new Node("Amrita.txt");
                Htree.CreateTree();
                DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                DefaultMutableTreeNode oldRoot = (DefaultMutableTreeNode) model.getRoot();
                oldRoot.removeAllChildren(); // Clear existing nodes
                DefaultMutableTreeNode newRoot = addToTree(Display.this.Htree.root);
                model.setRoot(newRoot); // Set the new root
                model.reload();
            }
        });

        updateButton = new JButton("Update");
        updateButton.setBounds(120, 0, 40, 40);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // break
                return;
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(newButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(updateButton);
        add(buttonPanel, BorderLayout.NORTH);
        add(jsp, BorderLayout.WEST);
        add(jspTable, BorderLayout.CENTER);

        tree.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        doMouseClicked(e);
                    }
                });
    }

    private void doMouseClicked(MouseEvent e) {
        TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
        if (tp == null)
            return;
        TreePath parent = tp.getParentPath();
        String s = tp.toString();
        String p = parent.toString();

        String dataList = s.substring(1, s.length() - 1);
        String parentList = p.substring(1, p.length() - 1);
        System.out.println(dataList);
        System.out.println(delete);
        if (delete) {
            Htree.deleteNode(dataList);
            return;
        }

        ArrayList<String> list = new ArrayList<String>(Arrays.asList(dataList.split(",")));
        ArrayList<String> listP = new ArrayList<String>(Arrays.asList(parentList.split(",")));
        String current = (list.get(list.size() - 1)).substring(1);
        String currentParent = (listP.get(listP.size() - 1)).substring(1);
        String Branch = ((listP.get(1)).substring(1));

        Node branchNode = findNode(Htree.root, Branch);
        Node currentNode;
        Node ParentNode = findNode(branchNode, currentParent);
        if (ParentNode == null) {
            currentNode = findNode(branchNode, current);
        } else {
            currentNode = findNode(ParentNode, current);
        }

        if (currentNode != null) {
            if (currentNode.data instanceof SubjectDetails) {
                editTable(currentNode.data);
            } else if (onlyDigits((String) currentNode.data)) {
                editTable(currentNode, ParentNode);
            } else if (currentNode.data instanceof String) {
                editTable(currentNode, currentNode.children.size());
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            TreePath tp = tree.getPathForLocation(e.getX(), e.getY());
            if (tp == null)
                return;
            String s = tp.toString();
            String dataList = s.substring(1, s.length() - 1);
            ArrayList<String> list = new ArrayList<String>(Arrays.asList(dataList.split(",")));
            String current = (list.get(list.size() - 1)).substring(1);
            Node currentNode = findNode(Htree.root, current);
        }
    }

    private void editTable(Node currentNode, Node parentNode) {
        System.out.println("Here");
        int row = 0;
        ArrayList<String> tempHeads = new ArrayList<>();
        tempHeads.add("name");
        data = new String[currentNode.children.size()][1];
        for (Node child : currentNode.children) {
            System.out.println(child.data);
            data[row++][0] = (String) child.data;
        }
        colHeads = tempHeads.toArray(new String[0]);
        jtb.setModel(new DefaultTableModel(data, colHeads));
    }

    private void editTable(Object val) {
        SubjectDetails sub = (SubjectDetails) val;
        colHeads = new String[] { "Exam", "Marks" };
        data = new String[sub.marks.size() + 1][colHeads.length + 1];
        int row = 0;
        for (String i : sub.marks.keySet()) {
            data[row++][0] = i;
        }
        row = 0;
        for (int i : sub.marks.values()) {
            data[row++][1] = String.valueOf(i);
        }
        jtb.setModel(new DefaultTableModel(data, colHeads));
    }

    private void editTable(Node obj, int i) {
        if (obj.children.isEmpty()) {
            return;
        }

        if (obj.children.get(0).data instanceof SubjectDetails) {
            colHeads = new String[] { "Subject", "Attendance" };
            data = new String[i + 1][colHeads.length];
            int row = 0;
            for (Node child : obj.children) {
                SubjectDetails subject = (SubjectDetails) child.data;
                if (subject != null) {
                    data[row][0] = subject.subjectName;
                    data[row][1] = String.valueOf(subject.attendance);
                }
                row++;
            }
        }

        jtb.setModel(new DefaultTableModel(data, colHeads));
    }

    public DefaultMutableTreeNode addToTree(Node node) {
        DefaultMutableTreeNode result = new DefaultMutableTreeNode(node.data.toString());
        for (Node child : node.children) {
            result.add(addToTree(child));
        }
        return result;
    }

    public static boolean onlyDigits(String str) {
        String regex = "[1-8]+";
        Pattern p = Pattern.compile(regex);
        if (str == null) {
            return false;
        }
        Matcher m = p.matcher(str);
        return m.matches();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
