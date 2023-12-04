package ru.skypro.homework.dto;

import lombok.Data;
import java.util.List;

@Data
public class AdsCount {

    Integer count;
    List<AdDTO> results;

    public AdsCount(List<AdDTO> results) {
        this.count = results.size();
        this.results = results;
    }
}
