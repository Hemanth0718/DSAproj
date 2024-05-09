package Menus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import DSA.Node;
import Panels.Display;

public class AddStudent extends JPanel {

    private JLabel nameLabel, semLabel, yearLabel, branchLabel, departmentLabel;
    private JTextField nameField, semField, yearField, branchField, departmentField;
    private JButton confirmButton, cancelButton;

    public String name;
    public String branch;
    public String department;
    public int semester;
    public int year;
    public AddStudent() {

        setLayout(null);

        nameLabel = new JLabel("Name:");
        nameLabel.setBounds(10, 10, 80, 20);
        add(nameLabel);

        nameField = new JTextField(20);
        nameField.setBounds(100, 10, 150, 20);
        add(nameField);

        semLabel = new JLabel("Semester:");
        semLabel.setBounds(10, 40, 80, 20);
        add(semLabel);

        semField = new JTextField(5);
        semField.setBounds(100, 40, 50, 20);
        add(semField);

        yearLabel = new JLabel("Year:");
        yearLabel.setBounds(160, 40, 40, 20);
        add(yearLabel);

        yearField = new JTextField(5);
        yearField.setBounds(200, 40, 50, 20);
        add(yearField);

        branchLabel = new JLabel("Branch:");
        branchLabel.setBounds(10, 70, 80, 20);
        add(branchLabel);

        branchField = new JTextField(20);
        branchField.setBounds(100, 70, 150, 20);
        add(branchField);

        departmentLabel = new JLabel("Department:");
        departmentLabel.setBounds(10, 100, 80, 20);
        add(departmentLabel);

        departmentField = new JTextField(20);
        departmentField.setBounds(100, 100, 150, 20);
        add(departmentField);

        confirmButton = new JButton("Confirm");
        confirmButton.setPreferredSize(new Dimension(80, 30));
        confirmButton.setBounds(10, 130, 80, 30);
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                name = nameField.getText();
                branch = branchField.getText();
                department = departmentField.getText();
                try {
                    semester = Integer.parseInt(semField.getText());
                    year = Integer.parseInt(yearField.getText());
                    System.out.println("Adding student: " + name + ", Semester: " + semester + ", Year: " + year + ", Branch: " + branch + ", Department: " + department);
                    if (year * 2 != semester && year * 2 - 1 != semester) {
                        System.out.println("Invalid Semester");
                        return;
                    }
                    nameField.setText("");
                    semField.setText("");
                    yearField.setText("");
                    branchField.setText("");
                    departmentField.setText("");

                    ((JDialog) SwingUtilities.getWindowAncestor(AddStudent.this)).dispose();
                } catch (NumberFormatException ex) {
                    System.out.println("Error: Invalid semester or year format.");
                }
            }
        });
        add(confirmButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(80, 30));
        cancelButton.setBounds(100, 130, 80, 30);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((JDialog) SwingUtilities.getWindowAncestor(AddStudent.this)).dispose();
            }
        });
        add(cancelButton);
    }
}
