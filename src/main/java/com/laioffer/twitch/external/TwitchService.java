package com.laioffer.twitch.external;

import com.laioffer.twitch.external.model.Clip;
import com.laioffer.twitch.external.model.Game;
import com.laioffer.twitch.external.model.Stream;
import com.laioffer.twitch.external.model.Video;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//为什么需要Twitch Service？为什么不能在Twtich API client做完？ Twitch api logic 1-to-1 mapping。我们可以额外的logic在twitchservice里做，比如说cacheing
//The main purpose of this TwitchService class is to provide a higher-level abstraction for interacting with the Twitch API by utilizing the Feign client (TwitchApiClient).
// Additionally, caching has been added to improve performance by storing and reusing the results of certain API calls.
@Service          //dependency injection - must include this annotation so that we can call these services below. 如果向运行
public class TwitchService {

    private final TwitchApiClient twitchApiClient;

    public TwitchService(TwitchApiClient twitchApiClient) {
        this.twitchApiClient = twitchApiClient;
    } //他需要twitchApiClient，被inject进来，SpringBoot帮你自动create了object
    @Cacheable("top_games")   // cache里面存储的时候，用top_game名字来存
    public List<Game> getTopGames() {
        return twitchApiClient.getTopGames().data();
    }
    @Cacheable("games_by_name")
    public List<Game> getGames(String name) {
        return twitchApiClient.getGames(name).data();
    }

    public List<Stream> getStreams(List<String> gameIds, int first) {
        return twitchApiClient.getStreams(gameIds, first).data();
    }

    public List<Video> getVideos(String gameId, int first) {
        return twitchApiClient.getVideos(gameId, first).data();
    }

    public List<Clip> getClips(String gameId, int first) {
        return twitchApiClient.getClips(gameId, first).data();
    }

    public List<String> getTopGameIds() {
        List<String> topGameIds = new ArrayList<>();
        for (Game game : getTopGames()) {
            topGameIds.add(game.id());
        }
        return topGameIds;
    }
}
