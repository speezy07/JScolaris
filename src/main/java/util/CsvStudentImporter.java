package util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvStudentImporter {
    public ImportResult importStudents(Path filePath) throws IOException {
        try (Reader reader = Files.newBufferedReader(filePath);
             CSVReader csvReader = new CSVReader(reader)) {
            List<String[]> rows = csvReader.readAll();
            return StudentImportSupport.validateRows(rows);
        } catch (CsvException exception) {
            throw new IOException("CSV parsing failed: " + exception.getMessage(), exception);
        }
    }
}
