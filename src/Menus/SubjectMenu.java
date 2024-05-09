package Menus;

import DSA.Student;
import DSA.Tree;
import Panels.Display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SubjectMenu extends JPanel implements ActionListener {

    private JLabel subjectLabel, examLabel, marksLabel, attendanceLabel1, attendanceLabel2, attendanceLabel3;
    private JComboBox<String> subjectComboBox;
    private JTextField examNameField, marksField, attendanceField1, attendanceField2, attendanceField3;
    private JButton commitButton, doneButton;
    public Tree Htree = new Tree();
    String data1,data2,data3;
    private List<String> subjectList; // List of available subjects

    public SubjectMenu(String c_name, String c_Branch, String c_department, int c_sem, int c_year) {
        setLayout(null);

        String data = c_Branch + "," + c_department + "," + c_year + "," + c_sem + "," + c_name;
        Student student = new Student(c_name, c_sem);
        student.addSubjects();
        subjectList = student.subjects;
        data1 = subjectList.get(0);
        data2 = subjectList.get(1);
        data3 = subjectList.get(2);

        subjectLabel = new JLabel("Subject:");
        subjectLabel.setBounds(10, 10, 80, 20);
        add(subjectLabel);

        subjectComboBox = new JComboBox<>(subjectList.toArray(new String[0]));
        subjectComboBox.setBounds(100, 10, 150, 20);
        subjectComboBox.addActionListener(this);
        add(subjectComboBox);

        examLabel = new JLabel("Exam Name:");
        examLabel.setBounds(10, 40, 80, 20);
        add(examLabel);

        examNameField = new JTextField(20);
        examNameField.setBounds(100, 40, 100, 20);
        add(examNameField);

        // Marks label and text field
        marksLabel = new JLabel("Marks Obtained:");
        marksLabel.setBounds(210, 40, 100, 20);
        add(marksLabel);

        marksField = new JTextField(5);
        marksField.setBounds(310, 40, 50, 20);
        add(marksField);

        // Attendance label and text field
        attendanceLabel1 = new JLabel("Attendance 1st:");
        attendanceLabel1.setBounds(10, 70, 80, 20);
        add(attendanceLabel1);
        attendanceLabel2 = new JLabel("Attendance 2nd:");
        attendanceLabel2.setBounds(10, 100, 80, 20);
        add(attendanceLabel2);
        attendanceLabel3 = new JLabel("Attendance 3rd:");
        attendanceLabel3.setBounds(10, 130, 80, 20);
        add(attendanceLabel3);

        attendanceField1 = new JTextField(5);
        attendanceField1.setBounds(100, 70, 50, 20);
        add(attendanceField1);
        attendanceField2 = new JTextField(5);
        attendanceField2.setBounds(100, 100, 50, 20);
        add(attendanceField2);
        attendanceField3 = new JTextField(5);
        attendanceField3.setBounds(100, 130, 50, 20);
        add(attendanceField3);

        // Commit button
        commitButton = new JButton("Commit");
        commitButton.setPreferredSize(new Dimension(80, 20));
        commitButton.setBounds(370, 40, 80, 20);
        commitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String selected = (String) subjectComboBox.getSelectedItem();

                if(selected == null){
                    System.out.println("ERROR");
                }
                else if(data1.startsWith(selected)){
                    data1 += "," + examNameField.getText();
                    data1 += "," + marksField.getText();
                }else if(data2.startsWith(selected)){
                    data2 += "," + examNameField.getText();
                    data2 += "," + marksField.getText();
                }else if(data3.startsWith(selected)){
                    data3 += "," + examNameField.getText();
                    data3 += "," + marksField.getText();
                }else{
                    System.out.println("ERROR");
                }

                examNameField.setText("");
                marksField.setText("");

            }
        });
        add(commitButton);

        doneButton = new JButton("Done");
        doneButton.setPreferredSize(new Dimension(80, 20));
        doneButton.setBounds(370, 70, 80, 20);
        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Htree.CreateTree();
                System.out.println(data+","+data1+",attendance"+","+attendanceField1.getText()+","+data2+",attendance"+","+attendanceField2.getText()+","+data3+",attendance"+","+attendanceField3.getText());
                if(attendanceField1.getText().isEmpty() || attendanceField2.getText().isEmpty() || attendanceField3.getText().isEmpty()){
                    return;
                }
                Htree.writeToFile(data+","+data1+",attendance"+","+attendanceField1.getText()+","+data2+",attendance"+","+attendanceField2.getText()+","+data3+",attendance"+","+attendanceField3.getText());
                ((JDialog) SwingUtilities.getWindowAncestor(SubjectMenu.this)).dispose();
                updateUI();
            }
        });
        add(doneButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
