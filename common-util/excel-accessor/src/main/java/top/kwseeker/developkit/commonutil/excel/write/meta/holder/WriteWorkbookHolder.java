package top.kwseeker.developkit.commonutil.excel.write.meta.holder;

import org.apache.poi.ss.usermodel.Workbook;
import top.kwseeker.developkit.commonutil.excel.write.meta.GlobalConfiguration;
import top.kwseeker.developkit.commonutil.excel.write.meta.WriteWorkbook;

import java.io.*;

public class WriteWorkbookHolder extends AbstractWriteHolder {

    //POI工作表（对应一个Excel文件）
    private Workbook workbook;

    private Workbook cachedWorkbook;

    //Excel目标文件，头部定义
    private WriteWorkbook writeWorkbook;

    private GlobalConfiguration globalConfiguration;


    private File file;

    private OutputStream outputStream;

    private InputStream templateInputStream;

    private File templateFile;

    private InputStream tempTemplateInputStream;

    //private ExcelTypeEnum excelType;

    private Boolean inMemory;

    private Boolean writeExcelOnException;

    public WriteWorkbookHolder(WriteWorkbook writeWorkbook) {
        super(writeWorkbook, null, writeWorkbook.getConvertAllFiled());
        this.writeWorkbook = writeWorkbook;
        this.file = writeWorkbook.getFile();
        if (file != null) {
            try {
                this.outputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                throw new ExcelGenerateException("Can not found file.", e);
            }
        } else {
            this.outputStream = writeWorkbook.getOutputStream();
        }
        if (writeWorkbook.getAutoCloseStream() == null) {
            this.autoCloseStream = Boolean.TRUE;
        } else {
            this.autoCloseStream = writeWorkbook.getAutoCloseStream();
        }
        try {
            copyTemplate();
        } catch (IOException e) {
            throw new ExcelGenerateException("Copy template failure.", e);
        }
        if (writeWorkbook.getExcelType() == null) {
            boolean isXls = (file != null && file.getName().endsWith(ExcelTypeEnum.XLS.getValue()))
                    || (writeWorkbook.getTemplateFile() != null
                    && writeWorkbook.getTemplateFile().getName().endsWith(ExcelTypeEnum.XLS.getValue()));
            if (isXls) {
                this.excelType = ExcelTypeEnum.XLS;
            } else {
                this.excelType = ExcelTypeEnum.XLSX;
            }
        } else {
            this.excelType = writeWorkbook.getExcelType();
        }
        if (writeWorkbook.getMandatoryUseInputStream() == null) {
            this.mandatoryUseInputStream = Boolean.FALSE;
        } else {
            this.mandatoryUseInputStream = writeWorkbook.getMandatoryUseInputStream();
        }
        this.hasBeenInitializedSheetIndexMap = new HashMap<Integer, WriteSheetHolder>();
        this.hasBeenInitializedSheetNameMap = new HashMap<String, WriteSheetHolder>();
        this.password = writeWorkbook.getPassword();
        if (writeWorkbook.getInMemory() == null) {
            this.inMemory = Boolean.FALSE;
        } else {
            this.inMemory = writeWorkbook.getInMemory();
        }
        if (writeWorkbook.getWriteExcelOnException() == null) {
            this.writeExcelOnException = Boolean.FALSE;
        } else {
            this.writeExcelOnException = writeWorkbook.getWriteExcelOnException();
        }
    }

    private void copyTemplate() throws IOException {
        if (writeWorkbook.getTemplateFile() == null && writeWorkbook.getTemplateInputStream() == null) {
            return;
        }
        byte[] templateFileByte = null;
        if (writeWorkbook.getTemplateFile() != null) {
            templateFileByte = FileUtils.readFileToByteArray(writeWorkbook.getTemplateFile());
        } else if (writeWorkbook.getTemplateInputStream() != null) {
            try {
                templateFileByte = IoUtils.toByteArray(writeWorkbook.getTemplateInputStream());
            } finally {
                if (autoCloseStream) {
                    writeWorkbook.getTemplateInputStream().close();
                }
            }
        }
        this.tempTemplateInputStream = new ByteArrayInputStream(templateFileByte);
    }

}
