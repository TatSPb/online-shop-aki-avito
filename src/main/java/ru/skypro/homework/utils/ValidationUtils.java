package ru.skypro.homework.utils;

public class ValidationUtils {

    public static boolean isNotEmptyAndNotNull(String str){
        return !(str==null||str.isEmpty()||str.isBlank());
    }

}
