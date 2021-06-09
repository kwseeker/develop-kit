package top.kwseeker.developkit.commonutil.batch;

import java.util.List;

public interface Processor<T> {

    void process(List<T> data) throws Exception;
}
