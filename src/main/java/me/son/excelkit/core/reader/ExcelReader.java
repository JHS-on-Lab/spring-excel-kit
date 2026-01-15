package me.son.excelkit.core.reader;

import java.io.InputStream;
import java.util.List;

public interface ExcelReader {
    <T> List<T> read(InputStream inputStream, Class<T> type);
}
