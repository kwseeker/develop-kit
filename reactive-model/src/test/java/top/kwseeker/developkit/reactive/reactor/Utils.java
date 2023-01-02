package top.kwseeker.developkit.reactive.reactor;

public class Utils {

    public static void println(Object msg) {
        System.out.printf("[Thread : %s] : %s\n", Thread.currentThread().getName(), msg.toString());
    }
}
