package me.son.excelkit.core.writer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class PoiExcelWriter implements ExcelWriter {
    @Override
    public <T> void write(OutputStream outputStream, List<T> data, Class<T> type) {
        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Sheet1");

            List<Field> fields = getFields(type);

            createHeader(sheet, fields);
            writeRows(sheet, data, fields);

            workbook.write(outputStream);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to write Excel file", e);
        }
    }

    private List<Field> getFields(Class<?> type) {
        return Arrays.stream(type.getDeclaredFields())
                .peek(f -> f.setAccessible(true))
                .toList();
    }

    private void createHeader(Sheet sheet, List<Field> fields) {
        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < fields.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(fields.get(i).getName());
        }
    }

    private <T> void writeRows(Sheet sheet, List<T> data, List<Field> fields)
            throws IllegalAccessException {

        int rowIndex = 1;

        for (T item : data) {

            Row row = sheet.createRow(rowIndex++);

            for (int col = 0; col < fields.size(); col++) {

                Field field = fields.get(col);
                Object value = field.get(item);

                Cell cell = row.createCell(col);
                writeCellValue(cell, value);
            }
        }
    }

    private void writeCellValue(Cell cell, Object value) {

        if (value == null) {
            return;
        }

        if (value instanceof String s) {
            cell.setCellValue(s);
        } else if (value instanceof Integer i) {
            cell.setCellValue(i);
        } else if (value instanceof Long l) {
            cell.setCellValue(l);
        } else if (value instanceof Double d) {
            cell.setCellValue(d);
        } else if (value instanceof Boolean b) {
            cell.setCellValue(b);
        } else if (value instanceof BigDecimal bd) {
            cell.setCellValue(bd.doubleValue());
        } else if (value instanceof LocalDate ld) {
            cell.setCellValue(ld.toString());
        } else if (value instanceof LocalDateTime ldt) {
            cell.setCellValue(ldt.toString());
        } else if (value instanceof Enum<?> e) {
            cell.setCellValue(e.name());
        } else {
            cell.setCellValue(value.toString());
        }
    }
}
