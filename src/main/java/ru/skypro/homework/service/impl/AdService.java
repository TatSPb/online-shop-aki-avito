package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.AdsCount;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.exception.NoPermissonException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.ImageRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static ru.skypro.homework.utils.AuthorizationUtils.isUserAdAuthorOrAdmin;
import static ru.skypro.homework.utils.ValidationUtils.isNotEmptyAndNotNull;

@Service
public class AdService {
    Logger LOG = LoggerFactory.getLogger(AdService.class);
    @Value("${path.to.ad.images}/")
    private String pathToAdImages;

    private final AdRepository adRepository;
    private final UserService userService;

    private final ImageService imageService;

    private final ImageRepository imageRepository;

    public AdService(AdRepository adRepository, UserService userService, ImageService imageService, ImageRepository imageRepository) {
        this.adRepository = adRepository;
        this.userService = userService;
        this.imageService = imageService;
        this.imageRepository = imageRepository;
    }

    /**
     * Маппер для трансформации entity объекта в DTO объект.
     *
     * @param ad - модель объявления (Ad ad)
     * @return AdDTO - DTO-объект объявления
     */
    public AdDTO adToDTO(Ad ad) {
        return new AdDTO(
                ad.getAuthor().getId(),
                "/ads/image/" + ad.getPk(),
                ad.getPk(),
                ad.getPrice(),
                ad.getTitle());
    }

    /**
     * Метод для преобразования объявления в расширенное объявление (с описанием и данными автора)
     *
     * @param ad - модель расширенного объявления (Ad ad)
     * @return AdDTO - DTO-объект расширенного объявления
     */
    public ExtendedAd adToExtendedAd(Ad ad) {
        return new ExtendedAd(
                ad.getPk(),
                ad.getAuthor().getFirstName(),
                ad.getAuthor().getLastName(),
                ad.getDescription(),
                ad.getAuthor().getUsername(),
                "/ads/image/" + ad.getPk(),
                ad.getAuthor().getPhone(),
                ad.getPrice(),
                ad.getTitle());
    }

    /**
     * Метод для поиска объявления по его идентификатору.
     *
     * @param id - идентификатор объявления (Integer id)
     * @return объект расширенного объявления
     * @throws NoSuchElementException
     */
    public Ad getAdById(Integer id) {
        LOG.info("Was invoked method GET_AD_BY_ID");
        return adRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    /**
     * Метод для возврата списка всех объявлений.
     */
    public AdsCount getAds() {
        LOG.info("Was invoked method GET_ADS");
        List<Ad> adsList = adRepository.findAll();
        return new AdsCount(adsList.stream().map(this::adToDTO).collect(Collectors.toList()));
    }

    /**
     * Метод для поиска объявления по его идентификатору и возврата расширенного объявления.
     *
     * @param id - идентификатор объявления (Integer id)
     * @return DTO-объект расширенного объявления
     */
    public ExtendedAd getAd(int id) {
        LOG.info("Was invoked method GET_AD");
        Ad ad = adRepository.findById(id).orElseThrow();
        return adToExtendedAd(ad);
    }

    /**
     * Метод для создания объявления.
     *
     * @param createOrUpdateAd - созданное объявление (CreateOrUpdateAd createOrUpdateAd)
     * @param file             - картинка объявления (MultipartFile file)
     * @return DTO-объект объявления
     * @throws UnauthorizedException
     * @throws IllegalArgumentException
     */
    public AdDTO addAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile file) throws UnauthorizedException {
        LOG.info("Was invoked method ADD_AD");
        User user = userService.getAuthUser();
        if (user == null) {
            throw new UnauthorizedException();
        }
        if (isNotEmptyAndNotNull(createOrUpdateAd.getDescription()) && createOrUpdateAd.getPrice() >= 0
                && isNotEmptyAndNotNull(createOrUpdateAd.getTitle())) {
            Ad ad = new Ad(user, createOrUpdateAd.getDescription(), null, createOrUpdateAd.getPrice(),
                    createOrUpdateAd.getTitle());
            Ad savedAd = adRepository.save(ad);
            imageService.updateAdImage(savedAd.getPk(), file);
            return adToDTO(savedAd);
        }
        throw new IllegalArgumentException();
    }

    /**
     * Метод для поиска всех объявлений авторизированного пользователя.
     *
     * @return список объявлений пользователя
     */
    public AdsCount getUserAllAds() {
        LOG.info("Was invoked method GET_USER_ALL_ADS");
        User user = userService.getAuthUser();
        List<AdDTO> list = adRepository.findAdsByAuthor(user).stream().map(this::adToDTO).collect(Collectors.toList());
        return new AdsCount(list);
    }

    /**
     * Метод для обновления объявления.
     *
     * @param id               - идентификатор объявления (Integer id)
     * @param createOrUpdateAd - объявление с новыми параметрами (CreateOrUpdateAd createOrUpdateAd)
     * @return DTO-объект обновленного объявления
     */
    public AdDTO updateAdInfo(Integer id, CreateOrUpdateAd createOrUpdateAd) {
        LOG.info("Was invoked method UPDATE_AD_INFO");
        if (isNotEmptyAndNotNull(createOrUpdateAd.getDescription()) && isNotEmptyAndNotNull(createOrUpdateAd.getTitle())
                && createOrUpdateAd.getPrice() >= 0) {
            User user = userService.getAuthUser();
            Ad ad = getAdById(id);
            if (isUserAdAuthorOrAdmin(ad, user)) {
                ad.setDescription(createOrUpdateAd.getDescription());
                ad.setTitle(createOrUpdateAd.getTitle());
                ad.setPrice(createOrUpdateAd.getPrice());
            }
            return adToDTO(adRepository.save(ad));
        }
        throw new IllegalArgumentException();
    }

    /**
     * Метод для удаления объявления.
     *
     * @param id - идентификатор объявления (Integer id)
     */
    public void removeAd(Integer id) {
        LOG.info("Was invoked method REMOVE_AD");
        User user = userService.getAuthUser();
        Ad ad = getAdById(id);
        if (isUserAdAuthorOrAdmin(ad, user)) {
            adRepository.delete(ad);
        }
    }

    /**
     * Метод для обновления картинки объявления.
     *
     * @param adId - идентификатор объявления (Integer id)
     * @param file - картинка объявления (MultipartFile file)
     * @return DTO-объект обновленного объявления
     */
    public AdDTO updateAdImage(Integer adId, MultipartFile file) throws NoPermissonException {
        LOG.info("Was invoked method UPDATE_AD_IMAGE_AdServ");
        User user = userService.getAuthUser();
        Ad ad = getAdById(adId);
        if (isUserAdAuthorOrAdmin(ad, user)) {
            File tempFile = new File(pathToAdImages, ad.getImage().getImageName());
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(file.getBytes());
            } catch (FileNotFoundException e) {
                throw new RuntimeException("File not found!");
            } catch (IOException e) {
                throw new RuntimeException();
            }
            return adToDTO(ad);
        } else {
            throw new NoPermissonException();
        }

    }

    /**
     * Метод для поиска объявлений по части названия.
     *
     * @param req - часть названия объявления (String req)
     * @return список объявлений, соответствующих условию.
     */
    public AdsCount searchAds(String req) {
        List<Ad> list = adRepository.findAdsByTitleContaining(req);
        return new AdsCount(list.stream().map(this::adToDTO).collect(Collectors.toList()));
    }
}
