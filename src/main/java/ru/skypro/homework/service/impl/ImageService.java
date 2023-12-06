package ru.skypro.homework.service.impl;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {
    private ImageRepository imageRepository;

    private UserRepository userRepository;
    private AdRepository adRepository;
    private static final Logger LOG = LoggerFactory.getLogger(AdService.class);
    @Value("${path.to.ad.images}/")
    private String pathToAdImages;

    @Value("${path.to.user.images}/")
    private String pathToUserImages;

    /**
     * Метод для обновления картинки объявления.
     *
     * @param id   - идентификатор объявления (Integer id)
     * @param file - картинка объявления (MultipartFile file)
     */
    public void updateAdImage(Integer id, MultipartFile file) {
        LOG.info("Was invoked method UPDATE_AD_IMAGE_ImServ");
        init();
        Ad ad = adRepository.findById(id).orElseThrow();

        String imageName = generateRandomFileName(file);

        if (ad.getImage() == null) {
            Image image = new Image(imageName);
            imageRepository.save(image);
            ad.setImage(image);
            adRepository.save(ad);
        } else {
            Image image = ad.getImage();
            deleteAdImageIfExists(image.getImageName());
            image.setImageName(imageName);
            imageRepository.save(image);
        }
        File tempFile = new File(
                Path.of(pathToAdImages).toAbsolutePath().toFile(),
                imageName);
        writeFile(tempFile, file);
    }


    /**
     * Метод для удаления картинки объявления.
     *
     * @param fileName - имя картинки (String fileName)
     * @return вывод в консоль информации, успешно ли удален аватар
     */
    private void deleteAdImageIfExists(String fileName) {
        Path path = Paths.get(Path.of(pathToUserImages).toAbsolutePath().toString(),
                fileName);

        try {
            boolean result = Files.deleteIfExists(path);
            if (result) {
                System.out.println("File is successfully deleted.");
            } else {
                System.out.println("File deletion failed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для обновления аватара (изображения) пользователя.
     *
     * @param username - имя пользователя (String userName)
     * @param file     - новый аватар (MultipartFile file)
     */
    public void updateUserImage(String username, MultipartFile file) {
        LOG.info("Was invoked method UPDATE_USER_IMAGE");
        init();
        User user = userRepository.findUserByUsername(username).orElseThrow();
        String imageName = generateRandomFileName(file);
        if (user.getImage() == null) {
            Image image = new Image(imageName);
            imageRepository.save(image);
            user.setImage(image);
            userRepository.save(user);
        } else {
            Image image = user.getImage();
            deleteAvatarIfExists(image.getImageName());
            image.setImageName(imageName);
            imageRepository.save(image);
        }
        File tempFile = new File(
                Path.of(pathToUserImages).toAbsolutePath().toFile(),
                imageName);
        writeFile(tempFile, file);
    }

    /**
     * Метод для удаления аватара (изображения) пользователя.
     *
     * @param fileName - имя аватара (String fileName)
     * @return вывод в консоль информации, успешно ли удален аватар
     */
    private void deleteAvatarIfExists(String fileName) {
        Path path = Paths.get(Path.of(pathToUserImages).toAbsolutePath().toString(),
                fileName);

        try {
            boolean result = Files.deleteIfExists(path);
            if (result) {
                System.out.println("File is successfully deleted.");
            } else {
                System.out.println("File deletion failed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        File adImageDir = new File(pathToAdImages);
        File userImageDir = new File(pathToUserImages);
        if (!adImageDir.exists()) {
            adImageDir.mkdirs(); //Creates the directory named by this abstract pathname
        }
        if (!userImageDir.exists()) {
            userImageDir.mkdirs();
        }
    }

    /**
     * Метод для генерации случайного имени файла.
     */
    private String generateRandomFileName(MultipartFile file) {
        String imageName = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        while (imageRepository.findAdImageByImageName(imageName) != null) {
            imageName = UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        }
        return imageName;
    }

    /**
     * Метод для записи файла.
     */
    private void writeFile(File tempFile, MultipartFile file) {
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(file.getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found!");
        } catch (
                IOException e) {
            throw new RuntimeException();
        }
    }
}
