package com.laioffer.twitch.model;
import com.fasterxml.jackson.annotation.JsonProperty;

// Spring用内嵌的library去帮我们转换。你只要return的东西是class，他可以帮你转换成json的key value pair这种形式把dynamic数据传递给前端。
// 额外知识：那前端传给后端呢？ 用query parameter。 url后面加个问号，把需要的参数用@RequestParam传进去，更dynamic
public record RegisterBody(
        String username,
        String password,
        @JsonProperty("first_name") String firstName,  // 为嘛需要@JsonProperty？单个单词无所谓，但是两个单词，需要转换成snackCase
        @JsonProperty("last_name") String lastName
) {
}