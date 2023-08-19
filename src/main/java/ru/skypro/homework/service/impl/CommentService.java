package ru.skypro.homework.service.impl;

import lombok.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.exceptions.UserException;
import ru.skypro.homework.mapper.CommentsMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.CommentsRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentsRepository commentsRepository;
    private final AdService adService;
    private final UserService userService;

    public CommentService(CommentsRepository commentsRepository, AdService adService, UserService userService) {
        this.commentsRepository = commentsRepository;
        this.adService = adService;
        this.userService = userService;
    }

    /**
     * adds new comment to existing ad
     * @param id - id of ad
     * @param createdCommentDto - dto of new comment
     * @return Optional'<'CommentDto'>' - created dto
     * @throws UserException
     */
    public Optional<CommentDto> createComment(Integer id, CreateOrUpdateCommentDto createdCommentDto, String login) {

        Optional<Ad> adOptional = adService.getAdOptionalById(id);
        if(adOptional.isEmpty()) {
            return Optional.empty();
        }

        Optional<User> userOptional = userService.getUserByLogin(login);
        if(userOptional.isEmpty()) {
            throw new UserException("User not found!");
        }

        Comment comment = new Comment();
        comment.setAd(adOptional.get());
        comment.setUser(userOptional.get());
        comment.setCreatedAt(System.currentTimeMillis());
        comment.setText(createdCommentDto.getText());

        return Optional.of(CommentsMapper.createCommentDtoFromComment(commentsRepository.save(comment)));
    }

    /**
     * gets ad's comments sorted by createdAt, list receives by id from db
     * @param id - id of ad
     * @return commentsDto - created dto
     */
    public CommentsDto getAllAdComments(Integer id) {

        List<CommentDto> responseCommentList = commentsRepository.findAllByAdId(id).
                stream().
                map(CommentsMapper::createCommentDtoFromComment).
                sorted(new CommentsComparator()).
                collect(Collectors.toList());

        return CommentsMapper.createCommentsDtoFromListCommentDto(responseCommentList);
    }

    /**
     * deletes ad's comment
     * @param idAd - id of ad(useless)
     * @param idComment - id of comment
     * @return boolean - result
     */
    public boolean deleteCommentFromAd(Integer idAd, Integer idComment) {

        if(!commentsRepository.existsById(idComment)) {
            return false;
        }

        commentsRepository.deleteById(idComment);
        return true;
    }

    /**
     * updates ad's comment
     * @param idAd - id of ad(useless)
     * @param idComment - id of comment
     * @param updatedCommentDto - dto CreateOrUpdateCommentDto
     * @return Optional'<'CommentDto'>' - created dto bases on updated comment
     */
    public Optional<CommentDto> updateCommentFromAd(Integer idAd, Integer idComment, CreateOrUpdateCommentDto updatedCommentDto) {

        Optional<Comment> commentOptional = commentsRepository.findById(idComment);
        if(commentOptional.isEmpty()) {
            return Optional.empty();
        }

        Comment comment = commentOptional.get();
        comment.setText(updatedCommentDto.getText());

        return Optional.of(CommentsMapper.createCommentDtoFromComment(commentsRepository.save(comment)));
    }

    /**
     * gets name of author if exists
     * @param id - id of comment
     * @return string - AuthorName
     * @throws UserException
     */
    public String getCommentAuthorNameByCommentId(Integer id){
        return commentsRepository.findById(id)
                .map(comment-> comment.getUser().getEmail())
                .orElseThrow(() -> new UserException("Name of author by commentId did not find"));
    }
}