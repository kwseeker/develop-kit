package top.kwseeker.developkit.excelutil;

import java.lang.annotation.*;

/**
 * 用在实体类上，指定文件对应的sheet
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Tab {

    /** 要解析的页名 */
    String name() default "";

    /** 要解析的页的索引 */
    int sheetIndex() default 0;

    /** 标题行索引 */
    int titleRow() default 0;

    /** 数据行起始索引 */
    int dataBeginRow() default 1;
}

