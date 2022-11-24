package top.kwseeker.developkit.excelutil;

import top.kwseeker.developkit.excelutil.validate.ValidateType;

import java.lang.annotation.*;

/**
 * 用在实体类字段上，指定field对应的列名
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {

    String title() default "";

    /* 是否是主要列,类似DB的主键 */
    boolean isPrimary() default false;

    /* 列是否有特殊检查需求 */
    ValidateType[] validateTypes() default {};
}