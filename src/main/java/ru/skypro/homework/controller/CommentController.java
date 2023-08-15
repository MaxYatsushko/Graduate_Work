package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Operation(summary = "Get comments of ad", tags = "Comments",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentsDto.class))}),
                    @ApiResponse(
                            responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(
                            responseCode = "404", description = "Not found")
            }
    )
    @GetMapping("{id}/comments")
    public ResponseEntity<CommentsDto> getAllAdComments(@PathVariable("id") Integer id) {

        return ResponseEntity.ok(commentService.getAllAdComments(id));
    }

    @Operation(summary = "Add comment to ad", tags = "Comments",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentDto.class))}),
                    @ApiResponse(
                            responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(
                            responseCode = "404", description = "Not found")
            }
    )
    @PostMapping("{id}/comments")
    public ResponseEntity<CommentDto> addCommentToAd(@PathVariable("id") Integer id,
                                                     @RequestBody CreateOrUpdateCommentDto comment) {

        return commentService.createComment(id, comment, SecurityContextHolder.getContext().getAuthentication().getName())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound()
                        .build());
    }

    @Operation(summary = "Delete comment", tags = "Comments",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK"),
                    @ApiResponse(
                            responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(
                            responseCode = "403", description = "Forbidden"),
                    @ApiResponse(
                            responseCode = "404", description = "Not found")
            }
    )
    @DeleteMapping("{adId}/comments/{commentId}")
    @PreAuthorize("hasRole('ADMIN') OR authentication.name == @commentService.getCommentAuthorNameByCommentId(#commentsId)")
    public ResponseEntity<?> deleteCommentFromAd(@PathVariable("adId") Integer adId, @PathVariable("commentId") Integer commentId) {

        if(commentService.deleteCommentFromAd(adId, commentId))
            return ResponseEntity.ok().build();

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Update comment", tags = "Comments",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentDto.class))}),
                    @ApiResponse(
                            responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(
                            responseCode = "403", description = "Forbidden"),
                    @ApiResponse(
                            responseCode = "404", description = "Not found")
            }
    )
    @PatchMapping("{adId}/comments/{commentId}")
    @PreAuthorize("hasRole('ADMIN') OR authentication.name == @commentService.getCommentAuthorNameByCommentId(#commentId)")
    public ResponseEntity<CommentDto> updateCommentFromAd(@PathVariable("adId") Integer adId,
                                                     @PathVariable("commentId") Integer commentId,
                                                     @RequestBody CreateOrUpdateCommentDto updatedComment) {

        return commentService.updateCommentFromAd(adId, commentId, updatedComment)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}