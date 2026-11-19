package util;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import model.GradeReportRow;
import model.Student;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PdfReportExporter {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Path exportReport(Student student, List<GradeReportRow> rows, double average, Path outputFile)
            throws IOException {
        Files.createDirectories(outputFile.getParent());
        try (OutputStream outputStream = Files.newOutputStream(outputFile)) {
            Document document = new Document(PageSize.A4, 42, 42, 42, 42);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            addHeader(document);
            addStudentSection(document, student);
            addGradesTable(document, rows);
            addAverage(document, average);
            addFooter(document);

            document.close();
            return outputFile;
        } catch (DocumentException exception) {
            throw new IOException("PDF generation failed: " + exception.getMessage(), exception);
        }
    }

    private void addHeader(Document document) throws DocumentException {
        Paragraph schoolTitle = new Paragraph(
                "JScolaris School Management",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, new Color(24, 34, 53))
        );
        schoolTitle.setAlignment(Element.ALIGN_CENTER);
        schoolTitle.setSpacingAfter(8);
        document.add(schoolTitle);

        Paragraph subtitle = new Paragraph(
                "Student Report Card",
                FontFactory.getFont(FontFactory.HELVETICA, 13, new Color(80, 91, 112))
        );
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(22);
        document.add(subtitle);
    }

    private void addStudentSection(Document document, Student student) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1.2f, 2.8f});
        addInfoRow(table, "Student", student.getFullName());
        addInfoRow(table, "Email", student.getEmail());
        addInfoRow(table, "Phone", student.getPhone());
        addInfoRow(table, "Class", student.getClassName());
        table.setSpacingAfter(18);
        document.add(table);
    }

    private void addGradesTable(Document document, List<GradeReportRow> rows) throws DocumentException {
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2f, 2f, 1f, 1f, 1f});
        addHeaderCell(table, "Module");
        addHeaderCell(table, "Subject");
        addHeaderCell(table, "Module Coef.");
        addHeaderCell(table, "Subject Coef.");
        addHeaderCell(table, "Grade");

        for (GradeReportRow row : rows) {
            addBodyCell(table, row.getModuleName(), Element.ALIGN_LEFT);
            addBodyCell(table, row.getSubjectName(), Element.ALIGN_LEFT);
            addBodyCell(table, DECIMAL_FORMAT.format(row.getModuleCoefficient()), Element.ALIGN_CENTER);
            addBodyCell(table, DECIMAL_FORMAT.format(row.getSubjectCoefficient()), Element.ALIGN_CENTER);
            addBodyCell(table, DECIMAL_FORMAT.format(row.getGradeValue()) + "/20", Element.ALIGN_CENTER);
        }

        if (rows.isEmpty()) {
            PdfPCell cell = new PdfPCell(new Phrase("No grades assigned yet."));
            cell.setColspan(5);
            cell.setPadding(10);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderColor(new Color(215, 225, 238));
            table.addCell(cell);
        }

        table.setSpacingAfter(18);
        document.add(table);
    }

    private void addAverage(Document document, double average) throws DocumentException {
        Paragraph paragraph = new Paragraph(
                "General Average: " + DECIMAL_FORMAT.format(average) + "/20",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new Color(24, 34, 53))
        );
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        paragraph.setSpacingAfter(22);
        document.add(paragraph);
    }

    private void addFooter(Document document) throws DocumentException {
        Paragraph footer = new Paragraph(
                "Generated on " + LocalDateTime.now().format(DATE_FORMAT),
                FontFactory.getFont(FontFactory.HELVETICA, 10, new Color(98, 112, 138))
        );
        footer.setAlignment(Element.ALIGN_RIGHT);
        document.add(footer);
    }

    private void addInfoRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11)));
        labelCell.setPadding(8);
        labelCell.setBorderColor(new Color(215, 225, 238));
        labelCell.setBackgroundColor(new Color(245, 247, 251));
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, FontFactory.getFont(FontFactory.HELVETICA, 11)));
        valueCell.setPadding(8);
        valueCell.setBorderColor(new Color(215, 225, 238));
        table.addCell(valueCell);
    }

    private void addHeaderCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE)));
        cell.setBackgroundColor(new Color(24, 34, 53));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(8);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }

    private void addBodyCell(PdfPTable table, String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA, 10)));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(8);
        cell.setBorderColor(new Color(215, 225, 238));
        table.addCell(cell);
    }
}
