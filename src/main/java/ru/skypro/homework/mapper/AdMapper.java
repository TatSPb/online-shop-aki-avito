package ru.skypro.homework.mapper;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.AdDTO;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.Ad;

@Service //иначе ошибка выскакивает, что конструктор не может найти bean
public class AdMapper {
    /**
     * Маппер для трансформации entity-объекта Ad в DTO-объект AdDTO.
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
     * Маппер для преобразования entity-объекта Ad в DTO-объект ExtendedDTO.
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
}
