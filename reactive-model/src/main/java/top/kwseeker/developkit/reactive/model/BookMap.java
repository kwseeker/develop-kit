package top.kwseeker.developkit.reactive.model;

import java.util.HashMap;
import java.util.Map;

public class BookMap {

    public static final Map<String, Book> MAP;

    static {
        //"资本论", "资治通鉴", "鲁迅文集"
        MAP = new HashMap<>();
        MAP.put("资本论", new Book("资本论"));
        MAP.put("资治通鉴", new Book("资治通鉴"));
        MAP.put("鲁迅文集", new Book("鲁迅文集"));
        MAP.put("世界通史", new Book("世界通史"));
        MAP.put("时间简史", new Book("时间简史"));
        MAP.put("乌合之众", new Book("乌合之众"));
        MAP.put("怪诞行为学", new Book("怪诞行为学"));
        MAP.put("经济学原理", new Book("经济学原理"));
    }
}
