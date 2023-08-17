package com.laioffer.twitch.db.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.laioffer.twitch.external.model.Clip;
import com.laioffer.twitch.external.model.Stream;
import com.laioffer.twitch.external.model.Video;
import com.laioffer.twitch.model.ItemType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

// ItemEntity的目的是要存在database里
/*public record ItemEntity(...) {...}: This is a record class definition for ItemEntity. A record is a compact way to define classes that are mainly used to store data (like a DTO - Data Transfer Object).
@Id Long id: This annotation specifies that the id field is the primary key of the table. It corresponds to the unique identifier of the item in the database.
@JsonProperty("twitch_id") String twitchId: This annotation specifies that the twitchId field should be mapped to a JSON property named "twitch_id" when serializing/deserializing JSON data.
The other fields in the record represent various attributes of an item, such as title, URL, thumbnail URL, broadcaster name, game ID, and item type.
*/
@Table("items")   // 一定要写，要不然run ItemRepository的时候没办法把ItemEntity回复的东西连接到这个record里
public record ItemEntity(
        @Id Long id,// twitch return时也需要gdbi id和twitch id。因为他考量用的时候万一twitch id不对或者messed up，或者想自己去create新的再用的话，可以用这个filed - 这是database的key  //@Id 意思就是这是table的primary key
        @JsonProperty("twitch_id") String twitchId,
        String title,
        String url,
        @JsonProperty("thumbnail_url") String thumbnailUrl,
        @JsonProperty("broadcaster_name") String broadcasterName,
        @JsonProperty("game_id") String gameId,
        @JsonProperty("item_type") ItemType type
) {
    // ItemEntity objects are all created from Video/Clip/Stream class objects. Therefore, no need to create a object with input parameters mentioned above.
    // If I use Video/Clip/Stream object to create ItemEntity is more convenient as this will be the sole case to create this class objects.
    public ItemEntity(String gameId, Video video) {
        this(null, video.id(), video.title(), video.url(), video.thumbnailUrl(), video.userName(), gameId, ItemType.VIDEO);
    }

    public ItemEntity(Clip clip) {
        this(null, clip.id(), clip.title(), clip.url(), clip.thumbnailUrl(), clip.broadcasterName(), clip.gameId(), ItemType.CLIP);
    }

    public ItemEntity(Stream stream) {
        this(null, stream.id(), stream.title(), null, stream.thumbnailUrl(), stream.userName(), stream.gameId(), ItemType.STREAM);
    }

}
