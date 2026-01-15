package me.son.excelkit.core.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

public class ExcelValueConverter {

    public static Object convert(Cell cell, Class<?> targetType) {
        if (cell == null) {
            return null;
        }

        CellType cellType = cell.getCellType();

        if (targetType == String.class) {
            return cell.toString().trim();
        }

        if (targetType == Integer.class) {
            if (cellType == CellType.NUMERIC) {
                return (int) cell.getNumericCellValue();
            }
            return Integer.valueOf(cell.getStringCellValue().trim());
        }

        if (targetType == Long.class) {
            if (cellType == CellType.NUMERIC) {
                return (long) cell.getNumericCellValue();
            }
            return Long.valueOf(cell.getStringCellValue().trim());
        }

        if (targetType == Double.class) {
            return cell.getNumericCellValue();
        }

        // 기본 fallback
        return cell.toString();
    }
}
