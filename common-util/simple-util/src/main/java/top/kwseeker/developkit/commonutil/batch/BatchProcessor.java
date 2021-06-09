package top.kwseeker.developkit.commonutil.batch;

import java.util.ArrayList;
import java.util.List;

public class BatchProcessor<T> {

    private final List<T> dataList;
    private final int batchCount;
    private final Processor<T> processor;

    public BatchProcessor(int batchCount, Processor<T> processor) {
        this.dataList = new ArrayList<>(batchCount);
        this.batchCount = batchCount;
        this.processor = processor;
    }

    //添加待处理数据
    public void add(T data) {
        dataList.add(data);
        if (batchCount == dataList.size()) {
            try {
                processor.process(dataList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dataList.clear();
        }
    }

    public void doFinal() {
        if (dataList.size() != 0) {
            try {
                processor.process(dataList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dataList.clear();
        }
    }
}
