package top.kwseeker.developkit.commonutil.excel.write;

import top.kwseeker.developkit.commonutil.excel.write.api.ExcelBuilder;
import top.kwseeker.developkit.commonutil.excel.write.meta.WriteSheet;
import top.kwseeker.developkit.commonutil.excel.write.meta.WriteTable;
import top.kwseeker.developkit.commonutil.excel.write.meta.WriteWorkbook;
import top.kwseeker.developkit.commonutil.excel.write.support.ExcelBuilderImpl;

import java.util.List;

/**
 * 写操作真正的实现类，调用POI接口实现Excel写
 *  这里
 * ExcelWriterBuilder将
 */
public class ExcelWriter {

    private ExcelBuilder excelBuilder;

    public ExcelWriter(WriteWorkbook writeWorkbook) {
        this.excelBuilder = new ExcelBuilderImpl();
    }

    /* ===================== 流程执行部分 ======================*/

    public ExcelWriter write(List data, WriteSheet writeSheet) {
        return write(data, writeSheet, null);
    }

    public ExcelWriter write(List data, WriteSheet writeSheet, WriteTable writeTable) {
        excelBuilder.addContent(data, writeSheet, writeTable);
        return this;
    }

    public void finish() {
        if (excelBuilder != null) {
            excelBuilder.finish(false);
        }
    }
}
