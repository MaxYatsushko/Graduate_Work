package ru.skypro.homework.controller;

import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.service.impl.ImageService;

@RestController
@RequestMapping
@CrossOrigin(value = "http://localhost:3000")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/images/{imageName}")
    public byte[] getImage(@PathVariable String imageName){

        return imageService.getImageBytes(imageName);
    }
}
