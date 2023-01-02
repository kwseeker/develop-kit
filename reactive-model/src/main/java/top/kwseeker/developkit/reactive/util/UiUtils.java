package top.kwseeker.developkit.reactive.util;

import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import top.kwseeker.developkit.reactive.model.Book;

import java.util.List;

public class UiUtils {
    public static void submitOnUiThread(Runnable runnable) {
        runnable.run();
    }

    public static void show(Book book) {
        System.out.println("\t" + book.toString());
    }

    public static void show(List<Book> books) {
        System.out.println("show: ");
        for (Book book : books) {
            show(book);
        }
    }

    public static void errorPopup(Throwable error) {
        System.out.println("error: " + error.toString());
    }

    public static void completePopup() {
        System.out.println("completed!");
    }

    public static Scheduler uiThreadScheduler() {
        return Schedulers.immediate();
    }
}
