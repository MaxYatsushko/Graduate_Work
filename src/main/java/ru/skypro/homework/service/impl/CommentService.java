package ru.skypro.homework.service.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.mapper.CommentsMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentsRepository commentsRepository;
    private final AdsService adsService;

    private final UserService userService;

    public CommentService(CommentsRepository commentsRepository, AdRepository adRepository, AdsService adsService, UserService userService) {
        this.commentsRepository = commentsRepository;
        this.adsService = adsService;
        this.userService = userService;
    }

    public String getCommentAuthorNameByCommentId(Long id){
        return commentsRepository.findById(id).map(com -> com.getUser().getEmail()).orElseThrow(RuntimeException::new);
    }

    public Optional<CommentDto> createComment(Long id, CreateOrUpdateCommentDto createOrUpdateCommentDto, String login) {
        Comment comment = new Comment();
        Optional<Ad> adOptional = adsService.getAdOptionalById(id);
        if(adOptional.isEmpty())
            return Optional.empty();

        Optional<User> userOptional = userService.getUserByLogin(login);
        if(userOptional.isEmpty()){
            throw new RuntimeException("User not found!");
        }
        comment.setAd(adOptional.get());
        comment.setUser(userOptional.get());
        comment.setCreatedAt(System.currentTimeMillis());
        comment.setText(createOrUpdateCommentDto.getText());

        return Optional.of(CommentsMapper.CommentToResponseComment(commentsRepository.save(comment)));
    }

    public List<CommentDto> getAllAdComments(Long id) {
        List<CommentDto> responseCommentList = new ArrayList<>();
        List<Comment> commentList = commentsRepository.findAllByAdId(id);
        for (Comment comment : commentList) {
            responseCommentList.add(CommentsMapper.CommentToResponseComment(comment));
        }
        return responseCommentList;
    }

    @PreAuthorize("hasRole('ADMIN') " +
            "or authentication.name == @commentService.getCommentAuthorNameByCommentId(#commentId)")
    public boolean deleteAdComment(Long adId, Long commentId) {
        if(!commentsRepository.existsById(commentId))
            return false;
        commentsRepository.deleteById(commentId);
        return true;
    }

    @PreAuthorize("hasRole('ADMIN') " +
            "or authentication.name == @commentService.getCommentAuthorNameByCommentId(#commentId)")
    public Optional<CommentDto> updateComment(Long adId, Long commentId, CreateOrUpdateCommentDto updatedComment) {
        Optional<Comment> commentOptional = commentsRepository.findById(commentId);
        if(commentOptional.isEmpty())
            return Optional.empty();
        Comment comment = commentOptional.get();
        comment.setText(updatedComment.getText());
        return Optional.of(CommentsMapper.CommentToResponseComment(commentsRepository.save(comment)));
    }
}