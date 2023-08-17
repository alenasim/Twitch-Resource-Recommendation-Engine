package com.laioffer.twitch.db;

import com.laioffer.twitch.db.entity.FavoriteRecordEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface FavoriteRecordRepository extends ListCrudRepository<FavoriteRecordEntity, Long> {  //input告诉连接到哪个table内容，告诉primary key是什么
    List<FavoriteRecordEntity> findAllByUserId(Long userId);    //当前user喜欢的所有的entity回复回来
    boolean existsByUserIdAndItemId(Long userId, Long itemId);  //某个用户有没有收藏过这个item
    @Query("SELECT item_id FROM favorite_records WHERE user_id = :userId")  //
    List<Long> findFavoriteItemIdsByUserId(Long userId);   //需要这个用户喜欢的所有的items。为什么需要@Query？因为我们select的不是*，是一个specific item_id所对应的,所以不能直接给你。
    // 这里的function name名字没有那么重要，因为@Query的关系。input的userId一定要跟@Query里面的写的要一致。

    // delete多个input settings的话，一定要用这个。
    @Modifying    //自己手动创建的那些是read only，哪些是写的操作。假如手动创建的API里需要改写的话，一定要加@Modifying
    @Query("DELETE FROM favorite_records WHERE user_id = :userId AND item_id = :itemId")
    void delete(Long userId, Long itemId);

    /* 三种不同的input command有什么区别？
    * DELETE FROM favorite_records WHERE user_id = :userId AND item_id = :itemId --》 某个用户决定不再收藏这个item，要删掉
    * DELETE FROM favorite_records WHERE item_id = :itemId  --》 以前所有user收藏过的所有item都没了。
    * DELETE FROM favorite_records WHERE user_id = :userId --》 某个user的所有的item都被删掉。
    * */
}
