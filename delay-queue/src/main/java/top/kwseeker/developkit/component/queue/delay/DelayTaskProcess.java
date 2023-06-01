package top.kwseeker.developkit.component.queue.delay;

import lombok.Data;

import java.util.function.Function;
import java.util.function.Supplier;

@Data
public abstract class DelayTaskProcess implements Supplier<Void>, Function<Throwable, Void> {

    protected DelayTask<Object> delayTask;

    /**
     * 定义任务处理器逻辑
     */
    public abstract void handle();

    /**
     * 定义任务处理器逻辑
     */
    public void exceptionHandle() {
    }

    @Override
    public Void get() {
        this.handle();
        return null;
    }

    @Override
    public Void apply(Throwable throwable) {
        this.exceptionHandle();
        return null;
    }
}