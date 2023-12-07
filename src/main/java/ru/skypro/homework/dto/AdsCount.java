package ru.skypro.homework.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
public class AdsCount {

    private Integer count;
    private List<AdDTO> results;

    public AdsCount(List<AdDTO> results) {
        this.count = results.size();
        this.results = results;
    }
}
