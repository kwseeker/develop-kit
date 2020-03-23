package top.kwseeker.developkit.calltrace.async;

import org.slf4j.MDC;

import java.util.Map;

/**
 * 父子Thread中传递MDC
 *
 * 在子线程被创建之前提取，在子线程被创建之后设置
 */
public class MdcThread extends Thread {

    private Map<String, String> traceMap;

    public MdcThread(Runnable runnable) {
        super(runnable);
        this.traceMap = MDC.getCopyOfContextMap();  //当执行到这里还在父线程中
    }

    //Thread类启动线程是native方法 start0()，从启动到调用run()执行任务前根本没有缝隙给我们插入MDC,所以只能在run及之后阶段动手脚了
    @Override
    public void run() {
        MDC.setContextMap(this.traceMap);
        super.run();
    }
}
