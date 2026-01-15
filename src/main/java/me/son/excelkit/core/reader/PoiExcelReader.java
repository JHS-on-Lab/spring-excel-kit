package me.son.excelkit.core.reader;

import me.son.excelkit.core.util.ExcelValueConverter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

public class PoiExcelReader implements ExcelReader {
    @Override
    public <T> List<T> read(InputStream inputStream, Class<T> type) {

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (!rows.hasNext()) {
                return List.of();
            }

            // Header 읽기
            Row headerRow = rows.next();
            Map<Integer, String> headerMap = extractHeader(headerRow);

            // 필드 캐싱
            Map<String, Field> fieldMap = getFieldMap(type);

            List<T> result = new ArrayList<>();

            // 데이터 행 처리
            while (rows.hasNext()) {
                Row row = rows.next();

                if (isEmptyRow(row)) {
                    continue; // 빈 행 스킵
                }

                T instance = type.getDeclaredConstructor().newInstance();

                for (Map.Entry<Integer, String> entry : headerMap.entrySet()) {
                    int columnIndex = entry.getKey();
                    String headerName = entry.getValue();

                    Field field = fieldMap.get(headerName);
                    if (field == null) {
                        continue; // DTO에 없는 컬럼은 무시
                    }

                    Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    Object value = ExcelValueConverter.convert(cell, field.getType());

                    field.setAccessible(true);
                    field.set(instance, value);
                }

                result.add(instance);
            }

            return result;

        } catch (Exception e) {
            throw new IllegalStateException("Failed to read Excel file", e);
        }
    }

    private Map<Integer, String> extractHeader(Row headerRow) {
        Map<Integer, String> headerMap = new HashMap<>();
        for (Cell cell : headerRow) {
            headerMap.put(
                    cell.getColumnIndex(),
                    cell.getStringCellValue().trim().toLowerCase()
            );
        }
        return headerMap;
    }

    private Map<String, Field> getFieldMap(Class<?> type) {
        Map<String, Field> map = new HashMap<>();
        for (Field field : type.getDeclaredFields()) {
            map.put(field.getName().toLowerCase(), field);
        }
        return map;
    }

    private boolean isEmptyRow(Row row) {
        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
}
