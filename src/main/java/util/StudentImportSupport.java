package util;

import model.Student;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

final class StudentImportSupport {
    private static final int EXPECTED_COLUMN_COUNT = 5;
    private static final List<String> EXPECTED_HEADERS = List.of("firstname", "lastname", "email", "phone", "classname");

    private StudentImportSupport() {
    }

    static ImportResult validateRows(List<String[]> rows) {
        List<Student> students = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        Set<String> emailsInFile = new HashSet<>();
        int skippedEmptyRows = 0;

        if (rows.isEmpty()) {
            errors.add("The file is empty.");
            return new ImportResult(students, errors, skippedEmptyRows);
        }

        String[] headers = rows.get(0);
        if (!isValidHeader(headers)) {
            errors.add("Invalid columns. Expected header: firstName,lastName,email,phone,className.");
            return new ImportResult(students, errors, skippedEmptyRows);
        }

        for (int rowIndex = 1; rowIndex < rows.size(); rowIndex++) {
            int displayRow = rowIndex + 1;
            String[] row = rows.get(rowIndex);
            if (isEmptyRow(row)) {
                skippedEmptyRows++;
                continue;
            }
            if (row.length != EXPECTED_COLUMN_COUNT) {
                errors.add("Row " + displayRow + ": invalid column count. Expected 5 columns.");
                continue;
            }

            Student student = new Student(
                    ValidationUtil.clean(row[0]),
                    ValidationUtil.clean(row[1]),
                    ValidationUtil.clean(row[2]).toLowerCase(Locale.ROOT),
                    ValidationUtil.clean(row[3]),
                    ValidationUtil.clean(row[4])
            );

            List<String> rowErrors = ValidationUtil.validateStudent(student);
            String emailKey = student.getEmail().toLowerCase(Locale.ROOT);
            if (!ValidationUtil.isBlank(emailKey) && emailsInFile.contains(emailKey)) {
                rowErrors.add("Duplicate email in file: " + student.getEmail() + ".");
            }

            if (rowErrors.isEmpty()) {
                students.add(student);
                emailsInFile.add(emailKey);
            } else {
                for (String error : rowErrors) {
                    errors.add("Row " + displayRow + ": " + error);
                }
            }
        }

        return new ImportResult(students, errors, skippedEmptyRows);
    }

    private static boolean isValidHeader(String[] headers) {
        if (headers == null || headers.length != EXPECTED_COLUMN_COUNT) {
            return false;
        }
        for (int index = 0; index < EXPECTED_COLUMN_COUNT; index++) {
            String normalized = normalizeHeader(headers[index]);
            if (!EXPECTED_HEADERS.get(index).equals(normalized)) {
                return false;
            }
        }
        return true;
    }

    private static String normalizeHeader(String header) {
        return ValidationUtil.clean(header)
                .replace("_", "")
                .replace(" ", "")
                .toLowerCase(Locale.ROOT);
    }

    private static boolean isEmptyRow(String[] row) {
        if (row == null || row.length == 0) {
            return true;
        }
        for (String value : row) {
            if (!ValidationUtil.isBlank(value)) {
                return false;
            }
        }
        return true;
    }
}
