package com.laioffer.twitch.favorite;

import com.laioffer.twitch.db.FavoriteRecordRepository;
import com.laioffer.twitch.db.ItemRepository;
import com.laioffer.twitch.db.entity.FavoriteRecordEntity;
import com.laioffer.twitch.db.entity.ItemEntity;
import com.laioffer.twitch.db.entity.UserEntity;
import com.laioffer.twitch.model.TypeGroupedItemList;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

// FavoriteService被FavoriteController调用。FavoriteService用来set/unset/get favorite items
@Service
public class FavoriteService {        //这service需要什么dependency？ == constructor里面需要什么外部信息？ItemRepository，FavoriteRecordRepository
    private final ItemRepository itemRepository;
    private final FavoriteRecordRepository favoriteRecordRepository;
    public FavoriteService(ItemRepository itemRepository,
                           FavoriteRecordRepository favoriteRecordRepository) {
        this.itemRepository = itemRepository;
        this.favoriteRecordRepository = favoriteRecordRepository;
    }
    @CacheEvict(cacheNames = "recommend_items", key = "#root.args[0]")
    @Transactional      //这annotation意思：有多个操作时，假如所有操作成功，transaction就go through；假如其中一个function，没成功，就roll back to initial state
    // 也可以理解成，先写在cache里，complete之后再转动database里 =》实际情况更复杂很多
    public void setFavoriteItem(UserEntity user, ItemEntity item) throws DuplicateFavoriteException {
        ItemEntity persistedItem = itemRepository.findByTwitchId(item.twitchId());  //用ItemReposity去fetch一次ItemEntity的twitchId
        // 目前我不知道这个item有没有在database里存过。是null的话，存下，再赋值给persistedItem，确保他不是null
        if (persistedItem == null) {
            persistedItem = itemRepository.save(item);
        }
        // 如果这个item之前已经favorite过就没意义再favorite，你要让前端知道，让前端去处理。
        if (favoriteRecordRepository.existsByUserIdAndItemId(user.id(), persistedItem.id())) {
            throw new DuplicateFavoriteException();
        }
        // Most common case -  把所有情况都满足的话，就存下来。
        FavoriteRecordEntity favoriteRecord = new FavoriteRecordEntity(null, user.id(), persistedItem.id(), Instant.now());
        favoriteRecordRepository.save(favoriteRecord);
    }
    @CacheEvict(cacheNames = "recommend_items", key = "#root.args[0]")
    public void unsetFavoriteItem(UserEntity user, String twitchId) {
        ItemEntity item = itemRepository.findByTwitchId(twitchId);
        // 如果不是null的话，只删除用户自己favorite过的record。千万别删item因为其他user可能也favotite了。
        if (item != null) {
            favoriteRecordRepository.delete(user.id(), item.id());
        }
        // 如果没有我要删的东西，可以直接什么都不错，跳过去。或者可以throw exception
    }

    public List<ItemEntity> getFavoriteItems(UserEntity user) {
        // 先找到用户favorite过的items，再去return - 所有的clip，video，stream都放在一个list里面
        List<Long> favoriteItemIds = favoriteRecordRepository.findFavoriteItemIdsByUserId(user.id());
        return itemRepository.findAllById(favoriteItemIds);
    }

    public TypeGroupedItemList getGroupedFavoriteItems(UserEntity user) {
        // Unlike前面的API，把所有entity group在一个list，这个API把他们分成三个types - stream，clip，video分类之后给你发过来
        List<ItemEntity> items = getFavoriteItems(user);
        return new TypeGroupedItemList(items);
    }
}