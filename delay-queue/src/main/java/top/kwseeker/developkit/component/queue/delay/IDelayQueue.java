package top.kwseeker.developkit.component.queue.delay;

public interface IDelayQueue {

    /**
     * 添加任务到延迟队列
     * @param task  待添加的任务
     */
    void push(DelayTask<Object> task);

    /**
     * 检查Ready的任务，并将其从 Bucket TaskPool 中删除，加入到 ReadyQueue
     */
    void checkReady(DelayTask.Type type);

    /**
     * 非阻塞地从延迟队列就绪队列中获取一个任务，并将其从bucket中删除
     * @return null 没有的话
     */
    DelayTask<Object> pollReady(DelayTask.Type type);

    void remove(DelayTask<Object> task);
}
