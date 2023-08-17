package com.laioffer.twitch.favorite;

import com.laioffer.twitch.db.entity.UserEntity;
import com.laioffer.twitch.model.FavoriteRequestBody;
import com.laioffer.twitch.model.TypeGroupedItemList;
import com.laioffer.twitch.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/favorite")   // 这是 optional，也可以把相对路径写在@GetMapping or @PostMapping里，但是因为路径一样，我们可以一起写在这里。// @RequestMapping vs. @GetMapping => function body里specify要get还是post；RequestMapping里只specify相对路径
public class FavoriteController {
    private final FavoriteService favoriteService;
    private final UserService userService;
    // Hard-coded user for temporary use, will be replaced when writing authentication code in the future
//    private final UserEntity userEntity = new UserEntity(1L, "user0", "Foo", "Bar", "password");
    public FavoriteController(FavoriteService favoriteService, UserService userService) {
        this.favoriteService = favoriteService;
        this.userService = userService;
    }

    @GetMapping       //读操作
    public TypeGroupedItemList getFavoriteItems(@AuthenticationPrincipal User user) {
        UserEntity userEntity = userService.findByUsername(user.getUsername());
        return favoriteService.getGroupedFavoriteItems(userEntity);
    }

    @PostMapping      //写操作
    // 这里的exception 可以在function上直接throw DuplicateFavoriteException,把exception给call它的function回过去；
    // 但是这不是很好的architecture practice； 最好跟自己有关的自己处理。跟自己无关的不要在这里有exception。
    // 在这里，我们用try catch自己控制在里面。
    public void setFavoriteItem(@AuthenticationPrincipal User user, @RequestBody FavoriteRequestBody body) throws DuplicateFavoriteException {

        //@RequestBody把json里面的output的东西拿出来再处理？？
        // 为什么这里用try catch，在catch里再throw exception？ 因为要把逻辑分开。要把error写在controller里。即使在其他地方用dependency injection用上再call，我们也会告诉前端，这个error是关于controller的不是跟当前call它的function有关。
        // 怎么理解single responsibility？ 怎么理解decoupling？ This is a good example for designing and architecture
        UserEntity userEntity = userService.findByUsername(user.getUsername());
        try {
            favoriteService.setFavoriteItem(userEntity, body.favorite());
        } catch (DuplicateFavoriteException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate entry for favorite record", e);
        }
    }
    @DeleteMapping
    public void unsetFavoriteItem(@AuthenticationPrincipal User user, @RequestBody FavoriteRequestBody body) {
        UserEntity userEntity = userService.findByUsername(user.getUsername());
        favoriteService.unsetFavoriteItem(userEntity, body.favorite().twitchId());
    }
}
