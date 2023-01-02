package top.kwseeker.developkit.reactive.callback.hell;

import org.springframework.stereotype.Service;
import top.kwseeker.developkit.reactive.callback.Callback;
import top.kwseeker.developkit.reactive.callback.service.FavoriteCallbackService;
import top.kwseeker.developkit.reactive.callback.service.SuggestionCallbackService;
import top.kwseeker.developkit.reactive.callback.service.UserCallbackService;
import top.kwseeker.developkit.reactive.model.Book;
import top.kwseeker.developkit.reactive.util.UiUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class HellService {

    @Resource
    private UserCallbackService userCallbackService;
    @Resource
    private SuggestionCallbackService suggestionCallbackService;
    @Resource
    private FavoriteCallbackService favoriteCallbackService;

    /**
     * 展示用户最喜欢的书籍详情
     */
    public void showFavorites(Long userId) {
        userCallbackService.getFavorites(userId, new Callback<List<String>>() {
            @Override
            public void onSuccess(List<String> list) {
                if (list.isEmpty()) {
                    suggestionCallbackService.getSuggestions(new Callback<List<Book>>() {
                        public void onSuccess(List<Book> list) {
                            UiUtils.submitOnUiThread(() -> {
                                list.stream()
                                        .limit(5)
                                        .forEach(UiUtils::show);
                            });
                        }

                        public void onError(Throwable error) {
                            UiUtils.errorPopup(error);
                        }
                    });
                } else {
                    list.stream()
                            .limit(5)
                            .forEach(favId -> favoriteCallbackService.getDetails(favId,
                                    new Callback<Book>() {
                                        public void onSuccess(Book details) {
                                            UiUtils.submitOnUiThread(() -> UiUtils.show(details));
                                        }

                                        public void onError(Throwable error) {
                                            UiUtils.errorPopup(error);
                                        }
                                    }
                            ));
                }
            }

            @Override
            public void onError(Throwable error) {
                UiUtils.errorPopup(error);
            }
        });
    }
}
