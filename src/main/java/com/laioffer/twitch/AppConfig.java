package com.laioffer.twitch;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.sql.DataSource;

/*the AppConfig class is responsible for configuring Spring Security settings for authentication and authorization,
including rules for permitting or restricting access to various paths and resources. It also customizes the behavior
of the authentication and authorization process, as well as the handling of login, logout, and error situations.*/

@Configuration
public class AppConfig {
    @Bean      // @Bean意思就是我有这个东西，我要传给你。为什么其他method里没有写@Bean；因为假如再class上面直接写@Service的话，不用写@Bean； 在这里不写@Bean的话，Spring找不到这个API不能执行
    UserDetailsManager users(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }
    // Spring存的密码是hash过的。不是original密码user设置的。- 为嘛要额外提供，而不是Spring Security给你一个default？ 因为user可能希望extra security？
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    /* 如果没有这个file的话，run程序的时候没有执行，会让你log in。我们添加了这个file去bypass login

    Security Filter Chain, we need to configure the security of different paths and also the future frontend assets. By default, everything is protected,
     we dont want that. In AppCOnfig, add the following customization for SecurityFilterChain */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        LogoutConfigurer<HttpSecurity> httpSecurityLogoutConfigurer = http
                .csrf().disable()
                .authorizeHttpRequests(auth ->
                        auth    //官方你给我们authorization registry template，我们按照格式把需要的一个一个填写就好
                                // 后端允许前端基本信息可以随时下载到browser。即使没有login也可以见到一些基本信息
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .requestMatchers(HttpMethod.GET, "/", "/index.html", "/*.json", "/*.png", "/static/**").permitAll()
                                // 开放下面的信息。没有注册的用户可以打开下面几个http
                                .requestMatchers("/hello/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/login", "/register", "/logout").permitAll()  //不需要registration，不需要登陆也可以看信息
                                .requestMatchers(HttpMethod.GET, "/recommendation", "/game", "/search").permitAll()
                                // 其他信息都需要authentication才能access
//                                .requestMatchers(HttpMethod.GET, "/favorite").permitAll()
                                .anyRequest().authenticated()
                )
                // 下面是Fluent APIs。 They specify authorization 和 authentication出错的话会怎么办？ 假如不写的话，SpringBoot就把我带到login page again
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))    // 前端会有401 UNAUTHORIZED popped out
                .and()
                // form based or session based authentication.
                .formLogin()  // session based authentication
                .successHandler((req, res, auth) -> res.setStatus(HttpStatus.NO_CONTENT.value()))
                .failureHandler(new SimpleUrlAuthenticationFailureHandler())   //告诉你出错了
                .and()
                .logout()
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT));
        return http.build();
    }
}
