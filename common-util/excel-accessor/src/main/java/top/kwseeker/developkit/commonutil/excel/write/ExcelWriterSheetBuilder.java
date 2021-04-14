package top.kwseeker.developkit.commonutil.excel.write;


import top.kwseeker.developkit.commonutil.excel.exception.ExcelGenerateException;

import java.util.List;

public class ExcelWriterSheetBuilder {

    //内部已经封装 file head 的属性和操作
    private ExcelWriter excelWriter;

    private WriteSheet writeSheet;

    public ExcelWriterSheetBuilder(ExcelWriter excelWriter) {
        this.excelWriter = excelWriter;
        this.writeSheet = new WriteSheet();
    }

    /* ===================== 流程准备部分 ======================*/

    public ExcelWriterSheetBuilder sheetNo(Integer sheetNo) {
        writeSheet.setSheetNo(sheetNo);
        return this;
    }

    public ExcelWriterSheetBuilder sheetName(String sheetName) {
        writeSheet.setSheetName(sheetName);
        return this;
    }

    /* ===================== 流程执行部分 ======================*/

    public void doWrite(List data) {
        if (excelWriter == null) {
            throw new ExcelGenerateException("Must use 'EasyExcelFactory.write().sheet()' to call this method");
        }
        excelWriter.write(data, build());
        excelWriter.finish();
    }

    public WriteSheet build() {
        return writeSheet;
    }
}
