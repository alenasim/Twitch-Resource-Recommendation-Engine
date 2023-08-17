// 这是可以拿来对DB做操作的interface了。我们不需要在里面implement要去执行的内容，因为我们extend了parent interface
package com.laioffer.twitch.db;

import com.laioffer.twitch.db.entity.ItemEntity;
import org.springframework.data.repository.ListCrudRepository;
/*Spring Data JDBC?
Spring Data JDBC is a part of the larger Spring Data project and provides a convenient and efficient way to work with
relational databases using the Java Persistence API (JPA) and Spring's programming model. While Spring Data JPA focuses
on the JPA standard, Spring Data JDBC provides an alternative approach that is more lightweight and closer to working
with plain SQL.

Spring Data JDBC is a suitable choice for projects that require a lightweight, direct SQL-based approach to database interactions,
especially when performance and control over SQL queries are essential. It provides a compromise between full control over SQL
and the convenience of a higher-level abstraction for working with relational databases within Spring applications.*/
// CRUD 什么意思？ 增删查改 - Spring Data JDBC的supported query method都有什么？怎么知道那些key words？去官方网找
public interface ItemRepository extends ListCrudRepository<ItemEntity, Long> {   //<ItemEntity - query return的就是ItemEntity, Long - primary key是用long typed的>
    // 会有自带的基本功能从parent interface里继承来的 - findAll(), saveAll() etc 不需要自己定义了，因为自带。
    // 重点是，按照我自己需要的query，假如parent class里不包括的话，可以在这里自己写一些需要的API。
    // Spring其他function用annotation去执行，所以它的语法结构或者命名不重要。但是这里的function名很重要。Spring按照它的函数名去找到他需要执行的内容。
    // 那怎么正确的设置函数名？ 参考Spring的官方doc，或者写代码的时候IntelliJ也会给你推荐有效的名字。
    ItemEntity findByTwitchId(String twitchId);  // function name很重要，因为spring按照你的命名方式去知道你要run的是什么内容。twitch拼错会significantly影响

}
