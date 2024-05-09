package DSA;

import DSA.Node;
import DSA.Student;
import Panels.SubjectDetails;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Tree {
    Scanner sc = new Scanner(System.in);
    public Node root;

    public Tree() {
        Path path = Paths.get("Amrita.txt");

        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
        }
        root = new Node(path.toString());
    }

    void removeSpace() {
        try {
            Path filePath = Paths.get((String) root.data);
            List<String> lines = Files.readAllLines(filePath);
            lines.removeIf(String::isEmpty);
            Files.write(filePath, lines);
        } catch (IOException e) {
            System.out.println("Error removing empty lines: " + e.getMessage());
        }
    }

    void addNode() {
        String c_name;
        int c_sem;
        String c_Branch;
        String c_department;
        int c_year;
        System.out.println("Enter the name of the student");
        c_name = sc.nextLine();

        System.out.println("Enter the Branch of the student");
        c_Branch = sc.next();
        System.out.println("Enter the department of the student");
        c_department = sc.next();
        System.out.println("Enter Year: ");
        c_year = sc.nextInt();
        do {
            System.out.println("Enter Semester: ");
            c_sem = sc.nextInt();
            if (c_year != c_sem / 2 && c_year != c_sem / 2 + 1) {
                System.out.println("Invalid Semester");
            }
        } while (c_year != c_sem / 2 && c_year != c_sem / 2 + 1);

        String data = c_Branch + "," + c_department + "," + c_year + "," + c_sem + "," + c_name;

        Student student = new Student(c_name, c_sem);
        student.addSubjects();
        for (String sub : student.subjects) {
            data += "," + sub;
            System.out.println("Enter marks for " + sub + ": ");
            int c = 0;
            while (c < 3) {
                System.out.println("Enter exam name (or 'done' to finish): ");
                String exam = sc.next();

                if (exam.equals("done")) {
                    break;
                }
                data += "," + exam;
                System.out.println("Enter mark: ");
                int mark = sc.nextInt();
                sc.nextLine();
                data += "," + mark;
                c++;

            }
            System.out.println("Enter attendance for " + sub + ": ");
            int attendance = sc.nextInt();
            data += ",attendance";
            data += "," + attendance;
            sc.nextLine();

        }

        writeToFile(data);
    }

    void addNode(String c_name, String c_Branch, String c_department, int c_sem, int c_year) {

        if (c_year * 2 != c_sem && c_year * 2 - 1 != c_sem) {
            System.out.println("Invalid Semester");
            return;
        }

        String data = c_department + "," + c_Branch + "," + c_year + "," + c_sem + "," + c_name;

        Student student = new Student(c_name, c_sem);
        student.addSubjects();
    }


    public static Node findNode(Node node, String data) {
        if (node.data.equals(data)) {
            return node;
        }

        if (node.data.toString().equals(data)) {
            return node;
        }

        for (Node child : node.children) {
            Node result = findNode(child, data);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    long getNumberOfLines() {
        removeSpace();
        try {
            return Files.lines(Paths.get((String) root.data)).count();
        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
            return -1;
        }
    }

    public void writeToFile(String content) {
        try {
            Files.write(Paths.get((String) root.data), (content + "\n").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public List<String> printTree(Node node) {
        List<String> result = new ArrayList<>();

        for (Node child : node.children) {
            String return_string;
            if (child.data instanceof SubjectDetails) {
                SubjectDetails subjectDetails = (SubjectDetails) child.data;
                return_string = subjectDetails.subjectName + " " + subjectDetails.marks + " " + subjectDetails.attendance;
            } else {
                return_string = child.data.toString();
            }
            result.add(return_string);
            result.addAll(printTree(child));
        }

        return result;
    }

    public void CreateTree() {

        for (int i = 0; i < getNumberOfLines(); i++) {

            String line = null;
            try {
                line = Files.readAllLines(Paths.get((String) root.data)).get(i);
            } catch (IOException e) {
                System.out.println("Error reading from file: " + e.getMessage());
            }

            String[] parts = line.split(",");

            String department = parts[1];
            String Branch = parts[0];
            int year = Integer.parseInt(parts[2]);
            int sem = Integer.parseInt(parts[3]);
            String name = parts[4];

            Node BranchNode = findNode(root, Branch);
            if (BranchNode == null) {
                BranchNode = new Node(Branch);
                root.children.add(BranchNode);
            }

            Node departmentNode = findNode(BranchNode, department);
            if (departmentNode == null) {
                departmentNode = new Node(department);
                BranchNode.children.add(departmentNode);
            }
            Student student = new Student(name, sem);
            student.addSubjects();
            Node semesterNode = findNode(departmentNode, String.valueOf(sem));
            if (semesterNode == null) {

                semesterNode = new Node(String.valueOf(sem));
                departmentNode.children.add(semesterNode);
            }
            Node studentNode = new Node(student.name);
            semesterNode.children.add(studentNode);
            for (String subject : student.subjects) {

                Node subjectNode = new Node(subject);
                studentNode.children.add(subjectNode);
            }

            int j = 5;
            while (j < parts.length) {
                HashMap<String, Integer> marks = new HashMap<>();
                String subject = parts[j];
                int c = 0;
                for (int k = j + 1; i < parts.length; k += 2) {

                    if (parts[k].equals("attendance")) {
                        break;
                    } else {
                        marks.put(parts[k], Integer.parseInt(parts[k + 1]));
                        c++;

                    }

                }
                int attendance = Integer.parseInt(parts[j + 2 * c + 2]);
                Node subjectNode = findNode(studentNode, subject);
                if (subjectNode == null) {
                    System.out.println("Subject not found: " + subject);
                } else {
                    subjectNode.data = new SubjectDetails(subject, marks, attendance);
                }
                j += 2 * c + 3;

            }

        }

    }
    public void depthFirstTraversal(Node node) {
        if (node != null) {
            System.out.println(node.data); // Visit the node
            for (Node child : node.children) { // Visit all children
                depthFirstTraversal(child);
            }

        }
    }

//    public void deleteNode(String data) {
//        data = data.replace("Amrita.txt,", "");
//        String[] dataList = data.split(",");
//
//        for (int i = 0; i < getNumberOfLines(); i++) {
//            String line = null;
//            try {
//                line = Files.readAllLines(Paths.get((String) root.data)).get(i);
//            } catch (IOException e) {
//                System.out.println("Error reading from file: " + e.getMessage());
//            }
//            boolean flag = true;
//            for(String child : dataList){
//                flag = flag && line.contains(child.substring(1));
//            }
//            System.out.println(flag);
//
//            if (flag) {
//                try {
//                    Path path = Paths.get((String) root.data);
//                    List<String> lines = Files.readAllLines(path);
//                    System.out.println(lines.get(i));
//                    lines.set(i, "");
//                    Files.write(path, lines);
//                } catch (IOException e) {
//                    System.out.println("Error deleting line: " + e.getMessage());
//                }
//            }
//        }
//    }

    public String Standardize(String var1) {
        var1 = var1.replace("Amrita.txt,", "");
        String[] var2 = var1.split(",");
        String[] var3 = new String[var2.length + 1];

        int var4;
        for(var4 = 0; var4 < 2; ++var4) {
            var3[var4] = var2[var4];
        }

        var2[2] = var2[2].substring(1, var2[2].length());
        var4 = Integer.parseInt(var2[2]);
        var3[2] = "" + (var4+1) / 2;

        int var5;
        for(var5 = 3; var5 < var3.length; ++var5) {
            var3[var5] = var2[var5 - 1];
        }

        for(var5 = 0; var5 < var3.length; ++var5) {
            var3[var5] = var3[var5].trim();
        }

        var1 = String.join(",", var3);
        return var1;
    }

    public void deleteNode(String var1) {
        var1 = this.Standardize(var1);
        System.out.println(var1);

        for(int var2 = 0; (long)var2 < this.getNumberOfLines(); ++var2) {
            String var3 = null;

            try {
                var3 = (String)Files.readAllLines(Paths.get((String)this.root.data)).get(var2);
            } catch (IOException var8) {
                System.out.println("Error reading from file: " + var8.getMessage());
            }

            if (var3.contains(var1)) {
                try {
                    Path var4 = Paths.get((String)this.root.data);
                    List var5 = Files.readAllLines(var4);
                    int var6 = ((String)var5.get(var2)).indexOf(var1);
                    if (var6 != -1) {
                        var5.set(var2, ((String)var5.get(var2)).substring(0, var6));
                    }

                    Files.write(var4, var5);
                } catch (IOException var7) {
                    System.out.println("Error deleting line: " + var7.getMessage());
                }
            }
        }

        this.CreateTree();
    }



    public void updateTree(String name,String subject){
        String line = null;
        for (int i = 0; i < getNumberOfLines(); i++) {

            try {
                line = Files.readAllLines(Paths.get((String) root.data)).get(i);
            } catch (IOException e) {
                System.out.println("Error reading from file: " + e.getMessage());
            }
            if (line.contains(name)) {
                break;
            }

        }
        String[] parts = line.split(",");
        int j = 5;
        while (j < parts.length) {
            if (parts[j].equals(subject)) {
                break;
            }
            j += 2;
        }
        if (j >= parts.length) {
            System.out.println("Subject not found: " + subject);
        } else {
            System.out.println("Enter exam name: ");
            String exam = sc.next();
            System.out.println("Enter mark: ");
            int mark = sc.nextInt();
            parts[j + 1] = exam;
            parts[j + 2] = String.valueOf(mark);
            String newLine = String.join(",", parts);
            try {
                Path path = Paths.get((String) root.data);
                List<String> lines = Files.readAllLines(path);
                int i = lines.indexOf(line); // Replace oldLine with the line you want to replace
                if (i != -1) {
                    lines.set(i, newLine);
                }
                Files.write(path, lines);
            } catch (IOException e) {
                System.out.println("Error updating line: " + e.getMessage());
            }
        }

    }
}
