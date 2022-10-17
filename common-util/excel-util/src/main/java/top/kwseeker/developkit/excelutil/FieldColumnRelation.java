package top.kwseeker.developkit.excelutil;

import java.lang.reflect.Field;

/**
 * 实体类 和 Excel列 的关联
 */
public class FieldColumnRelation {

    private Field field;
    private int columnIndex;
    private String columnTitle;

    public FieldColumnRelation(Field field, int columnIndex, String columnTitle) {
        this.field = field;
        this.columnIndex = columnIndex;
        this.columnTitle = columnTitle;
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
}
