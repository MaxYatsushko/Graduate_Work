package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.Image;

import java.util.List;

public class CommentsMapper {
    public static CommentDto CommentToResponseComment(Comment comment){
        Image image = comment.getUser().getImage();
        return new CommentDto(
                comment.getUser().getId(),
                image == null? null: ("/images/" + image.getFileName()),
                comment.getUser().getFirstName(),
                comment.getCreatedAt(),
                comment.getId(),
                comment.getText());
    }

    public static CommentsDto CommentsToResponseWrapperComments(List<CommentDto> commentList){
        return new CommentsDto(commentList.size(), commentList);
    }

}
