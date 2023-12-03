package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.exception.NoPermissonException;
import ru.skypro.homework.exception.UnauthorizedException;
import ru.skypro.homework.service.impl.AdService;
import ru.skypro.homework.service.impl.CommentService;

/**
 * КОНТРОЛЛЕР ДЛЯ РАБОТЫ С ОБЪЯВЛЕНИЯМИ И КОММЕНТАРИЯМИ
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdsController {

    private final CommentService commentService;
    private final AdService adService;
    Logger LOG = LoggerFactory.getLogger(AdsController.class);
    /**
     * ПОЛУЧЕНИЕ ВСЕХ ОБЪЯВЛЕНИЙ ОТ ВСЕХ ПОЛЬЗОВАТЕЛЕЙ.
     *
     * @return: ответ сервера со статусом 200
     */
    @Tag(name = "Объявления")
    @Operation(summary = "Получение всех объявлений")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping()
    public ResponseEntity<?> getAllAds() {
        return ResponseEntity.ok().body(adService.getAds());
    }

    /**
     * ДОБАВЛЕНИЕ НОВОГО ОБЪЯВЛЕНИЯ АВТОРИЗИРОВАННЫМ ПОЛЬЗОВАТЕЛЕМ.
     *
     * @param properties - объект созданного объявления (CreateOrUpdateAd properties)
     * @param image      - файл с изображением товара (MultipartFile image).
     * @return ответ сервера со статусом 200 / 401
     */
    @Tag(name = "Объявления")
    @Operation(summary = "Добавление объявления")
    @ApiResponse(responseCode = "201", description = "CREATED")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDTO> addAd(@RequestPart CreateOrUpdateAd properties,
                                       @RequestPart("image") MultipartFile image) throws UnauthorizedException {
        return ResponseEntity.ok(adService.addAd(properties, image));
    }

    /**
     * ПОЛУЧЕНИЕ КОММЕНТАРИЕВ К ОБЪЯВЛЕНИЮ.
     *
     * @param id - идентификатор объявления (Integer id);
     * @return ответ сервера со статусом 200 / 401
     */
    @Tag(name = "Комментарии")
    @Operation(summary = "Получение комментариев объявления")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @GetMapping("/{id}/comments")
    public ResponseEntity<CommentCount> getComments(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok().body(commentService.getCommentsOfAd(id));
    }

    /**
     * ДОБАВЛЕНИЕ КОММЕНТАРИЯ К ОБЪЯВЛЕНИЮ.
     *
     * @param id   - идентификатор объявления (Integer id)
     * @param text - объект с текстом комментария (CreateOrUpdateComment text)
     * @return ответ сервера со статусом 200 / 401
     */
    @Tag(name = "Комментарии")
    @Operation(summary = "Добавление комментария к объявлению")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable(name = "id") Integer id,
                                                 @RequestBody CreateOrUpdateComment text) {
        return ResponseEntity.ok().body(commentService.addCommentToAd(id, text));
    }

    /**
     * ПОЛУЧЕНИЕ ИНФОРМАЦИИ ОБ ОБЪЯВЛЕНИИ.
     *
     * @param id - идентификатор объявления (Integer id)
     * @return ответ сервера со статусом 200 / 401
     */
    @Tag(name = "Объявления")
    @Operation(summary = "Получение информации об объявлении")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @GetMapping("{id}")
    public ResponseEntity<ExtendedAd> getAd(@PathVariable Integer id) {
        return ResponseEntity.ok().body(adService.getAd(id));
    }

    /**
     * УДАЛЕНИЕ ОБЪЯВЛЕНИЯ.
     *
     * @param id - идентификатор объявления (Integer id)
     * @return ответ сервера со статусом 200 / 204 / 401 / 403 / 404
     */
    @Tag(name = "Объявления")
    @Operation(summary = "Удалить объявление")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "204", description = "NO_CONTENT")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @ApiResponse(responseCode = "403", description = "FORBIDDEN")
    @ApiResponse(responseCode = "404", description = "NOT_FOUND")
    @DeleteMapping("{id}")
    public ResponseEntity<?> removeAd(@PathVariable Integer id) {
        adService.removeAd(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * ОБНОВЛЕНИЕ ИНФОРМАЦИИ ОБ ОБЪЯВЛЕНИИ.
     *
     * @param id    - идентификатор объявления (Integer id)
     * @param newAd - объект созданного объявления (CreateOrUpdateAd properties)
     * @return ответ сервера со статусом 200 / 401 / 403 / 404
     */
    @PatchMapping("{id}")
    public ResponseEntity<?> updateAds(@PathVariable Integer id,
                                       @RequestBody CreateOrUpdateAd newAd) {
        return ResponseEntity.ok().body(adService.updateAdInfo(id, newAd));
    }

    /**
     * УДАЛЕНИЕ КОММЕНТАРИЯ ИЗ ОБЪЯВЛЕНИЯ.
     *
     * @param adId      - идентификатор объявления (Integer adId)
     * @param commentId - идентификатор комментария (Integer commentId)
     * @return ответ сервера со статусом 200 / 401 / 403 / 404
     */
    @Tag(name = "Комментарии")
    @Operation(summary = "Удаление комментария")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @ApiResponse(responseCode = "403", description = "FORBIDDEN")
    @ApiResponse(responseCode = "404", description = "NOT_FOUND")
    @DeleteMapping("{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComments(@PathVariable Integer adId,
                                            @PathVariable Integer commentId) {
        commentService.deleteCommentById(adId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * ОБНОВЛЕНИЕ КОММЕНТАРИЯ В ОБЪЯВЛЕНИИ.
     *
     * @param adId      - идентификатор объявления (Integer adId)
     * @param commentId - идентификатор комментария (Integer commentId)
     * @param newText   - объект с новым текстом комментария (CreateOrUpdateComment newText)
     * @return ответ сервера со статусом 200 / 401 / 403 / 404
     */
    @Tag(name = "Комментарии")
    @Operation(summary = "Обновление комментария")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @ApiResponse(responseCode = "403", description = "FORBIDDEN")
    @ApiResponse(responseCode = "404", description = "NOT_FOUND")
    @PatchMapping("{adId}/comments/{commentId}")
    public ResponseEntity<?> updateComments(@PathVariable Integer adId,
                                            @PathVariable Integer commentId,
                                            @RequestBody CreateOrUpdateComment newText) {
        return ResponseEntity.ok().body(commentService.
                updateCommentById(adId, commentId, newText));
    }

    /**
     * ПОЛУЧЕНИЕ ОБЪЯВЛЕНИЙ АВТОРИЗИРОВАННОГО ПОЛЬЗОВАТЕЛЯ.
     *
     * @return ответ сервера со статусом 200 / 401 / 403 / 404
     */
    @Tag(name = "Объявления")
    @Operation(summary = "Получить объявления авторизованного пользователя")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @ApiResponse(responseCode = "403", description = "FORBIDDEN")
    @ApiResponse(responseCode = "404", description = "NOT_FOUND")
    @GetMapping("/me")
    public ResponseEntity<?> getAdsMe() {
        return ResponseEntity.ok().body(adService.getUserAllAds());
    }

    /**
     * ОБНОВЛЕНИЕ КАРТИНКИ ТОВАРА В ОБЪЯВЛЕНИИ.
     *
     * @param id   - идентификатор объявления (Integer id)
     * @param file - файл с новым изображением товара (MultipartFile file).
     * @return ответ сервера со статусом 200 / 401 / 403 / 404
     */
    @Tag(name = "Объявления")
    @Operation(summary = "Обновление картинки объявления")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @ApiResponse(responseCode = "403", description = "FORBIDDEN")
    @ApiResponse(responseCode = "404", description = "NOT_FOUND")
    @PatchMapping("ads/{id}/image")
    public ResponseEntity<?> updateImage(@PathVariable Integer id,
                                         @RequestParam("image") MultipartFile file) throws NoPermissonException {
        LOG.info("Was invoked controller Обновление картинки объявления");
        return ResponseEntity.ok().body(adService.updateAdImage(id, file));
    }
}


