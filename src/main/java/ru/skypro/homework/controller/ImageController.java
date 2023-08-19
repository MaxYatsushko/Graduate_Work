package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.service.impl.ImageService;

@RestController
@RequestMapping
@CrossOrigin(value = "http://localhost:3000")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @Operation(summary = "Get image", tags = "Images",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = "OK",
                            content = {@Content(mediaType = "application/octet-stream",
                                    array = @ArraySchema(schema = @Schema(type = "byte")))}),
                    @ApiResponse(
                            responseCode = "400", description = "Bad request")
            }
    )
    @GetMapping(value = "/images/{imageName}")
    public byte[] getImage(@PathVariable String imageName){

        return imageService.getImageBytes(imageName);
    }
}
