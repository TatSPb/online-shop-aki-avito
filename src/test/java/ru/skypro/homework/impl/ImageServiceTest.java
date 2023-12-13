package ru.skypro.homework.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AuthServiceImpl;
import ru.skypro.homework.service.impl.ImageService;


@SpringBootTest
public class ImageServiceTest {
    @Value("${path.to.ad.images}/")
    private String pathToAdImages;

    @Value("${path.to.user.images}/")
    private String pathToUserImages;
    @Value("${name.of.test.data.file}")
    private String testFileName;

    @Autowired
    ImageRepository imageRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AdRepository adRepository;
    @Autowired
    ImageService imageService;

    @Autowired
    AuthServiceImpl authService;
    @Mock
    User user;
    @Mock
    Ad ad;
    @Mock
    Image image;
    @Mock
    MultipartFile imageFile;

    @BeforeEach
    public void init() {
        image = new Image("image");
        imageRepository.save(image);

        user = new User("user@gmail.com", "password", "Max", "Maximov",
                "+79215556699", Role.USER);
        userRepository.save(user);

        ad = new Ad(user, "description", image, 500, "title");
        adRepository.save(ad);

        imageFile = new MockMultipartFile(testFileName, new byte[]{1});

    }
    @AfterEach
    public void clearImageDirs(){
        userRepository.deleteAll();
        adRepository.deleteAll();
        imageRepository.deleteAll();
    }

    @Test
    public void updateUserImageCreatesUserImageFileInTheFolder() {
        imageService.updateUserImage(user.getUsername(), imageFile);
        User userFromDB = userRepository.findUserByUsername(user.getUsername()).orElseThrow();
        FileSystemResource resource = new FileSystemResource(pathToUserImages + userFromDB.getImage().getImageName());
        Assertions.assertTrue(resource.exists());
    }

    @Test
    public void updateUserImageThrowsNullPointerExceptionWhenImageFileIsNull() {
       Assertions.assertThrows(NullPointerException.class,() -> imageService.updateUserImage(user.getUsername(), null));
    }

    @Test
    public void updateAdImageThrowsNullPointerExceptionWhenImageFileIsNull() {
        Assertions.assertThrows(NullPointerException.class,() -> imageService.updateAdImage(ad.getPk(), null));
    }

    @Test
    public void updateAdImageCreatesAdImageFileInTheFolder() {
        imageService.updateAdImage(ad.getPk(), imageFile);
        Ad adFromDB = adRepository.findAdByPk(ad.getPk());
        FileSystemResource resource = new FileSystemResource(pathToAdImages + adFromDB.getImage().getImageName());
        Assertions.assertTrue(resource.exists());
    }

    @Test
    public void updateUserImageSetsNullImageOfUserToNotNull(){
        Assertions.assertNull(user.getImage());
        imageService.updateUserImage(user.getUsername(), imageFile);
        User userFromDB = userRepository.findUserByUsername(user.getUsername()).orElseThrow();
        Assertions.assertNotNull(userFromDB.getImage());
    }
    @Test
    public void updateUserImageChangesImageFieldVariableOfUser(){
        User userFromDB = userRepository.findUserByUsername(user.getUsername()).orElseThrow();
        imageService.updateUserImage(userFromDB.getUsername(), imageFile);
        userFromDB = userRepository.findUserByUsername(user.getUsername()).orElseThrow();
        Image oldImage = userFromDB.getImage();
        Assertions.assertNotNull(oldImage);
        MultipartFile newImageFile = new MockMultipartFile(testFileName+"1", new byte[]{2});
        imageService.updateUserImage(userFromDB.getUsername(), newImageFile);
        userFromDB = userRepository.findUserByUsername(user.getUsername()).orElseThrow();
        Assertions.assertNotEquals(userFromDB.getImage(), oldImage);
    }

    @Test
    public void updateAdImageChangesImageFieldVariableOfAd(){
        Image oldImage = adRepository.findAdByPk(ad.getPk()).getImage();
        imageService.updateAdImage(ad.getPk(), imageFile);
        Image newImage = adRepository.findAdByPk(ad.getPk()).getImage();
        Assertions.assertNotEquals(oldImage.getImageName(), newImage.getImageName());
    }

    @Test
    public void getUserImageReturnsFileSystemResource() {
        imageService.updateUserImage(user.getUsername(), imageFile);
        User userFromDB = userRepository.findUserByUsername(user.getUsername()).orElseThrow();
        FileSystemResource resource = imageService.getUserImage(userFromDB.getId());
        Assertions.assertNotNull(resource);
    }
    @Test
    public void getAdImageReturnsFileSystemResource() {
        imageService.updateAdImage(ad.getPk(), imageFile);
        Ad adFromDB = adRepository.findAdByPk(ad.getPk());
        FileSystemResource resource = imageService.getAdImage(adFromDB.getPk());
        Assertions.assertNotNull(resource);
    }
}
