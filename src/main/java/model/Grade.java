package model;

public class Grade {
    private int id;
    private int studentId;
    private int subjectId;
    private double gradeValue;

    public Grade() {
    }

    public Grade(int id, int studentId, int subjectId, double gradeValue) {
        this.id = id;
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.gradeValue = gradeValue;
    }

    public Grade(int studentId, int subjectId, double gradeValue) {
        this(0, studentId, subjectId, gradeValue);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public double getGradeValue() {
        return gradeValue;
    }

    public void setGradeValue(double gradeValue) {
        this.gradeValue = gradeValue;
    }
}
