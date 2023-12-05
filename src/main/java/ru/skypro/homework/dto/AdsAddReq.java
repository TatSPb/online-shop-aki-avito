package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class AdsAddReq {
    private String description;
    private int price;
    private String title;
    //String image; //BizinMitya 05.12 здесь нет картинки в этом dto
}