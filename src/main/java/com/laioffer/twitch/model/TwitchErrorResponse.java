package com.laioffer.twitch.model;

public record TwitchErrorResponse(
        String message,
        String error,
        String details
) {
}
//所有的exception， Springboot有default处理，但是我不喜欢，我要自己处理。

