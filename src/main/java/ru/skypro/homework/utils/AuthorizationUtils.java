package ru.skypro.homework.utils;


import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;


public class AuthorizationUtils {

    public static boolean isUserAdAuthorOrAdmin(Ad ad, User user){
        return user.getUsername().equals(ad.getAuthor().getUsername()) || user.getRole() == Role.ADMIN;
    }

    public static boolean isUserCommentAuthorOrAdmin(Comment comment, User user){
        return user.getUsername().equals(comment.getAuthor().getUsername()) || user.getRole() == Role.ADMIN;
    }
}
