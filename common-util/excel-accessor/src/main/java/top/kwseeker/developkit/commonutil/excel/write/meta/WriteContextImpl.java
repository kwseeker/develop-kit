package top.kwseeker.developkit.commonutil.excel.write.meta;

import top.kwseeker.developkit.commonutil.excel.exception.ExcelGenerateException;
import top.kwseeker.developkit.commonutil.excel.write.enums.WriteTypeEnum;
import top.kwseeker.developkit.commonutil.excel.write.meta.holder.WriteHolder;
import top.kwseeker.developkit.commonutil.excel.write.meta.holder.WriteWorkbookHolder;

import java.util.List;
import java.util.Map;

public class WriteContextImpl implements WriteContext {

    private WriteWorkbookHolder writeWorkbookHolder;

    private WriteHolder currentWriteHolder;

    public WriteContextImpl(WriteWorkbook writeWorkbook) {
        if (writeWorkbook == null) {
            throw new IllegalArgumentException("Workbook argument cannot be null");
        }
        initCurrentWorkbookHolder(writeWorkbook);
        WriteHandlerUtils.beforeWorkbookCreate(this);
        try {
            WorkBookUtil.createWorkBook(writeWorkbookHolder);
        } catch (Exception e) {
            throw new ExcelGenerateException("Create workbook failure", e);
        }
        WriteHandlerUtils.afterWorkbookCreate(this);
    }

    private void initCurrentWorkbookHolder(WriteWorkbook writeWorkbook) {
        writeWorkbookHolder = new WriteWorkbookHolder(writeWorkbook);
        currentWriteHolder = writeWorkbookHolder;
    }

    public static void beforeWorkbookCreate(WriteContext writeContext, boolean runOwn) {

        List<WriteHandler> handlerList = getHandlerList(writeContext, WorkbookWriteHandler.class, runOwn);
        if (handlerList == null || handlerList.isEmpty()) {
            return;
        }
        for (WriteHandler writeHandler : handlerList) {
            if (writeHandler instanceof WorkbookWriteHandler) {
                ((WorkbookWriteHandler) writeHandler).beforeWorkbookCreate();
            }
        }
    }

    private static List<WriteHandler> getHandlerList(WriteContext writeContext,
                                                     Class<? extends WriteHandler> clazz,
                                                     boolean runOwn) {
        Map<Class<? extends WriteHandler>, List<WriteHandler>> writeHandlerMap;
        if (runOwn) {
            writeHandlerMap = writeContext.currentWriteHolder().ownWriteHandlerMap();
        } else {
            writeHandlerMap = writeContext.currentWriteHolder().writeHandlerMap();
        }
        return writeHandlerMap.get(clazz);
    }

    @Override
    public void currentSheet(WriteSheet writeSheet, WriteTypeEnum writeType) {
        if (writeSheet == null) {
            throw new IllegalArgumentException("Sheet argument cannot be null");
        }
        if (selectSheetFromCache(writeSheet)) {
            return;
        }

        initCurrentSheetHolder(writeSheet);

        // Workbook handler need to supplementary execution
        WriteHandlerUtils.beforeWorkbookCreate(this, true);
        WriteHandlerUtils.afterWorkbookCreate(this, true);

        // Initialization current sheet
        initSheet(writeType);
    }

    @Override
    public void currentTable(WriteTable writeTable) {
        if (writeTable == null) {
            return;
        }
        if (writeTable.getTableNo() == null || writeTable.getTableNo() <= 0) {
            writeTable.setTableNo(0);
        }
        if (writeSheetHolder.getHasBeenInitializedTable().containsKey(writeTable.getTableNo())) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Table:{} is already existed", writeTable.getTableNo());
            }
            writeTableHolder = writeSheetHolder.getHasBeenInitializedTable().get(writeTable.getTableNo());
            writeTableHolder.setNewInitialization(Boolean.FALSE);
            currentWriteHolder = writeTableHolder;
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("CurrentConfiguration is writeTableHolder");
            }
            return;
        }

        initCurrentTableHolder(writeTable);

        // Workbook and sheet handler need to supplementary execution
        WriteHandlerUtils.beforeWorkbookCreate(this, true);
        WriteHandlerUtils.afterWorkbookCreate(this, true);
        WriteHandlerUtils.beforeSheetCreate(this, true);
        WriteHandlerUtils.afterSheetCreate(this, true);

        initHead(writeTableHolder.excelWriteHeadProperty());
    }

    @Override
    public void finish(boolean onException) {
        if (finished) {
            return;
        }
        finished = true;
        WriteHandlerUtils.afterWorkbookDispose(this);
        if (writeWorkbookHolder == null) {
            return;
        }
        Throwable throwable = null;
        boolean isOutputStreamEncrypt = false;
        // Determine if you need to write excel
        boolean writeExcel = !onException;
        if (writeWorkbookHolder.getWriteExcelOnException()) {
            writeExcel = Boolean.TRUE;
        }
        // No data is written if an exception is thrown
        if (writeExcel) {
            try {
                isOutputStreamEncrypt = doOutputStreamEncrypt07();
            } catch (Throwable t) {
                throwable = t;
            }
        }
        if (!isOutputStreamEncrypt) {
            try {
                if (writeExcel) {
                    writeWorkbookHolder.getWorkbook().write(writeWorkbookHolder.getOutputStream());
                }
                writeWorkbookHolder.getWorkbook().close();
            } catch (Throwable t) {
                throwable = t;
            }
        }
        try {
            Workbook workbook = writeWorkbookHolder.getWorkbook();
            if (workbook instanceof SXSSFWorkbook) {
                ((SXSSFWorkbook) workbook).dispose();
            }
        } catch (Throwable t) {
            throwable = t;
        }
        try {
            if (writeWorkbookHolder.getAutoCloseStream() && writeWorkbookHolder.getOutputStream() != null) {
                writeWorkbookHolder.getOutputStream().close();
            }
        } catch (Throwable t) {
            throwable = t;
        }
        if (writeExcel && !isOutputStreamEncrypt) {
            try {
                doFileEncrypt07();
            } catch (Throwable t) {
                throwable = t;
            }
        }
        try {
            if (writeWorkbookHolder.getTempTemplateInputStream() != null) {
                writeWorkbookHolder.getTempTemplateInputStream().close();
            }
        } catch (Throwable t) {
            throwable = t;
        }
        clearEncrypt03();
        removeThreadLocalCache();
        if (throwable != null) {
            throw new ExcelGenerateException("Can not close IO.", throwable);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Finished write.");
        }
    }

}
