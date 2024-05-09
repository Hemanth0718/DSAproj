import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class SubjectDetails {
  String subjectName;
  HashMap<String, Integer> marks;
  int attendance;

  SubjectDetails(String subjectName, HashMap<String, Integer> marks, int attendance) {
    this.subjectName = subjectName;
    this.marks = marks;
    this.attendance = attendance;
  }
}

class Node {
  Object data;
  List<Node> children;

  Node(String data) {
    this.data = data;
    this.children = new ArrayList<>();
  }

  Node(String subjectName, HashMap<String, Integer> marks, int attendance) {
    this.data = new SubjectDetails(subjectName, marks, attendance);
    this.children = new ArrayList<>();
  }

  void addChild(Node child) {
    children.add(child);
  }

}

class Student {
  String name;
  int semester;
  List<String> subjects;
  HashMap<String, Integer> marks;

  Student(String name, int semester) {
    this.name = name;
    this.semester = semester;
    this.subjects = new ArrayList<>();
  }

  void addSubjects() {

    Map<Integer, List<String>> semesterSubjects = new HashMap<>();
    semesterSubjects.put(1, Arrays.asList("Subject1", "Subject2", "Subject3"));
    semesterSubjects.put(2, Arrays.asList("Subject4", "Subject5", "Subject6"));
    semesterSubjects.put(3, Arrays.asList("Subject7", "Subject8", "Subject9"));
    semesterSubjects.put(4, Arrays.asList("Subject10", "Subject11", "Subject12"));
    semesterSubjects.put(5, Arrays.asList("Subject13", "Subject14", "Subject15"));
    semesterSubjects.put(6, Arrays.asList("Subject16", "Subject17", "Subject18"));
    semesterSubjects.put(7, Arrays.asList("Subject19", "Subject20", "Subject21"));
    semesterSubjects.put(8, Arrays.asList("Subject22", "Subject23", "Subject24"));

    if (semesterSubjects.containsKey(semester)) {
      subjects.addAll(semesterSubjects.get(semester));
    } else {
      System.out.println("Invalid semester: " + semester);
    }
  }

  void addMark(String exam, int mark) {
    marks.put(exam, mark);
  }

}

class Tree {
  Scanner sc = new Scanner(System.in);
  Node root;

  Tree() {
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

  Node findNode(Node node, String data) {
    if (node.data.equals(data)) {
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

  void writeToFile(String content) {
    try {
      Files.write(Paths.get((String) root.data), (content + "\n").getBytes(), StandardOpenOption.APPEND);
    } catch (IOException e) {
      System.out.println("Error writing to file: " + e.getMessage());
    }
  }

  List<String> printTree(Node node) {

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

  void CreateTree() {

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
      // int year = Integer.parseInt(parts[2]);
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

  public String Standardize(String data) {
    data = data.replace("Amrita.txt,", "");

    String parts[] = data.split(",");
    if (parts.length < 3) {
      for (int i = 0; i < parts.length; i++) {
        parts[i] = parts[i].trim();
      }
      data = String.join(",", parts);
      return data;
    } else {
      String[] newParts = new String[parts.length + 1];
      for (int i = 0; i < 2; i++) {
        newParts[i] = parts[i];
      }
      parts[2] = parts[2].substring(1, parts[2].length());

      double v = Double.parseDouble(parts[2]);
      newParts[2] = (int) Math.ceil(v / 2) + "";

      for (int i = 3; i < newParts.length; i++) {
        newParts[i] = parts[i - 1];
      }
      for (int i = 0; i < newParts.length; i++) {
        newParts[i] = newParts[i].trim();
      }

      data = String.join(",", newParts);
      return data;
    }
  }

  public void deleteNode(String data) {
    data = Standardize(data);
    ArrayList<String> lines = new ArrayList<>();
    for (int i = 0; i < getNumberOfLines(); i++) {

      try {
        lines.add(Files.readAllLines(Paths.get((String) root.data)).get(i));
      } catch (IOException e) {
        System.out.println("Error reading from file: " + e.getMessage());
      }
    }
    try {
      PrintWriter writer = new PrintWriter((String) root.data);
      writer.print("");
      writer.close();
    } catch (FileNotFoundException e) {
      System.out.println("Error emptying file: " + e.getMessage());
    }
    ArrayList<String> newLines = new ArrayList<>();
    System.out.println(data);
    for (String s : lines) {
      if (s.contains(data)) {
        continue;
      }
      newLines.add(s);

    }
    for (String s : newLines) {
      writeToFile(s);

    }

  }

  public void updateTree(String path, String tbChanged) {
    path = Standardize(path);

    String parts[] = path.split(",");
    ArrayList<String> lines = new ArrayList<>();
    for (int i = 0; i < getNumberOfLines(); i++) {

      try {
        lines.add(Files.readAllLines(Paths.get((String) root.data)).get(i));
      } catch (IOException e) {
        System.out.println("Error reading from file: " + e.getMessage());
      }
    }
    if (lines.isEmpty()) {
      return;
    }
    try {
      PrintWriter writer = new PrintWriter((String) root.data);
      writer.print("");
      writer.close();
    } catch (FileNotFoundException e) {
      System.out.println("Error emptying file: " + e.getMessage());
    }
    System.out.println(parts.length);
    if (tbChanged == "exam") {

      String subject = parts[parts.length - 1];
      System.out.println(subject);
      System.out.println(path);
      path = path.replace("," + subject, "");
      ArrayList<String> newLines = new ArrayList<>();
      for (String s : lines) {
        System.out.println(s);
        if (s.contains(path)) {
          String parts1[] = s.split(",");
          int j = 0;

          System.out.println("Enter Exam Name:");
          String exam = sc.next();
          System.out.println("Enter Marks:");
          int marks = sc.nextInt();
          while (j < parts1.length - 1) {
            System.out.println(parts1[j]);
            if (parts1[j].equals(subject)) {
              break;
            }
            j++;
          }
          int flag = 0;
          while (j < parts1.length - 1 && !parts1[j].equals("attendance")) {
            if (parts1[j].equals(exam)) {
              flag = 1;
              parts1[j + 1] = marks + "";
              break;
            }
            j++;
          }
          String newLine = "";
          if (flag == 0) {
            String parts2[] = new String[parts1.length + 2];
            for (int i = 0; i < j; i++) {
              parts2[i] = parts1[i];
            }
            parts2[j] = exam;
            parts2[j + 1] = marks + "";
            for (int i = j + 2; i < parts2.length; i++) {
              parts2[i] = parts1[i - 2];
            }
            newLine = String.join(",", parts2);
          }

          newLines.add(newLine);

        } else {
          newLines.add(s);
        }

      }
      for (String s : newLines) {
        writeToFile(s);

      }
    } else if (tbChanged == "attendance") {
      String subject = parts[parts.length - 1];

      path = path.replace("," + subject, "");
      ArrayList<String> newLines = new ArrayList<>();
      System.out.println(subject);
      for (String s : lines) {
        System.out.println(s);
        if (s.contains(path)) {
          String parts1[] = s.split(",");
          int j = 0;

          while (j < parts1.length - 1) {

            if (parts1[j].equals(subject)) {
              break;
            }
            j++;
          }
          
          while (j < parts1.length - 1 && !parts1[j].equals("attendance")) {
            j++;
          }
          System.out.println(j);
          System.out.println(parts1[j]);
          System.out.println("Enter Updated Attendance: ");
          int updatedAttendance = sc.nextInt();
          parts1[j + 1] = updatedAttendance + "";
          String newLine = String.join(",", parts1);
          newLines.add(newLine);

        } else {
          newLines.add(s);
        }

      }
      for (String s : newLines) {
        writeToFile(s);

      }

    } else {
      System.out.println("Enter Updated Value: ");
      String updatedValue = sc.nextLine();
      ArrayList<String> newLines = new ArrayList<>();
      for (String s : lines) {
        System.out.println(s);
        if (s.contains(path)) {
          String parts1[] = s.split(",");
          for (int i = 0; i < parts1.length; i++) {
            if (parts1[i].equals(parts[parts.length - 1])) {
              parts1[i] = updatedValue;
            }

          }
          String newLine = String.join(",", parts1);
          newLines.add(newLine);
          System.out.println(newLine);
        } else {
          newLines.add(s);
        }

      }
      for (String s : newLines) {
        writeToFile(s);

      }
    }

  }
}

public class DSA {
  public static void main(String[] args) {
    Tree tree = new Tree();

    tree.CreateTree();
    tree.updateTree("Amrita.txt, BLR, CSE, 3, Sricharan, Subject7", "exam");
    // tree.updateTree("Amrita.txt, BLR, CSE, 8, Hemanth","Hem");
    System.out.println(tree.printTree(tree.root));
    // tree.CreateTree();
    // System.out.println(tree.printTree(tree.root));
    // tree.updateTree("Hemanth", "Subject10");
    // tree.updateTree("Hemanth", null);

  }
}
