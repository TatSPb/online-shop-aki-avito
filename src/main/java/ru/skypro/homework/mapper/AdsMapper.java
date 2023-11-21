package ru.skypro.homework.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.FullAdDto;
import ru.skypro.homework.model.*;

import java.util.List;

/*** Interface for dto->DB->dto mapping (ads) / Интерфейс для преобразования объектов объявлений (dto->DB->dto) ***/
@Component
@Mapper(componentModel = "spring")
public interface AdsMapper {

    AdsMapper INSTANCE = Mappers.getMapper(AdsMapper.class);

    /*** Method to convert AdsModel -> AdDto / Метод для  преобразования модели объявления в dto ***/
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "userModel.id")
    @Mapping(target = "image", expression = "java(getImageModel(adsModel))")
    AdDto toAdDto(AdsModel adsModel);

    /*** Method to convert AdsModel -> FullAdDto/
     * Метод для преобразования модели объявления в dto полной модели объявления
     * ***/
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "authorFirstName", source = "userModel.firstName")
    @Mapping(target = "authorLastName", source = "userModel.lastName")
    @Mapping(target = "email", source = "userModel.username")
    @Mapping(target = "phone", source = "userModel.phone")
    @Mapping(target = "image", expression = "java(getImageModel(adsModel))")
    FullAdDto toFullAdDto(AdsModel adsModel);

    /*** Method to get image url from AdsModel DB /
     * Метод для получения ссылки на изображение в БД из модели объекта "Объявление"
     * ***/
    default String getImageModel(AdsModel adsModel) {
        if (adsModel.getImageModel() == null) {
            return null;
        }
        return "/ads/image/" + adsModel.getId() + "/from-db";
    }

    /*** Method to convert List of ad models to List of ad DTOs /
     * Метод для получения списка DTO объявлений из списка моделей объявлений
     ***/
    List<AdDto> adListToAdsDtoList(List<AdsModel> adList);
}
