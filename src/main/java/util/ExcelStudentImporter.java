package util;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ExcelStudentImporter {
    public ImportResult importStudents(Path filePath) throws IOException {
        List<String[]> rows = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        try (InputStream inputStream = Files.newInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getNumberOfSheets() == 0 ? null : workbook.getSheetAt(0);
            if (sheet == null) {
                return new ImportResult(List.of(), List.of("The workbook does not contain any sheets."), 0);
            }
            for (Row row : sheet) {
                int lastCell = Math.max(row.getLastCellNum(), 0);
                String[] values = new String[lastCell];
                for (int cellIndex = 0; cellIndex < lastCell; cellIndex++) {
                    values[cellIndex] = formatter.formatCellValue(row.getCell(cellIndex));
                }
                rows.add(values);
            }
            return StudentImportSupport.validateRows(rows);
        } catch (Exception exception) {
            if (exception instanceof IOException ioException) {
                throw ioException;
            }
            throw new IOException("Excel parsing failed: " + exception.getMessage(), exception);
        }
    }
}
