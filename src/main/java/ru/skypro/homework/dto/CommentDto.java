package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class CommentDto {

    private int author;
    private String authorImage;
    private String authorFirstName;
    private Long createdAt;//дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970
    private int pk;
    private String text;

    public CommentDto(int author, String authorImage, String authorFirstName, Long createdAt, int pk, String text) {
        this.authorImage = authorImage;
        this.authorFirstName = authorFirstName;
        this.createdAt = createdAt;
        this.pk = pk;
        this.text = text;
    }
}