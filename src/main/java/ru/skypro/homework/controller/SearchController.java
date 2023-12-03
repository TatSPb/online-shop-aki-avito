package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.service.impl.AdService;
/**
 * КОНТРОЛЛЕР ДЛЯ ПОИСКА ОБЪЯВЛЕНИЙ
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class SearchController {

    private final AdService adService;

    @GetMapping
    public ResponseEntity<?> searchAds(@PathVariable String req) throws UnauthorizedException {
        return ResponseEntity.ok().body(adService.searchAds(req));
    }
}
