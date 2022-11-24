package top.kwseeker.developkit.excelutil;

import top.kwseeker.developkit.excelutil.validate.Validator;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 实体类 和 Excel列 的关联
 */
public class FieldColumnRelation {

    /** 实体类字段 */
    private Field field;
    /** 实体类字段对应Excel列索引 */
    private int columnIndex;
    /** 实体类字段对应Excel列的标题 */
    private String columnTitle;
    /** 实体类字段内容校验器 */
    private List<Validator> validators;

    public FieldColumnRelation(Field field, int columnIndex, String columnTitle, List<Validator> validators) {
        this.field = field;
        this.columnIndex = columnIndex;
        this.columnTitle = columnTitle;
        this.validators = validators;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public String getColumnTitle() {
        return columnTitle;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }

    public List<Validator> getValidators() {
        return validators;
    }

    public void setValidators(List<Validator> validators) {
        this.validators = validators;
    }
}
