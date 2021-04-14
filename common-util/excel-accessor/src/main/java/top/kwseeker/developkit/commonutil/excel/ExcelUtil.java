package top.kwseeker.developkit.commonutil.excel;

import top.kwseeker.developkit.commonutil.excel.write.ExcelWriterBuilder;

import java.util.Objects;

public class ExcelUtil {

    public static <T> ExcelWriterBuilder write(String pathName, Class<T> clazz) {
        Objects.requireNonNull(pathName);
        Objects.requireNonNull(clazz);
        ExcelWriterBuilder builder = new ExcelWriterBuilder();
        builder.file(pathName);
        builder.head(clazz);
        return builder;
    }
}
