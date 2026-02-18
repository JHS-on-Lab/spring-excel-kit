package me.son.excelkit.core.writer;

import java.io.OutputStream;
import java.util.List;

public interface ExcelWriter {
    <T> void write(OutputStream outputStream, List<T> data, Class<T> type);
}
