package util;

import model.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImportResult {
    private final List<Student> students;
    private final List<String> errors;
    private final int skippedEmptyRows;

    public ImportResult(List<Student> students, List<String> errors, int skippedEmptyRows) {
        this.students = new ArrayList<>(students);
        this.errors = new ArrayList<>(errors);
        this.skippedEmptyRows = skippedEmptyRows;
    }

    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public int getSkippedEmptyRows() {
        return skippedEmptyRows;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public String summary() {
        return "Valid students: " + students.size()
                + System.lineSeparator()
                + "Skipped empty rows: " + skippedEmptyRows
                + System.lineSeparator()
                + "Errors: " + errors.size();
    }
}
