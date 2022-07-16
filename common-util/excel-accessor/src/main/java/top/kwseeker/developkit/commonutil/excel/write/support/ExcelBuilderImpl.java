package top.kwseeker.developkit.commonutil.excel.write.support;

import top.kwseeker.developkit.commonutil.excel.exception.ExcelGenerateException;
import top.kwseeker.developkit.commonutil.excel.executor.ExcelWriteAddExecutor;
import top.kwseeker.developkit.commonutil.excel.write.api.ExcelBuilder;
import top.kwseeker.developkit.commonutil.excel.write.enums.WriteTypeEnum;
import top.kwseeker.developkit.commonutil.excel.write.meta.*;

import java.util.List;

public class ExcelBuilderImpl implements ExcelBuilder {

    private WriteContext context;
    private ExcelWriteAddExecutor excelWriteAddExecutor;

    public ExcelBuilderImpl(WriteWorkbook writeWorkbook) {
        try {
            context = new WriteContextImpl(writeWorkbook);
        } catch (RuntimeException e) {
            finishOnException();
            throw e;
        } catch (Throwable e) {
            finishOnException();
            throw new ExcelGenerateException(e);
        }
    }

    @Override
    public void addContent(List data, WriteSheet writeSheet, WriteTable writeTable) {
        try {
            context.currentSheet(writeSheet, WriteTypeEnum.ADD);
            context.currentTable(writeTable);
            if (excelWriteAddExecutor == null) {
                excelWriteAddExecutor = new ExcelWriteAddExecutor(context);
            }
            excelWriteAddExecutor.add(data);
        } catch (RuntimeException e) {
            finishOnException();
            throw e;
        } catch (Throwable e) {
            finishOnException();
            throw new ExcelGenerateException(e);
        }
    }

    private void finishOnException() {
        finish(true);
    }

    @Override
    public void finish(boolean onException) {
        if (context != null) {
            context.finish(onException);
        }
    }
}
