package top.kwseeker.developkit.excelutil;

import java.lang.annotation.*;

/**
 * 用在实体类字段上，指定field对应的列名
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {

    String title() default "";

    boolean isPrimary() default false;
}