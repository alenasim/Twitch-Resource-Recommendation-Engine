package com.laioffer.twitch.model;

import com.laioffer.twitch.db.entity.ItemEntity;

//前段发给我们的时候，要告诉我们要favorite哪个item
public record FavoriteRequestBody(
        ItemEntity favorite
) {
}
