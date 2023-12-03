package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.*;

import java.util.List;

/*** Interface for dto->DB->dto mapping (comments) / Интерфейс для преобразования объектов комментариев dto->DB->dto ***/
@Component
@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    /*** Method to create CommentModel / Метод для  создания модели объекта "Комментарий" ***/
    CommentModel toCommentModel(CreateComment createComment);

    /*** Method to convert CommentModel -> CommentDto / Метод для  преобразования модели комментария в dto ***/
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "userModel.id")
    @Mapping(target = "authorFirstName", source = "userModel.firstName")
    @Mapping(target = "authorImage", expression = "java(getImageModel(commentModel))")
    CommentDto toCommentDto (CommentModel commentModel);

    /*** Method to get DB url of user avatar / Метод для получения ссылки на аватар пользователя в БД" ***/
    default String getImageModel(CommentModel commentModel) {
        if (commentModel.getUserModel().getImageModel() == null) {
            return null;
        }
        return "/users/image/" + commentModel.getUserModel().getId() + "/from-db";
    }

    /*** Method to convert List of comment models to List of comment DTOs /
     * Метод для получения списка DTO комментариев из списка моделей комментариев
     ***/
    List<CommentDto> commentModelListToCommentDtoList(List<CommentModel> commentList);
}
