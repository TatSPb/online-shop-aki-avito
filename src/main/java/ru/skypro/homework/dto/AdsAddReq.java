package ru.skypro.homework.dto;

import lombok.Data;

@Data
public class AdsAddReq {
    String description;
    int price;
    String title;
    String image;
}