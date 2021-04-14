package top.kwseeker.developkit.commonutil.excel.write;

import java.io.File;

/**
 * Excel写流程构造者
 */
public class ExcelWriterBuilder {

    //要写到工作簿的数据结构对象
    private WriteWorkbook writeWorkbook;

    public ExcelWriterBuilder() {
        this.writeWorkbook = new WriteWorkbook();
    }

    /* ===================== 流程准备部分 ======================*/
    //数据写到哪个文件
    public ExcelWriterBuilder file(String outputPathName) {
        writeWorkbook.setFile(new File(outputPathName));
        return this;
    }

    //Sheet头部数据格式是什么样的
    public ExcelWriterBuilder head(Class clazz) {
        writeWorkbook.setClazz(clazz);
        return this;
    }

    /* ===================== 装饰部分 ======================*/

    public ExcelWriterSheetBuilder sheet(String sheetName) {
        return sheet(null, sheetName);
    }
    //装饰了一层
    //在 file head 基础上继续装配其他操作，并返回一个新的builder
    public ExcelWriterSheetBuilder sheet(Integer sheetNo, String sheetName) {
        //TODO
        ExcelWriter excelWriter = build();
        ExcelWriterSheetBuilder excelWriterSheetBuilder = new ExcelWriterSheetBuilder(excelWriter);
        if (sheetNo != null) {
            excelWriterSheetBuilder.sheetNo(sheetNo);
        }
        if (sheetName != null) {
            excelWriterSheetBuilder.sheetName(sheetName);
        }
        return excelWriterSheetBuilder;
    }

    public ExcelWriter build() {
        return new ExcelWriter(writeWorkbook);
    }

    /* ===================== 流程执行部分 ======================*/


}
