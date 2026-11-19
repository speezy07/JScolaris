package util;

import model.SchoolModule;
import model.Student;
import model.Subject;
import model.Teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class ValidationUtil {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private ValidationUtil() {
    }

    public static List<String> validateStudent(Student student) {
        List<String> errors = new ArrayList<>();
        requireText(student.getFirstName(), "First name", errors);
        requireText(student.getLastName(), "Last name", errors);
        requireEmail(student.getEmail(), errors);
        requireText(student.getPhone(), "Phone", errors);
        requireText(student.getClassName(), "Class name", errors);
        return errors;
    }

    public static List<String> validateTeacher(Teacher teacher) {
        List<String> errors = new ArrayList<>();
        requireText(teacher.getFirstName(), "First name", errors);
        requireText(teacher.getLastName(), "Last name", errors);
        requireText(teacher.getSpecialty(), "Specialty", errors);
        requireEmail(teacher.getEmail(), errors);
        return errors;
    }

    public static List<String> validateModule(SchoolModule module) {
        List<String> errors = new ArrayList<>();
        requireText(module.getModuleName(), "Module name", errors);
        requirePositive(module.getCoefficient(), "Coefficient", errors);
        return errors;
    }

    public static List<String> validateSubject(Subject subject) {
        List<String> errors = new ArrayList<>();
        requireText(subject.getSubjectName(), "Subject name", errors);
        if (subject.getModuleId() <= 0) {
            errors.add("A module must be selected.");
        }
        requirePositive(subject.getCoefficient(), "Coefficient", errors);
        return errors;
    }

    public static List<String> validateGrade(double gradeValue) {
        List<String> errors = new ArrayList<>();
        if (gradeValue < 0 || gradeValue > 20) {
            errors.add("Grade must be between 0 and 20.");
        }
        return errors;
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static double parseDouble(String value, String fieldName, List<String> errors) {
        if (isBlank(value)) {
            errors.add(fieldName + " is required.");
            return 0;
        }
        try {
            return Double.parseDouble(value.trim().replace(',', '.'));
        } catch (NumberFormatException exception) {
            errors.add(fieldName + " must be a number.");
            return 0;
        }
    }

    public static String clean(String value) {
        return value == null ? "" : value.trim();
    }

    public static String joinErrors(List<String> errors) {
        return String.join(System.lineSeparator(), errors);
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static void requireText(String value, String fieldName, List<String> errors) {
        if (isBlank(value)) {
            errors.add(fieldName + " is required.");
        }
    }

    private static void requireEmail(String email, List<String> errors) {
        if (isBlank(email)) {
            errors.add("Email is required.");
        } else if (!isValidEmail(email)) {
            errors.add("Email format is invalid.");
        }
    }

    private static void requirePositive(double value, String fieldName, List<String> errors) {
        if (value <= 0) {
            errors.add(fieldName + " must be greater than zero.");
        }
    }
}
