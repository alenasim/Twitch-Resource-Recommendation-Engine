/*用init去创建需要的table*/

DROP TABLE IF EXISTS favorite_records;
DROP TABLE IF EXISTS authorities;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;
/* when running the code, if table previously exists, delete the table before running the code */

CREATE TABLE users
(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,    /*基本就是这个配件，也可以用UUID - id是主件，不能没有；从1开始auto-increment，不用手动组建*/
    username VARCHAR(50) NOT NULL UNIQUE,   /*username 可以用为primary key吗？ 可以但是不方便因为改username的时候不方便改多有的数据*/
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    password VARCHAR(100) NOT NULL,
    enabled  TINYINT      NOT NULL DEFAULT 1
);

CREATE TABLE authorities /*只有这个table link to username，其他都link到id。为什么？因为implement spring security官方schema的要求*/
(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    username  VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE items
(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    twitch_id VARCHAR(255) UNIQUE NOT NULL,   /*为嘛不用twitch id做id？十有八九不会改，但是用外部的id去设置关系的话，掌控能力太少。而且database还没存的时候，twitch id not null但是数据库id是null；可辨别有没有存*/
    title TEXT,   /*TEXT, 长度很长的用text，比如说Description里面很长的内容。但是很短的内容 比如说订单名，就可以用VARCHAR*/
    url VARCHAR(255),
    thumbnail_url VARCHAR(255),
    broadcaster_name VARCHAR(255),
    game_id VARCHAR(255), /*game_id 和twitch id 的区别？ game_id用在itemEntity， twitch id指的是从twich来的clip id，video id，这些id都跟某一个game相关，所以我们要练一个game id*/
    type VARCHAR(255)
);

CREATE TABLE favorite_records   /*foreign key用来干嘛？没有的话，dp还是可以跑起来。*/
(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,      /*user喜欢的所有id*/
    item_id INT NOT NULL,      /*喜欢这个item的所有的user*/
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, /*什么时候点赞的*/
    /*上面schema里面，没有任何操作去理解favorite_record跟items table 和 user table有关。怎么specify favorite_record table和上面的table是连起来的呢？foreign key*/
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,  /*不能銮驾user_id 一定要加user table里面有的id；还可以另加constraints i.e. ON DELETE CASCADE 假如user的id没有的话，在这里没有连接好，会报错。但是写完可以cascade掉*/
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
    UNIQUE KEY unique_item_and_user_combo (item_id, user_id)  /*prevent having redundant info, 这名字define有什么用？假如出错的话，它会告诉你这个名字违反了，可以debug*/
);

