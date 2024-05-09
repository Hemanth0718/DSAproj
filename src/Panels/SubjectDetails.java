package Panels;

import java.util.HashMap;

public class SubjectDetails {
    public String subjectName;
    public HashMap<String, Integer> marks;
    public int attendance;

    public SubjectDetails(String subjectName, HashMap<String, Integer> marks, int attendance) {
        this.subjectName = subjectName;
        this.marks = marks;
        this.attendance = attendance;
    }

    @Override
    public String toString(){
        return this.subjectName;
    }
}
