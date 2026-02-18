package me.son.excelkit.sample.config;

import me.son.excelkit.core.reader.ExcelReader;
import me.son.excelkit.core.reader.PoiExcelReader;
import me.son.excelkit.core.writer.ExcelWriter;
import me.son.excelkit.core.writer.PoiExcelWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExcelKitConfig {

    @Bean
    public ExcelReader excelReader() {
        return new PoiExcelReader();
    }

    @Bean
    public ExcelWriter excelWriter() {
        return new PoiExcelWriter();
    }
}
