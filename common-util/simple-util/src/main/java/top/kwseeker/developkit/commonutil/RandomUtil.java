package top.kwseeker.developkit.commonutil;

/**
 * 通过算法模拟出来的随机数都是伪随机数。伪随机数序列仍然会存在某些规律。
 * Java中几种常见的随机数生成器对比
 *  Random
 *      如果知道种子的值，随机数序列生成是可以预测的。
 *      两个Random实例如果种子相同生成的随机数序列也是相同的。
 *      next()方法每次调用都会根据原来的seed生成一个新的seed,设置新的seed的时候，使用CAS保证线程安全性。
 *      场景：安全性要求不高，一般都是使用公用的静态的Random实例生成随机数（多实例有可能生成一样的随机数序列）。
 *  ThreadLocalRandom
 *      使用内部生成的种子初始化，只与当前线程关联，因为没有CAS锁性能比较高。
 *      场景：并发多任务场景。
 *  SecurityRandom
 *      提供加密的强随机数生成器 (RNG)，要求种子必须是不可预知的，产生非确定性输出。
 *  Math.random()
 *      内部通过Random实现，使用了一个和当前系统时间相关的数字作为种子。
 *      只能生成0-1之间的数
 */
public class RandomUtil {

    //public static int getRandomInt() {
    //
    //}
    //
    //public static long getRandomLong() {
    //
    //}
    //
    //public static float getRandomFloat() {
    //
    //}
    //
    //public static double getRandomDouble() {
    //
    //}
    //
    //public static boolean getRandomBoolean() {
    //
    //}

}
