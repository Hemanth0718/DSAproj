package DSA;

import Panels.SubjectDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Node {

    public Object data;
    public List<Node> children;

    public Node(String data) {
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

    @Override
    public String toString() {
        return this.data.toString();
    }

}
