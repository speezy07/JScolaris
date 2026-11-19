package model;

public class GradeReportRow {
    private final int gradeId;
    private final int subjectId;
    private final String moduleName;
    private final String subjectName;
    private final double moduleCoefficient;
    private final double subjectCoefficient;
    private final double gradeValue;

    public GradeReportRow(
            int gradeId,
            int subjectId,
            String moduleName,
            String subjectName,
            double moduleCoefficient,
            double subjectCoefficient,
            double gradeValue
    ) {
        this.gradeId = gradeId;
        this.subjectId = subjectId;
        this.moduleName = moduleName;
        this.subjectName = subjectName;
        this.moduleCoefficient = moduleCoefficient;
        this.subjectCoefficient = subjectCoefficient;
        this.gradeValue = gradeValue;
    }

    public int getGradeId() {
        return gradeId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public double getModuleCoefficient() {
        return moduleCoefficient;
    }

    public double getSubjectCoefficient() {
        return subjectCoefficient;
    }

    public double getGradeValue() {
        return gradeValue;
    }

    public double getWeightedCoefficient() {
        return moduleCoefficient * subjectCoefficient;
    }
}
