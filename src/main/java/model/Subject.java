package model;

public class Subject {
    private int id;
    private String subjectName;
    private int moduleId;
    private double coefficient;

    public Subject() {
    }

    public Subject(int id, String subjectName, int moduleId, double coefficient) {
        this.id = id;
        this.subjectName = subjectName;
        this.moduleId = moduleId;
        this.coefficient = coefficient;
    }

    public Subject(String subjectName, int moduleId, double coefficient) {
        this(0, subjectName, moduleId, coefficient);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getModuleId() {
        return moduleId;
    }

    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    @Override
    public String toString() {
        return subjectName;
    }
}
