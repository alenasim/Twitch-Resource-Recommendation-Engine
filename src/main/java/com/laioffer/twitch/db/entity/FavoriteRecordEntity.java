package com.laioffer.twitch.db.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("favorite_records")
public record FavoriteRecordEntity(
        @Id Long id,
        Long userId,
        Long itemId,
        Instant createdAt    // 代表一个time stamp - java里面的时间怎么处理的？Instant代表timestamp，精准度高，不被不同的time zone影响
) {
}
