package com.laioffer.twitch.item;

import com.laioffer.twitch.external.TwitchService;
import com.laioffer.twitch.external.model.Clip;
import com.laioffer.twitch.external.model.Stream;
import com.laioffer.twitch.external.model.Video;
import com.laioffer.twitch.model.TypeGroupedItemList;
import org.checkerframework.checker.units.qual.C;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

// ？？？把跟gameId相关的所有的信息(clip/video/stream)放在typegroupitemlist里面。前端call search的时候，把跟game相关的东西传出来
@Service
public class ItemService {

    private static final int SEARCH_RESULT_SIZE = 20;

    private final TwitchService twitchService;

    public ItemService(TwitchService twitchService) {
        this.twitchService = twitchService;
    }
    @Cacheable("items")
    public TypeGroupedItemList getItems(String gameId) {
        List<Video> videos = twitchService.getVideos(gameId, SEARCH_RESULT_SIZE);
        List<Clip> clips = twitchService.getClips(gameId, SEARCH_RESULT_SIZE);
        List<Stream> streams = twitchService.getStreams(List.of(gameId), SEARCH_RESULT_SIZE);
        return new TypeGroupedItemList(gameId, streams, videos, clips);
    }

}