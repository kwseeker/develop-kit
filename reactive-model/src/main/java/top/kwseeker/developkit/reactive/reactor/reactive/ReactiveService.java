package top.kwseeker.developkit.reactive.reactor.reactive;

import org.springframework.stereotype.Service;
import top.kwseeker.developkit.reactive.reactor.service.FavoriteReactiveService;
import top.kwseeker.developkit.reactive.reactor.service.SuggestionReactiveService;
import top.kwseeker.developkit.reactive.reactor.service.UserReactiveService;
import top.kwseeker.developkit.reactive.util.UiUtils;

import javax.annotation.Resource;

@Service
public class ReactiveService {

    @Resource
    private UserReactiveService userReactiveService;
    @Resource
    private SuggestionReactiveService suggestionReactiveService;
    @Resource
    private FavoriteReactiveService favoriteReactiveService;

    public void showFavorites(Long userId) {
        userReactiveService.getFavorites(userId)
                //.flatMap(favId -> favoriteService.getDetails(favId))
                .flatMap(favoriteReactiveService::getDetails)
                .switchIfEmpty(suggestionReactiveService.getSuggestions())
                .take(5)
                .publishOn(UiUtils.uiThreadScheduler())
                .subscribe(UiUtils::show, UiUtils::errorPopup, UiUtils::completePopup);
    }
}
