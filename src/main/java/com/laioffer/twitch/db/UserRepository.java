package com.laioffer.twitch.db;

import com.laioffer.twitch.db.entity.UserEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

// 为什么一定要extend？ 因为其他controller都用annotation。在这里没有写annotation，为什么？因为annotation都写在parent class里了。所以Spring通过这个parent class的extension来执行功能
public interface UserRepository extends ListCrudRepository<UserEntity, Long> {
    List<UserEntity> findByLastName(String lastName);   //find 和 findAll 什么区别 - 都一样。
    List<UserEntity> findByFirstName(String firstName);
    UserEntity findByUsername(String username);   // 不需要跟上面一样return list 因为return的是unique的一个User Entity
    @Modifying
    @Query("UPDATE users SET first_name = :firstName, last_name = :lastName WHERE username = :username")  //用户刚创建用户，repository不用这个创建操作，。。。？？？？
    void updateNameByUsername(String username, String firstName, String lastName);//Does 这三个参数顺序matter？No
}
