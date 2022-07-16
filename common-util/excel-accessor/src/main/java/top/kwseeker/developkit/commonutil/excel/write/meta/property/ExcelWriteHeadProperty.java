package top.kwseeker.developkit.commonutil.excel.write.meta.property;


import java.lang.reflect.Field;
import java.util.Map;

public class ExcelWriteHeadProperty {

    //head行高
    private Short headRowHeight;
    //内容行高
    private Short contentRowHeight;

    private int firstRowIndex;
    private int lastRowIndex;
    private int firstColumnIndex;
    private int lastColumnIndex;

    private Class headClazz;
    //TODO?
    private HeadKindEnum headKind;
    private int headRowNumber;
    //Head行的属性(每个field的名字、展示名、索引、)
    private Map<Integer, Head> headMap;
    private Map<Integer, ExcelContentProperty> contentPropertyMap;
    private Map<String, ExcelContentProperty> fieldNameContentPropertyMap;
    private Map<String, Field> ignoreMap;

    enum HeadKindEnum {
        NONE,
        CLASS,
        STRING;
    }
}
