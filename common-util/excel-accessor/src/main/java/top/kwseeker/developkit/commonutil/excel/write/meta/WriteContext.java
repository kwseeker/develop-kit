package top.kwseeker.developkit.commonutil.excel.write.meta;

import top.kwseeker.developkit.commonutil.excel.write.enums.WriteTypeEnum;

public interface WriteContext {

    void currentSheet(WriteSheet writeSheet, WriteTypeEnum writeType);

    void currentTable(WriteTable writeTable);

    void finish(boolean onException);
}
