package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.mapper.CommentsMapper;
import ru.skypro.homework.service.impl.CommentService;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("ads")
@CrossOrigin(value = "http://localhost:3000")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("{id}/comments")
    public ResponseEntity<CommentsDto> getCommentsByAdId(@PathVariable("id") Long id) {
        //List<ResponseComment> responseCommentList = commentService.getAllAdComments(id);
        return ResponseEntity.ok(CommentsMapper.CommentsToResponseWrapperComments(commentService.getAllAdComments(id)));
    }

    @PostMapping("{id}/comments")
    public ResponseEntity<CommentDto> addCommentToAd(@PathVariable("id") Long id,
                                                     @RequestBody CreateOrUpdateCommentDto comment) {
        Optional<CommentDto> responseCommentOptional = commentService.createComment(id,
                comment,
                SecurityContextHolder.getContext().getAuthentication().getName());
        return responseCommentOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @DeleteMapping("{adId}/comments/{commentsId}")
    public ResponseEntity<?> deleteAdComment(@PathVariable("adId") Long adId, @PathVariable("commentsId") Long commentId) {
        if(commentService.deleteAdComment(adId,commentId))
            return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("{adId}/comments/{commentsId}")
    public ResponseEntity<CommentDto> patchAdComment(@PathVariable("adId") Long adId,
                                                     @PathVariable("commentsId") Long commentId,
                                                     @RequestBody CreateOrUpdateCommentDto updatedComment) {

        Optional<CommentDto> responseCommentOptional = commentService.updateComment(adId, commentId, updatedComment);
        return responseCommentOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}