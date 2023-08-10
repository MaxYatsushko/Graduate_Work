package ru.skypro.homework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.service.impl.CommentService;


@RestController
@RequestMapping("ads")
@CrossOrigin(value = "http://localhost:3000")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("{id}/comments")
    public ResponseEntity<CommentsDto> getAllAdComments(@PathVariable("id") Integer id) {

        return ResponseEntity.ok(commentService.getAllAdComments(id));
   }

    @PostMapping("{id}/comments")
    public ResponseEntity<CommentDto> addCommentToAd(@PathVariable("id") Integer id,
                                                     @RequestBody CreateOrUpdateCommentDto comment) {

        return commentService.createComment(id, comment, SecurityContextHolder.getContext().getAuthentication().getName())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound()
                        .build());
    }

    @DeleteMapping("{adId}/comments/{commentsId}")
    public ResponseEntity<?> deleteCommentFromAd(@PathVariable("adId") Integer adId, @PathVariable("commentsId") Integer commentsId) {

        if(commentService.deleteCommentFromAd(adId, commentsId))
            return ResponseEntity.ok().build();

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("{adId}/comments/{commentsId}")
    public ResponseEntity<CommentDto> updateCommentFromAd(@PathVariable("adId") Integer adId,
                                                     @PathVariable("commentsId") Integer commentId,
                                                     @RequestBody CreateOrUpdateCommentDto updatedComment) {

        return commentService.updateCommentFromAd(adId, commentId, updatedComment)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}