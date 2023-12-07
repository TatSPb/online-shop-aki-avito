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
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;

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
    private final AdRepository adRepository;
    private final UserService userService;
    private final ImageService imageService;
    private final AdMapper adMapper;

    private static final Logger LOG = LoggerFactory.getLogger(AdService.class);
    @Value("${path.to.ad.images}/")
    private String pathToAdImages;

    public AdService(AdRepository adRepository, UserService userService, ImageService imageService, AdMapper adMapper) {
        this.adRepository = adRepository;
        this.userService = userService;
        this.imageService = imageService;
        this.adMapper = adMapper;
    }

    /**
     * Метод для возврата списка всех объявлений.
     */
    public AdsCount getAds() {
        LOG.info("Was invoked method GET_ADS");
        List<Ad> adsList = adRepository.findAll();
        return new AdsCount(adsList.stream().
                map(adMapper::adToDTO)
                .collect(Collectors.toList())
        );
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
        // BizinMitya 6/12 эта проверка не нужно, потому что в WebSecurityConfig уже настроено,
        // что запрос может выполнять только аутентифицированный юзер
//        if (user == null) {
//            throw new UnauthorizedException();
//        }
        if (isNotEmptyAndNotNull(createOrUpdateAd.getDescription()) && createOrUpdateAd.getPrice() >= 0
                && isNotEmptyAndNotNull(createOrUpdateAd.getTitle())) {
            Ad ad = new Ad(user, createOrUpdateAd.getDescription(), null, createOrUpdateAd.getPrice(),
                    createOrUpdateAd.getTitle());
            Ad savedAd = adRepository.save(ad);
            imageService.updateAdImage(savedAd.getPk(), file);
            return adMapper.adToDTO(savedAd);
        }
        throw new IllegalArgumentException();
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
        return adMapper.adToExtendedAd(ad);
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
            return adMapper.adToDTO(adRepository.save(ad));
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
        List<Ad> adList = adRepository.findAdsByAuthor(user);
        return new AdsCount(adList.stream().
                map(adMapper::adToDTO)
                .collect(Collectors.toList())
        );
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
            return adMapper.adToDTO(ad);
        } else {
            throw new NoPermissonException();
        }
    }
}
