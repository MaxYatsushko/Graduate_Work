package ru.skypro.homework.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    LocalDateTime createdAt;


    String text;

    /*
    author	integer($int32)

id автора комментария
authorImage	string

ссылка на аватар автора комментария
authorFirstName	string

имя создателя комментария
createdAt	integer($int64)

дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970
pk	integer($int32)

id комментария
text	string

текст комментария
     */

}
