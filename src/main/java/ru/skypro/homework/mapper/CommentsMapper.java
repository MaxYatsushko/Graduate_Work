package ru.skypro.homework.mapper;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.Image;

import java.util.List;

public class CommentsMapper {

    /**
     * creates CommentDto from comment
     * @param comment - Comment
     * @return commentDto - created dto
     */
    public static CommentDto createCommentDtoFromComment(Comment comment){

        Image image = comment.getUser().getImage();

        return new CommentDto(
                comment.getUser().getId(),
                image == null ? null : ("/images/" + image.getFileName()),
                comment.getUser().getFirstName(),
                comment.getCreatedAt(),
                comment.getId(),
                comment.getText());
    }

    /**
     * creates CommentsDto from list of CommentDto ad via createOrUpdateAdDto in db
     * @param commentDtoList - list of CommentDto
     * @return commentsDto - created dto
     */
    public static CommentsDto createCommentsDtoFromListCommentDto(List<CommentDto> commentDtoList){
        return new CommentsDto(commentDtoList.size(), commentDtoList);
    }

}
