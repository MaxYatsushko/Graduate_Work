package ru.skypro.homework.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.component.ImageProcessor;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.ImageRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.InputMismatchException;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class ImageService {

    @Value("${image.dir.path}")
    private String imageDir;

    private final UserService userService;
    private final ImageRepository imageRepository;

    public ImageService(UserService userService, ImageRepository imageRepository) {
        this.userService = userService;
        this.imageRepository = imageRepository;
    }

    public void updateUserAvatar(MultipartFile image, String login) throws IOException {
        Optional<User> userOptional = userService.getUserByLogin(login);
        if (userOptional.isEmpty()) {
            return ;
        }
        User user = userOptional.get();
        if (image == null ||image.getOriginalFilename()==null) {
            return ;
        }
        String fileName = image.getOriginalFilename();

        String newFileName = login.hashCode()
                + "." +ImageProcessor.getExtension(fileName);

        Path filepath = Path.of(imageDir, newFileName);
        Image newImage = processImage(image, filepath, newFileName);
        user.setImage(imageRepository.save(newImage));
        userService.updateUserImage(user);
    }
    public Image addAdImage(MultipartFile image, long adId) throws IOException {

        if (image == null ||image.getOriginalFilename()==null)
            throw new InputMismatchException();

        String fileName = image.getOriginalFilename();
        String newFileName = "ad_"+adId + "." +ImageProcessor.getExtension(fileName);

        Path filepath = Path.of(imageDir, newFileName);

        return imageRepository.save(processImage(image, filepath,newFileName));
    }

    private Image processImage(MultipartFile image, Path filepath,String filename) throws IOException {
        Files.createDirectories(filepath.getParent());
        Files.deleteIfExists(filepath);

        try (InputStream is = image.getInputStream();
             OutputStream os = Files.newOutputStream(filepath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        Image newImage;
        if(imageRepository.existsByFileName(filename)) {
            newImage = imageRepository.findByFileName(filename);
        }
        else
            newImage = new Image();
        newImage.setFileName(filename);
        newImage.setMediaType(image.getContentType());

        return newImage;
    }

    public byte[] getImageBytes(String imageName)  {
        String path = imageDir + "/" + imageName;
        BufferedImage bufferedImage;
        byte[] result;
        try {
            bufferedImage = ImageIO.read(new File(path));
            result = toByteArray(bufferedImage, StringUtils.getFilenameExtension(imageName));
        }catch (IOException e){
            throw new RuntimeException("Image Exception, " + e.getMessage());
        }

        return result;
    }

    public static byte[] toByteArray(BufferedImage bi, String format)
            throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        return baos.toByteArray();

    }

}