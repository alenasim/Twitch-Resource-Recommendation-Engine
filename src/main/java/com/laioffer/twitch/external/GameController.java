package com.laioffer.twitch.external;

import com.laioffer.twitch.external.model.Game;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// GameController是干嘛的？ 给他一个game name，把twitch里面跟game有关的都fetch出来
@RestController                 //dependency injection的annotation其中之一。下面的@GetMapping都跟这个连接在一起。换了或者删了就没办法用下面的@
public class GameController {        //在dependency inject里面new之后
    private final TwitchService twitchService;   // final member field, must be initialized using a constructor below.

    public GameController(TwitchService twitchService) {
        this.twitchService = twitchService;
    }
    @GetMapping("/game")
    public List<Game> getGames(@RequestParam(value = "game_name", required = false) String gameName) {
        if (gameName == null) {
            return twitchService.getTopGames();
        } else {
            return twitchService.getGames(gameName);
        }
    }
}
