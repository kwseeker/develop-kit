package top.kwseeker.developkit.commonutil.excel.executor;

import top.kwseeker.developkit.commonutil.excel.write.meta.WriteContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ExcelWriteAddExecutor {

    public ExcelWriteAddExecutor(WriteContext writeContext) {
        super(writeContext);
    }

    public void add(List data) {
        if (CollectionUtils.isEmpty(data)) {
            data = new ArrayList();
        }
        WriteSheetHolder writeSheetHolder = writeContext.writeSheetHolder();
        int newRowIndex = writeSheetHolder.getNewRowIndexAndStartDoWrite();
        if (writeSheetHolder.isNew() && !writeSheetHolder.getExcelWriteHeadProperty().hasHead()) {
            newRowIndex += writeContext.currentWriteHolder().relativeHeadRowIndex();
        }
        // BeanMap is out of order,so use sortedAllFiledMap
        Map<Integer, Field> sortedAllFiledMap = new TreeMap<Integer, Field>();
        int relativeRowIndex = 0;
        for (Object oneRowData : data) {
            int n = relativeRowIndex + newRowIndex;
            addOneRowOfDataToExcel(oneRowData, n, relativeRowIndex, sortedAllFiledMap);
            relativeRowIndex++;
        }
    }
}
