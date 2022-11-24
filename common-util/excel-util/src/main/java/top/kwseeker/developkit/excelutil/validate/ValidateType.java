package top.kwseeker.developkit.excelutil.validate;

/**
 * 校验类型
 * 对某些字段添加格式检查
 */
public enum ValidateType {
    //JSON格式校验
    JSON("json", new JSONValidator()),
    JSON_ARRAY("jsonArray", new JSONArrayValidator()),
    ;

    private final String name;
    private final Validator validator;

    ValidateType(String name, Validator validator) {
        this.name = name;
        this.validator = validator;
    }

    public String getName() {
        return name;
    }

    public Validator getValidator() {
        return validator;
    }
}
