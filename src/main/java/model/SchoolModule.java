package model;

public class SchoolModule {
    private int id;
    private String moduleName;
    private double coefficient;

    public SchoolModule() {
    }

    public SchoolModule(int id, String moduleName, double coefficient) {
        this.id = id;
        this.moduleName = moduleName;
        this.coefficient = coefficient;
    }

    public SchoolModule(String moduleName, double coefficient) {
        this(0, moduleName, coefficient);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    @Override
    public String toString() {
        return moduleName;
    }
}
