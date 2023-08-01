package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("ads")
@CrossOrigin(value = "http://localhost:3000")
public class AdController {

    @GetMapping
    public ResponseEntity<ResponseWrapperAds> getAllAds() {
        //TODO Complete the method
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ad> addAds(@RequestPart("image") MultipartFile image,
                                     @RequestBody CreateOrUpdateAds createOrUpdateAds) {
        //TODO Complete the method
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Ad> getFullAd(@PathVariable("id") int id) {
        //TODO Complete the method
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeAds(@PathVariable("id") int id) {
        //TODO Complete the method
        // Return a 204 No Content response
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}")
    public ResponseEntity<Ad> updateAds(@PathVariable("id") int id,
                                        @RequestBody CreateOrUpdateAds updatedAds) {
        //TODO Complete the method
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("me")
    public ResponseEntity<List<Ad>> getMyAds(){
        //TODO Complete the method
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("{id}/image")
    public ResponseEntity<Comment> updateComments(@PathVariable("id") int id,
                                                  @RequestPart("image") MultipartFile image) {
        //TODO Complete the method
        return new ResponseEntity<>(HttpStatus.OK);
    }


}