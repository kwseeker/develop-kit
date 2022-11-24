package top.kwseeker.developkit.excelutil;

import cn.hutool.poi.excel.WorkbookUtil;
import org.apache.poi.ss.usermodel.*;
import top.kwseeker.developkit.excelutil.validate.ValidateType;
import top.kwseeker.developkit.excelutil.validate.Validator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Excel文件转对象集合的转换器
 */
public class Excel2CollectionConverter {

    public <T> List<T> convert(File file, Class<T> targetClass) {
        try (InputStream fis = new FileInputStream(file)) {
            List<T> data = new ArrayList<T>();

            // Excel文件对象
            Workbook workbook = WorkbookUtil.createBook(fis, true);
            Annotation sheetAnnotation = targetClass.getAnnotation(Tab.class);

            return data;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("Excel文件解析失败", e.getCause());
        }
    }

    public <T> List<T> convert(InputStream inputStream, Class<T> targetClass) {
        List<T> data = new ArrayList<T>();

        // Excel文件对象
        Workbook workbook = WorkbookUtil.createBook(inputStream, true);
        Tab tabAnnotation = targetClass.getAnnotation(Tab.class);
        Sheet sheet;
        String sheetName = tabAnnotation.name();
        if (!"".equals(sheetName)) {
            sheet = workbook.getSheet(sheetName);
        } else {
            sheet = workbook.getSheetAt(tabAnnotation.sheetIndex());
        }

        // 列索引 -> 字段
        int titleRowIndex = tabAnnotation.titleRow();
        ExcelEntityRelation excelEntityRelation = parseExcelEntityRelation(targetClass, sheet, titleRowIndex);

        int dataBeginRow = tabAnnotation.dataBeginRow();
        int lastRowNum = sheet.getLastRowNum();
        System.out.println("lastRowNum: " + lastRowNum);
        try {
            for (int i = dataBeginRow; i <= lastRowNum; i++) {
                Row row = sheet.getRow(i);
                Cell primaryCell = row.getCell(excelEntityRelation.getPrimaryColumnIndex());
                if (primaryCell == null || primaryCell.getCellType() == CellType.BLANK) {
                    continue;
                }

                T instance = targetClass.newInstance();
                for (FieldColumnRelation relation : excelEntityRelation.getRelations()) {
                    int cellIndex = relation.getColumnIndex();
                    Field field = relation.getField();
                    List<Validator> validators = relation.getValidators();
                    Cell cell = row.getCell(cellIndex);

                    fieldSetCellValue(instance, field, cell, validators);
                }

                data.add(instance);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return data;
    }

    private ExcelEntityRelation parseExcelEntityRelation(Class<?> targetClass, Sheet sheet, int titleRowIndex) {
        List<FieldColumnRelation> relations = new ArrayList<>();

        // 获取标题行
        Row titleRow = sheet.getRow(titleRowIndex);
        Map<String, Integer> title2CellIndexMap = new HashMap<>();
        for (int i = 0; i < titleRow.getLastCellNum(); i++) {
            Cell titleCell = titleRow.getCell(i);
            String title = titleCell.getStringCellValue();
            title2CellIndexMap.put(title, i);
        }

        Integer primaryColumnIndex = null;
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            Column columnAnnotation = field.getAnnotation(Column.class);
            if (columnAnnotation == null) {
                continue;
            }

            String title = columnAnnotation.title();
            Integer cellIndex = title2CellIndexMap.get(title);
            if (columnAnnotation.isPrimary()) {
                primaryColumnIndex = cellIndex;
            }
            List<Validator> validators = new ArrayList<>();
            if (columnAnnotation.validateTypes().length > 0) {
                for (ValidateType validateType : columnAnnotation.validateTypes()) {
                    validators.add(validateType.getValidator());
                }
            }
            FieldColumnRelation relation = new FieldColumnRelation(field, cellIndex, title, validators);
            relations.add(relation);
        }

        return new ExcelEntityRelation(relations, primaryColumnIndex);
    }

    private <T> void fieldSetCellValue(T instance, Field field, Cell cell, List<Validator> validators) {
        try {
            Class<?> fieldType = field.getType();
            field.setAccessible(true);

            if (fieldType == String.class) {
                checkAndSetField(instance, field, cell.getStringCellValue(), validators);
            } else if (fieldType == Boolean.class) {
                checkAndSetField(instance, field, cell.getBooleanCellValue(), validators);
            } else if (fieldType == Date.class) {
                checkAndSetField(instance, field, cell.getDateCellValue(), validators);
            } else if (Number.class.isAssignableFrom(fieldType)) {
                if (fieldType == Short.class) {
                    checkAndSetField(instance, field, (short) cell.getNumericCellValue(), validators);
                } else if (fieldType == Integer.class) {
                    checkAndSetField(instance, field, (int) cell.getNumericCellValue(), validators);
                } else if (fieldType == Long.class) {
                    checkAndSetField(instance, field, (long) cell.getNumericCellValue(), validators);
                } else if (fieldType == Float.class) {
                    checkAndSetField(instance, field, (float) cell.getNumericCellValue(), validators);
                } else if (fieldType == Double.class) {
                    checkAndSetField(instance, field, cell.getNumericCellValue(), validators);
                } else {
                    throw new BusinessException("Unsupported field type: " + fieldType);
                }
            } else {
                throw new BusinessException("Unsupported field type: " + fieldType);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new BusinessException("field set cell value failed");
        }
    }

    private <T> void checkAndSetField(T instance, Field field, Object cellValue, List<Validator> validators) throws IllegalAccessException {
        for (Validator validator : validators) {
            Class<?> fieldType = field.getType();
            if (!validator.matchType(fieldType) || !validator.valid(cellValue)) {
                throw new BusinessException("cell value validate failed");
            }
        }
        field.set(instance, cellValue);
    }
}
