package top.kwseeker.developkit.reactive.callback.service;

import org.springframework.stereotype.Service;
import top.kwseeker.developkit.reactive.callback.Callback;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class UserCallbackService {

    /**
     * 比如查看用户最喜欢的书
     */
    public void getFavorites(Long userId, Callback<List<String>> callback) {
        //模拟查询
        try {
            List<String> bookNames = getFavorites(userId);
            callback.onSuccess(bookNames);
        } catch (Throwable e) {
            callback.onError(e);
        }
    }

    private List<String> getFavorites(Long userId) {
        Random random = new Random();
        if (random.nextInt(5) == 4) {
            throw new RuntimeException("Exception Occur!");
        }
        return Arrays.asList("资本论", "资治通鉴", "鲁迅文集");
    }
}
