package ru.skypro.homework.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

import java.io.IOException;


/*** ЭТАП #1 - Написание DTO, контроллеров ***/

@RestController
@RequestMapping("/ads")
@CrossOrigin(value = "http://localhost:3000")
public class AdsController {

/*** METHODS TO WORK WITH ADS / МЕТОДЫ ДЛЯ РАБОТЫ С ОБЪЯВЛЕНИЯМИ ***/

    /*** Method to get a whole set of ads  / Метод для получения всех объявлений **/
    @GetMapping()
    public Ad getAllAds() {
        return new Ad();
    }

    /*** Method to set a new advert  / Метод для добавления нового объявления **/
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Ad addAds(Authentication authentication,
                     @RequestPart("properties") CreateAds properties,
                     @RequestPart("image") MultipartFile file) throws IOException {
        return new Ad();
    }

    /*** Method to get an ad data  / Метод для получения информации о объявлении **/
    @GetMapping("{id}")
    public FullAd getAds(@PathVariable Integer id) {
        return new FullAd();
    }

    /*** Method to delete an ad   / Метод для удаления объявления **/
    @DeleteMapping("{id}")
    public Ad removeAd(Authentication authentication, @PathVariable int id) {
        return new Ad();
    }

    /*** Method to update an ad data  / Метод для обновления информации об объявлении **/
    @PatchMapping("{id}")
    public Ad updateAds(Authentication authentication, @PathVariable int id, @RequestBody CreateAds createAds) {
        return new Ad();
    }

    /*** Method to get an authorized user ad  / Метод для получения объявления авторизованного пользователя **/
    @GetMapping("me")
    public ResponseWrapperAds getAdsMe() {
        return new ResponseWrapperAds();
    }


    /*** Method to update an ad image  / Метод для обновления картинки объявления ***/
    @PatchMapping(value = "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Ad updateImage(@PathVariable Integer id, @RequestParam MultipartFile image) {
        return new Ad();
    }


    /*** METHODS TO WORK WITH COMMENTS / МЕТОДЫ ДЛЯ РАБОТЫ С КОММЕНТАРИЯМИ ***/

    /*** Method to get an ad comments  / Метод для получения комментариев объявления ***/
    @GetMapping("{id}/comments")
    public ResponseWrapperComment getComments(@PathVariable Integer id) {
        return new ResponseWrapperComment();
    }

    /*** Method to add a comment to ad  / Метод для добавления комментария к объявлению ***/
    @PostMapping("{id}/comments")
    public Comment addComment(@PathVariable Integer id,
                              @RequestBody CreateComment createCommentDto,
                              Authentication authentication) {
        return new Comment();
    }

    /*** Method to delete a comment from ad  / Метод для удаления комментария из объявления ***/
    @DeleteMapping("{id}")
    public Comment removeComment(Authentication authentication, int id) {
        return new Comment();
    }

    /*** Method to update an ad comments  / Метод для обновления комментария объявления ***/
    //BizinMitya здесь должно быть 2 id по спецификации - id объявления и id комментария
    @PatchMapping("{id}")
    public class AdsController {
    }

    public Comment updateComment(Authentication authentication, @PathVariable int id, @RequestBody CreateAds createAds) {
        return new Comment();
    }
}
