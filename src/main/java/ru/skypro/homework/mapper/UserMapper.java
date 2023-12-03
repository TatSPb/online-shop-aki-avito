package ru.skypro.homework.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.model.*;

/*** Interface for dto->DB->dto mapping (users) / Интерфейс для преобразования объектов пользователей dto->DB->dto ***/
@Component
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /*** Method to convert UserModel -> UserDto / Метод для  преобразования модели пользователя в dto ***/
    @Mapping(source = "username", target = "email")
    @Mapping(target = "image", expression = "java(getImageModel(userModel))")
    void toUserDto(@MappingTarget UserDto userDto, UserModel userModel);


    /*** Method to convert UserDto  -> UserModel / Метод для  преобразования dto пользователя в его модель  ***/
    @Mapping(ignore = true, target = "userModel.id")
    @Mapping(ignore = true, target = "userModel.imageModel")
    @Mapping(ignore = true, target = "userModel.username")
    void toUserModel(@MappingTarget UserModel userModel, UserDto userDto);


    /*** Method to get DB url of user avatar / Метод для получения ссылки на аватар пользователя в БД" ***/
    default String getImageModel(UserModel userModel) {
        if (userModel.getImageModel() == null) {
            return null;
        }
        return "/users/image/" + userModel.getId() + "/from-db";
    }


    /*** Method to convert RegisterRegDto (user registration data) -> UserModel /
     * Метод для преобразования данных регистрации пользователя (RegisterRegDto) в модель пользователя  ***/
    default UserModel mapRegisterReqToUserModel(RegisterReqDto registerReqDto) {
        UserModel userModel = new UserModel();
        userModel.setUsername(registerReqDto.getUsername());
        userModel.setPassword(registerReqDto.getPassword());
        userModel.setFirstName(registerReqDto.getFirstName());
        userModel.setLastName(registerReqDto.getLastName());
        userModel.setPhone(registerReqDto.getPhone());
        userModel.setRole(Role.USER);
        return userModel;
    }
}
