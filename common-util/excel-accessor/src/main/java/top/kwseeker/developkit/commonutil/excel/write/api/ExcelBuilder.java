package top.kwseeker.developkit.commonutil.excel.write.api;

import top.kwseeker.developkit.commonutil.excel.write.meta.WriteSheet;
import top.kwseeker.developkit.commonutil.excel.write.meta.WriteTable;

import java.util.List;

public interface ExcelBuilder {

    void addContent(List data, WriteSheet writeSheet, WriteTable writeTable);

    /**
     * 关闭IO
     * @param onException if exception occur
     */
    void finish(boolean onException);
}
