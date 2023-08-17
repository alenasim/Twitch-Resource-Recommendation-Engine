package com.laioffer.twitch.user;

import com.laioffer.twitch.db.UserRepository;
import com.laioffer.twitch.db.entity.UserEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    // 下面的API需要什么dependency？
    private final UserDetailsManager userDetailsManager;   // AppConfig里写的
    private final PasswordEncoder passwordEncoder;         // AppConfig里写的
    private final UserRepository userRepository;

    public UserService(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }
    //register需要transactional吗？
    @Transactional
    public void register(String username, String password, String firstName, String lastName) {
        UserDetails user = User.builder()    // .builder()  - builder pattern,可以在下面写点操作去取builder pattern里自带的API，optional的可以不写。不写的话，直接用default setting
                .username(username)
                .password(passwordEncoder.encode(password))   //存在DB里的是加密过的数据
                .roles("USER")
                .build();
        userDetailsManager.createUser(user);
        userRepository.updateNameByUsername(username, firstName, lastName);  // 这line上面用的都是Spring Security里面的default code，Spring Security不在乎方面操作的User方面的跟security无关的信息。所以我用这API把他们调出来方便操作
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
