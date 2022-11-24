package top.kwseeker.developkit.excelutil.validate;

public interface Validator {

    boolean matchType(Class<?> clazz);

    boolean valid(Object columnContent);
}
