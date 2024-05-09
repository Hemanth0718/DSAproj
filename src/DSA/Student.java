package DSA;

import java.util.*;

public class Student {
    String name;
    int semester;
    public List<String> subjects;
    HashMap<String, Integer> marks;

    public Student(String name, int semester) {
        this.name = name;
        this.semester = semester;
        this.subjects = new ArrayList<>();
    }

    public void addSubjects() {

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
