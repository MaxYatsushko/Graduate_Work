package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CreateOrUpdateAds;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("ads")
@CrossOrigin(value = "http://localhost:3000")
public class CommentController {
    @GetMapping("{id}/comments")
    public ResponseEntity<List<Ad>> getAllUserAds(@PathVariable("id") int id) {
        //TODO Complete the method
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("{id}/comments")
    public ResponseEntity<Ad> addCommentToAd(@PathVariable("id") int id,
                                             @RequestBody CreateOrUpdateAds createdAd) {
        //TODO Complete the method
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{adId}/comments/{commentsId}")
    public ResponseEntity<Comment> deleteAdComment(@PathVariable("adId") int adId, @PathVariable("commentsId") int commentId) {
        //TODO Complete the method
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("{adId}/comments/{commentsId}")
    public ResponseEntity<Comment> patchAdComment(@PathVariable("adId") int adId,
                                                  @PathVariable("commentsId") int commentId,
                                                  @RequestBody CreateOrUpdateAds updatedAds) {
        //TODO Complete the method
        return new ResponseEntity<>(HttpStatus.OK);
    }

}