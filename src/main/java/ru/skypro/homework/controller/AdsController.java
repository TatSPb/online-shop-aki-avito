package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;


/**
 * КОНТРОЛЛЕР ДЛЯ РАБОТЫ С ОБЪЯВЛЕНИЯМИ И КОММЕНТАРИЯМИ
 */

@RestController
@RequestMapping("/ads")
@CrossOrigin(value = "http://localhost:3000")
@RequiredArgsConstructor
public class AdsController {

    /**
     * ПОЛУЧЕНИЕ ВСЕХ ОБЪЯВЛЕНИЙ ОТ ВСЕХ ПОЛЬЗОВАТЕЛЕЙ.
     */
    @Tag(name = "Объявления")
    @Operation(summary = "Получение всех объявлений")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping()
    public ResponseEntity<?> getAllAds() {
        //some code
        return ResponseEntity.ok().body(null);
    }

    /**
     * ДОБАВЛЕНИЕ НОВОГО ОБЪЯВЛЕНИЯ АВТОРИЗИРОВАННЫМ ПОЛЬЗОВАТЕЛЕМ.
     */
    @Tag(name = "Объявления")
    @Operation(summary = "Добавление объявления")
    @ApiResponse(responseCode = "201", description = "CREATED")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDTO> addAd(@RequestPart CreateOrUpdateAd properties,
                                       @RequestPart("image") MultipartFile image) {
        //some code
        return ResponseEntity.ok(null);
    }

    /**
     * ПОЛУЧЕНИЕ ИНФОРМАЦИИ ОБ ОБЪЯВЛЕНИИ.
     */
    @Tag(name = "Объявления")
    @Operation(summary = "Получение информации об объявлении")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @GetMapping("{id}")
    public ResponseEntity<ExtendedAd> getAd(@PathVariable Integer id) {
        //some code
        return ResponseEntity.ok().body(null);
    }

    /**
     * УДАЛЕНИЕ ОБЪЯВЛЕНИЯ.
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
        //some code
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    ;

    /**
     * ОБНОВЛЕНИЕ ИНФОРМАЦИИ ОБ ОБЪЯВЛЕНИИ.
     */
    @PatchMapping("{id}")
    public ResponseEntity<?> updateAds(@PathVariable Integer id,
                                       @RequestBody CreateOrUpdateAd newAd) {
        //some code
        return ResponseEntity.ok().body(null);
    }

    /*** ПОЛУЧЕНИЕ ОБЪЯВЛЕНИЙ АВТОРИЗИРОВАННОГО ПОЛЬЗОВАТЕЛЯ. **/
    @Tag(name = "Объявления")
    @Operation(summary = "Получить объявления авторизованного пользователя")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @ApiResponse(responseCode = "403", description = "FORBIDDEN")
    @ApiResponse(responseCode = "404", description = "NOT_FOUND")
    @GetMapping("me")
    public ResponseEntity<?> getAdsMe() {
        //some code
        return ResponseEntity.ok().body(null);
    }

    /**
     * ОБНОВЛЕНИЕ КАРТИНКИ ТОВАРА В ОБЪЯВЛЕНИИ.
     */
    @Tag(name = "Объявления")
    @Operation(summary = "Обновление картинки объявления")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @ApiResponse(responseCode = "403", description = "FORBIDDEN")
    @ApiResponse(responseCode = "404", description = "NOT_FOUND")
    @PatchMapping(value = "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImage(@PathVariable Integer id,
                                         @RequestParam("image") MultipartFile file) {
        //some code
        return ResponseEntity.ok().body(null);
    }

    /** КОНТРОЛЛЕРЫ ДЛЯ РАБОТЫ С КОММЕНТАРИЯМИ. */

    /**
     * ПОЛУЧЕНИЕ КОММЕНТАРИЕВ К ОБЪЯВЛЕНИЮ.
     */
    @Tag(name = "Комментарии")
    @Operation(summary = "Получение комментариев объявления")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @GetMapping("{id}/comments")
    public ResponseEntity<CommentCount> getComments(@PathVariable(name = "id") Integer id) {
        //some code
        return ResponseEntity.ok().body(null);
    }

    /**
     * ДОБАВЛЕНИЕ КОММЕНТАРИЯ К ОБЪЯВЛЕНИЮ.
     */
    @Tag(name = "Комментарии")
    @Operation(summary = "Добавление комментария к объявлению")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @PostMapping("{id}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable(name = "id") Integer id,
                                                 @RequestBody CreateOrUpdateComment text) {
        //some code
        return ResponseEntity.ok().body(null);
    }

    /**
     * УДАЛЕНИЕ КОММЕНТАРИЯ ИЗ ОБЪЯВЛЕНИЯ.
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
        //commentService.deleteCommentById(adId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * ОБНОВЛЕНИЕ КОММЕНТАРИЯ В ОБЪЯВЛЕНИИ.
     */
    @Tag(name = "Комментарии")
    @Operation(summary = "Обновление комментария")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    @ApiResponse(responseCode = "403", description = "FORBIDDEN")
    @ApiResponse(responseCode = "404", description = "NOT_FOUND")
    @PatchMapping("{adId}/comments/{commentId}")
    //BizinMitya здесь должно быть 2 id по спецификации - id объявления и id комментария
    public ResponseEntity<?> updateComments(@PathVariable Integer adId,
                                            @PathVariable Integer commentId,
                                            @RequestBody CreateOrUpdateComment newText) {
        //some code
        return ResponseEntity.ok().body(null);
    }
}
