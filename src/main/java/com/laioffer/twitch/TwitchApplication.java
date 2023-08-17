package com.laioffer.twitch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication       //即使下面有psvm。我一定需要这个annotation，这个annotation是在spring运行的时候，告诉它找到这个annotation之后，做一个初始化的操作。
@EnableFeignClients
@EnableCaching    //告诉SpringBoot整个项目需要enable caching
public class TwitchApplication {
    // 所有java application 都需要这个。从这里call functions 去启动程序。但是spring运行不是应为这main function被call了，而是上面的annotation告诉spring，我要被调用。
    public static void main(String[] args) {
        SpringApplication.run(TwitchApplication.class, args);
    }

}
