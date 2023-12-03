package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.NoPermissonException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdServiceTest {


    @Value("${name.of.test.data.file}")
    private String testFileName;


    @Autowired
    AdRepository adRepository;

    @Autowired
    AdService adService;

    @Autowired
    UserService userService;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthService authService;

    @BeforeEach
    public void setUp() {
        authService.register(new RegisterReq("user@gmail.com", "password", "Max",
                "Maximov", "+7921000000", Role.USER), Role.USER);
    }

    /**
     * Тестирование метода adToAdDTO() с позитивным сценарием трансформации entity в DTO.
     **/
    @Test
    @WithMockUser("user@gmail.com")
    public void adToAdDTO_ReturnsCorrectAdDTO() {
        User user = userService.getAuthUser();
        Ad ad = new Ad(user, new Image("imageName"), 1, "description", 333, "title");
        AdDTO adDTO = adService.adToDTO(ad);
        Assertions.assertEquals(ad.getPk(), adDTO.getPk());
        Assertions.assertEquals("/ads/image/" + ad.getPk(), adDTO.getImage());
        Assertions.assertEquals(ad.getTitle(), adDTO.getTitle());
        Assertions.assertEquals(ad.getPrice(), adDTO.getPrice());
    }

    /**
     * Тестирование метода adToExtendedAd() для преобразования объявления в расширенное объявление (с описанием и данными автора).
     **/
    @Test
    @WithMockUser("user@gmail.com")
    public void adToExtendedAdReturnsCorrectExtendedAd() {
        User user = userService.getAuthUser();
        Ad ad = new Ad(user, new Image("imageName"), 1, "description", 333, "title");
        ExtendedAd extendedAd = adService.adToExtendedAd(ad);
        Assertions.assertEquals("/ads/image/1", extendedAd.getImage());
        Assertions.assertEquals(ad.getPk(), extendedAd.getPk());
        Assertions.assertEquals(ad.getAuthor().getFirstName(), extendedAd.getAuthorFirstName());
        Assertions.assertEquals(ad.getAuthor().getLastName(), extendedAd.getAuthorLastName());
        Assertions.assertEquals(ad.getDescription(), extendedAd.getDescription());
        Assertions.assertEquals(ad.getAuthor().getUsername(), extendedAd.getEmail());
        Assertions.assertEquals(ad.getPrice(), extendedAd.getPrice());
        Assertions.assertEquals(ad.getTitle(), extendedAd.getTitle());
        Assertions.assertEquals(ad.getAuthor().getPhone(), extendedAd.getPhone());
    }

    /**
     * Тестирование метода getAdById() при условии, что такое объявление существует в базе данных
     **/
    @Test
    @WithMockUser("user@gmail.com")
    public void getAdByIdReturnsCorrectExistingAd() throws IOException, UnauthorizedException {
        AdDTO adDTO = adService.addAd(new CreateOrUpdateAd("title", "description", 500),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        Ad ad = adService.getAdById(adDTO.getPk());
        Assertions.assertEquals("title", ad.getTitle());
        Assertions.assertEquals("description", ad.getDescription());
        Assertions.assertEquals(500, ad.getPrice());
    }

    /**
     * Тестирование метода getAdById() при условии, что такое объявление НЕ существует в базе данных
     **/
    @Test
    public void getAdByIdWithNotExistingAdThrowsException() {
        Assertions.assertThrows(NoSuchElementException.class, () -> adService.getAdById(1));
    }

    /**
     * Тестирование метода getAds() при условии, что объявления существуют в базе данных
     **/
    @Test
    @WithMockUser("user@gmail.com")
    public void getAdsReturnsAdsListIfAdsExist() throws IOException, UnauthorizedException {
        adService.addAd(new CreateOrUpdateAd("title-1", "description-1", 500),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        adService.addAd(new CreateOrUpdateAd("title-2", "description-2", 500),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        Assertions.assertTrue(adService.getAds().getCount() == 2);
    }

    /**
     * Тестирование метода getAds() при условии, что в базе данных отсутствуют объявления
     **/
    @Test
    public void getAdsReturnEmptyListIfAdsNotExist() {
        Assertions.assertTrue(adService.getAds().getCount() == 0);
    }

    /**
     * Тестирование метода getAd(int id) для поиска объявления по его идентификатору и возврата расширенного объявления.
     **/
    @Test
    @WithMockUser("user@gmail.com")
    public void getAdReturnsCorrectExtendedAd() throws IOException, UnauthorizedException {
        AdDTO adDTO = adService.addAd(new CreateOrUpdateAd("title", "description", 500),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        Ad ad = adService.getAdById(adDTO.getPk());
        ExtendedAd extendedAd = adService.getAd(ad.getPk());
        Assertions.assertEquals(ad.getPk(), extendedAd.getPk());
        Assertions.assertEquals(ad.getAuthor().getFirstName(), extendedAd.getAuthorFirstName());
        Assertions.assertEquals(ad.getAuthor().getLastName(), extendedAd.getAuthorLastName());
        Assertions.assertEquals(ad.getDescription(), extendedAd.getDescription());
        Assertions.assertEquals(ad.getAuthor().getUsername(), extendedAd.getEmail());
        Assertions.assertEquals(ad.getPrice(), extendedAd.getPrice());
        Assertions.assertEquals(ad.getTitle(), extendedAd.getTitle());
        Assertions.assertEquals(ad.getAuthor().getPhone(), extendedAd.getPhone());
    }

    /**
     * Тестирование метода addAd() для корректного добавления новых объявлений с полностью заполненными полями.
     **/
    @Test
    @WithMockUser("user@gmail.com")
    public void addAddCorrectlyCreatesNewAddWithValidData() throws IOException, UnauthorizedException {
        AdDTO adDTO = adService.addAd(new CreateOrUpdateAd("title", "description", 500),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        Assertions.assertEquals("title", adDTO.getTitle());
        Assertions.assertEquals(500, adDTO.getPrice());
        Assertions.assertEquals(1, adDTO.getAuthor());
    }

    /**
     * Тестирование метода addAd() если при добавлении нового объявления не заполнено поле "Title".
     **/
    @Test
    @WithMockUser("user@gmail.com")
    public void addAddDoesNotCreatesNewAddWithEmptyTitle() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> adService.addAd(new CreateOrUpdateAd("",
                        "description", 500),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName))));
    }

    /**
     * Тестирование метода addAd() если при добавлении нового объявления не заполнено поле "Description".
     **/
    @Test
    @WithMockUser("user@gmail.com")
    public void addAddDoesNotCreatesNewAddWithEmptyDescription() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> adService.addAd(new CreateOrUpdateAd("title",
                        "", 500),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName))));
    }

    /**
     * Тестирование метода addAd() если при добавлении нового объявления не заполнено поле "Price".
     **/
    @Test
    @WithMockUser("user@gmail.com")
    public void addAddDoesNotCreatesNewAddWithBellowZeroPrice() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> adService.addAd(new CreateOrUpdateAd("title",
                        "description", -49),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName))));
    }

    /**
     * Тестирование метода getUserAllAds() для поиска всех объявлений авторизированного пользователя,
     * если они у него существуют.
     */
    @Test
    @WithMockUser("user@gmail.com")
    public void getUserAllAdsReturnsAllExistsUsersAds() throws IOException, UnauthorizedException {
        User user = userService.getAuthUser();
        adService.addAd(new CreateOrUpdateAd("title-1", "description-1", 500),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        adService.addAd(new CreateOrUpdateAd("title-2", "description-2", 500),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        Assertions.assertEquals(2, adService.getUserAllAds().getResults().stream()
                .filter(a -> a.getAuthor() == user.getId()).collect(Collectors.toList()).size());

    }

    /**
     * Тестирование метода getUserAllAds() для поиска всех объявлений авторизированного пользователя,
     * если объявления пользователя отсутствуют.
     */
    @Test
    @WithMockUser("user@gmail.com")
    public void getAllUsersAdReturnsEmptyListIfThereAreNoAds() {
        Assertions.assertEquals(0, adService.getUserAllAds().getCount());
        Assertions.assertTrue(adService.getUserAllAds().getResults().isEmpty());
    }

    /**
     * Тестирование метода updateAdInfo() для обновления объявления, если пользователь является автором объявления и
     * если данные для обновления объявления заполнены корректно
     */
    @Test
    @WithMockUser("user@gmail.com")
    public void updateAdInfoUpdatesAdIfUserIsAuthorAndRequestIsCorrect() throws IOException, UnauthorizedException {
        User user = userService.getAuthUser();
        AdDTO adDTO = adService.addAd(new CreateOrUpdateAd("oldTitle", "oldDescription", 500),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd("newTitle", "newDescription", 1000);
        AdDTO updatedAd = adService.updateAdInfo(adDTO.getPk(), createOrUpdateAd);
        Assertions.assertEquals(user.getId(), updatedAd.getAuthor());
        Assertions.assertEquals(createOrUpdateAd.getTitle(), updatedAd.getTitle());
        Assertions.assertEquals(createOrUpdateAd.getPrice(), updatedAd.getPrice());
    }

    /**
     * Тестирование метода updateAdInfo() для обновления объявления, если запрос от Администратора и
     * если данные для обновления объявления заполнены корректно
     */
    @Test
    @WithMockUser("admin@gmail.com")
    public void updateAdInfoUpdatesAdIfUserIsAdminAndRequestIsCorrect() {
        authService.register(new RegisterReq("admin@gmail.com", "password", "Admin",
                "Adminov", "+79051234567", Role.ADMIN), Role.ADMIN);
        User newUser = new User("user2@gmail.com", "password", "Max",
                "Maximov", "+79115556677");
        newUser.setRole(Role.USER);
        User savedNewUser = userRepository.save(newUser);
        Image savedImage = imageRepository.save(new Image("imageName"));
        Ad ad = adRepository.save(new Ad(savedNewUser, "oldDescription", savedImage,
                500, "oldTitle"));
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd("newTitle", "newDescription", 200);
        AdDTO updatedAd = adService.updateAdInfo(ad.getPk(), createOrUpdateAd);
        Assertions.assertEquals(newUser.getId(), updatedAd.getAuthor());
        Assertions.assertEquals(createOrUpdateAd.getTitle(), updatedAd.getTitle());
        Assertions.assertEquals(createOrUpdateAd.getPrice(), updatedAd.getPrice());
    }

    /**
     * Тестирование метода updateAdInfo() для обновления объявления, если пользователь НЕ является автором объявления,
     * но данные для обновления объявления заполнены корректно
     */
    @Test
    @WithMockUser("user@gmail.com")
    public void updateAdInfoUpdatesAdIfUserIsNotAuthorAndRequestIsCorrect() {
        User newUser = new User("user2@gmail.com", "password", "Kirill",
                "Kirillov", "+73205698756");
        newUser.setRole(Role.USER);
        User savedNewUser = userRepository.save(newUser);
        Image savedImage = imageRepository.save(new Image("imageName"));
        Ad ad = adRepository.save(new Ad(savedNewUser, "oldDescription", savedImage,
                500, "oldTitle"));
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd("newTitle", "newDescription", 200);
        AdDTO updatedAd = adService.updateAdInfo(ad.getPk(), createOrUpdateAd);
        Assertions.assertEquals(newUser.getId(), updatedAd.getAuthor());
        Assertions.assertEquals("oldTitle", updatedAd.getTitle());
        Assertions.assertEquals(500, updatedAd.getPrice());
    }

    /**
     * Тестирование метода updateAdInfo() для обновления объявления, если пользователь является автором объявления, но
     * в обновленном объявлении не заполнил поле "Название объявления".
     */
    @Test
    @WithMockUser("user@gmail.com")
    public void updateAdInfoUpdatesAdIfUserIsAuthorAndTitleIsEmpty() throws IOException, UnauthorizedException {
        AdDTO adDTO = adService.addAd(new CreateOrUpdateAd("oldTitle", "oldDescription", 500),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd("", "newDescription", 200);
        Assertions.assertThrows(IllegalArgumentException.class, () -> adService.updateAdInfo(adDTO.getPk(), createOrUpdateAd));
    }

    /**
     * Тестирование метода updateAdInfo() для обновления объявления, если пользователь является автором объявления, но
     * в обновленном объявлении не заполнил поле "Описание".
     */
    @Test
    @WithMockUser("user@gmail.com")
    public void updateAdInfoUpdatesAdIfUserIsAuthorAndDescriptionIsEmpty() throws IOException, UnauthorizedException {
        AdDTO adDTO = adService.addAd(new CreateOrUpdateAd("oldTitle", "oldDescription", 500),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd("newTitle", "", 200);
        Assertions.assertThrows(IllegalArgumentException.class, () -> adService.updateAdInfo(adDTO.getPk(), createOrUpdateAd));
    }

    /**
     * Тестирование метода updateAdInfo() для обновления объявления, если пользователь является автором объявления, но
     * цена товара указана некорректно (отрицательная).
     */
    @Test
    @WithMockUser("user@gmail.com")
    public void updateAdInfoUpdatesAdIfUserIsAuthorAndPriceIsBellowZero() throws IOException, UnauthorizedException {
        AdDTO adDTO = adService.addAd(new CreateOrUpdateAd("oldTitle", "oldDescription", 500),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        CreateOrUpdateAd createOrUpdateAd = new CreateOrUpdateAd("newTitle", "newDescription", -200);
        Assertions.assertThrows(IllegalArgumentException.class, () -> adService.updateAdInfo(adDTO.getPk(), createOrUpdateAd));
    }

    /**
     * Тестирование метода removeAd() для удаления объявления, если пользователь является автором объявления
     */
    @Test
    @WithMockUser("user@gmail.com")
    public void removeAdRemovesAdIfUserIsAdsAuthor() throws IOException, UnauthorizedException {
        AdDTO adDTO = adService.addAd(new CreateOrUpdateAd("title", "description", 500),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        adService.removeAd(adDTO.getPk());
        Assertions.assertThrows(NoSuchElementException.class, () -> adService.getAdById(adDTO.getPk()));
    }

    /**
     * Тестирование метода removeAd() для удаления объявления, если пользователь НЕ является автором объявления
     * и НЕ является администратором
     */
    @Test
    @WithMockUser("user@gmail.com")
    public void removeAdDoesNotRemoveAdIfUserIsNotAdsAuthorAndNotAdmin() {
        User newUser = new User("user2@gmail.com", "password", "Fedor",
                "Sinicin", "+8444");
        newUser.setRole(Role.USER);
        User savedNewUser = userRepository.save(newUser);
        Image savedImage = imageRepository.save(new Image("imageName"));
        Ad ad = adRepository.save(new Ad(savedNewUser, "descr", savedImage,
                100, "title"));
        adService.removeAd(ad.getPk());
        Assertions.assertNotNull(adService.getAdById(ad.getPk()));
    }

    /**
     * Тестирование метода removeAd() для удаления объявления, если пользователь НЕ является автором объявления,
     * НО является администратором
     */
    @Test
    @WithMockUser("admin@gmail.com")
    public void removeAdRemovesAdIfUserIsNotAdsAuthorAndUserIsAdmin() {
        authService.register(new RegisterReq("admin@gmail.com", "password", "Admin",
                "Adminov", "+79215556699", Role.ADMIN), Role.ADMIN);
        User newUser = new User("user2@gmail.com", "password", "Max",
                "Maximov", "+79118889966");
        newUser.setRole(Role.USER);
        User savedNewUser = userRepository.save(newUser);
        Image savedImage = imageRepository.save(new Image("imageName"));
        Ad ad = adRepository.save(new Ad(savedNewUser, "description", savedImage,
                500, "title"));
        adService.removeAd(ad.getPk());
        Assertions.assertThrows(NoSuchElementException.class, () -> adService.getAdById(ad.getPk()));
    }

    /**
     * Тестирование метода updateAdImage() для обновления картинки объявления,
     * если пользователь является автором объявления
     */
    @Test
    @WithMockUser("user@gmail.com")
    public void updateAdImageUpdatesAdImageIfUserIsAdsAuthor() throws IOException, UnauthorizedException, NoPermissonException {
        AdDTO adDTO = adService.addAd(new CreateOrUpdateAd("title", "description", 100),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        String oldImageName = adService.getAdById(adDTO.getPk()).getImage().getImageName();
        adService.updateAdImage(adDTO.getPk(), new MockMultipartFile("test.jpg", new FileInputStream(testFileName)));
        String newImageName = adService.getAdById(adDTO.getPk()).getImage().getImageName();
        Assertions.assertEquals(oldImageName, newImageName);
    }

    /**
     * Тестирование метода updateAdImage() для обновления картинки объявления, если пользователь НЕ является ни автором
     * объявления, ни администратором
     */
    @Test
    @WithMockUser("user@gmail.com")
    public void updateAdImageUpdatesAdImageThrowsExceptionIfUserIsNotAuthorAndNotAdmin() {
        User newUser = new User("user2@gmail.com", "password", "Max",
                "Maximov", "+79215554477");
        newUser.setRole(Role.USER);
        User savedNewUser = userRepository.save(newUser);
        Image savedImage = imageRepository.save(new Image("imageName"));
        Ad ad = adRepository.save(new Ad(savedNewUser, "description", savedImage,
                500, "title"));
        Assertions.assertThrows(NoPermissonException.class, () -> adService.updateAdImage(ad.getPk(),
                new MockMultipartFile("test.jpg", new FileInputStream(testFileName))));
    }
}
