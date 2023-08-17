package com.laioffer.twitch;

import com.laioffer.twitch.model.TwitchErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

// ==》 这些信息写好给Spring Boot用的。等SpringBoot用的好时候，它可以参考这些内容去执行以下代码
@ControllerAdvice  //每次运行一次，都要来这里被advice一下。假如有error丢出来，不用default，我用以下方式的handle
public class GlobalControllerExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    // 假如没有找到exception class的话，用最广泛用意的这个exception - All other exceptions other than below specific exception handlers specified.
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<TwitchErrorResponse> handleDefaultException(Exception e) {
        logger.error("", e);
        return new ResponseEntity<>(
                new TwitchErrorResponse("Something went wrong, please try again later.",
                        e.getClass().getName(),
                        e.getMessage()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    //多加一个exception - 意思就是说如果有这样的exception，给我一个机会catch一下告诉我具体exception是什么
    @ExceptionHandler(ResponseStatusException.class)
    public final ResponseEntity<TwitchErrorResponse> handleResponseStatusException(ResponseStatusException e) {
        logger.error("", e.getCause());
        return new ResponseEntity<>(
                // TwitchErrorResponse是我们自己organized的error response - 哪里出错？为什么出错？
                new TwitchErrorResponse(e.getReason(), e.getCause().getClass().getName(), e.getCause().getMessage()),
                e.getStatusCode()
        );
    }

    //需要的话，也可以加其他的exception在这里。
}
