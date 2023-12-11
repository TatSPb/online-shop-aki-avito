package ru.skypro.homework.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.service.impl.AdService;
import ru.skypro.homework.service.impl.ImageService;

import java.io.IOException;

/**
 * КОНТРОЛЛЕР ПОИСКА ИЗОБРАЖЕНИЙ ПО ИДЕНТИФИКАТОРУ
 */
@RestController
@CrossOrigin(value = "http://localhost:3000")
@Slf4j
public class ImageController {
    private final AdService adService;
    private final ImageService imageService;

    public ImageController(AdService adService, ImageService imageService) {
        this.adService = adService;
        this.imageService = imageService;
    }

    /**
     * УТИЛИТАРНЫЕ МЕТОДЫ.
     * Отдают массив байтов по ссылке на картинку объявления / аватар пользователя.
     */
    @GetMapping(value = "ads/image/{id}")
    public FileSystemResource getAdImage(@PathVariable Integer id) throws IOException {
        return imageService.getAdImage(id);
    }

    @GetMapping(value = "users/avatar/{id}")
    public FileSystemResource getUserImage(@PathVariable Integer id) throws IOException {
        return imageService.getUserImage(id);
    }

    //    /**
//     * ОБНОВЛЕНИЕ КАРТИНКИ ТОВАРА В ОБЪЯВЛЕНИИ.
//     *
//     * @param id   - идентификатор объявления (Integer id)
//     * @param file - файл с новым изображением товара (MultipartFile file).
//     * @return ответ сервера со статусом 200 / 401 / 403 / 404
//     */
//    @Tag(name = "Объявления")
//    @Operation(summary = "Обновить картинку объявления")
//    @ApiResponse(responseCode = "200", description = "OK")
//    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
//    @ApiResponse(responseCode = "403", description = "FORBIDDEN")
//    @ApiResponse(responseCode = "404", description = "NOT_FOUND")
//    @PatchMapping("ads/{id}/image")
//    public ResponseEntity<?> updateImage(@PathVariable Integer id,
//                                         @RequestParam("image") MultipartFile file) throws NoPermissionException {
//        return ResponseEntity.ok().body(adService.updateAdImage(id, file));
//    }
}
