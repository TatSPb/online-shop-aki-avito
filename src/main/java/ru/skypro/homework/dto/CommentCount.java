package ru.skypro.homework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Data
@Component
public class CommentCount {
    private Integer count;
    private List<CommentDTO> results = new ArrayList<>();

    public CommentCount(List<CommentDTO> results) {
        this.count = results.size();
        this.results = results;
    }

}