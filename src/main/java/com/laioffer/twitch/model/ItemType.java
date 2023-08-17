package com.laioffer.twitch.model;

public enum ItemType {        // enum is a special class
    STREAM, VIDEO, CLIP
}
/* This enum also can be written as

public class ItemType {
    public static final int STREAM_TYPE = 0;
    public static final int VIDEO_TYPE = 1;
    public static final int CLIP_TYPE = 2;
}

public static void main(String[] args) {
    ItemType myFavoriteType = 0; // STREAM_TYPE

    // can be rewritten
    myFavoriteType = 3;  // 这样写很容易搞错。enum更好，因为enum里只能选enum里面的选项。不能选除那些以外的option
}

*/
