package ru.skypro.homework.controller;

import org.springframework.http.MediaType;
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
    public AdDto getAllAds() {
        return new AdDto();
    }


    /*** Method to set a new ad  / Метод для добавления нового объявления **/
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AdDto addAds(Authentication authentication,
                        @RequestPart("properties") CreateAds properties,
                        @RequestPart("image") MultipartFile file) throws IOException {
        return new AdDto();
    }


    /*** Method to get an ad data  / Метод для получения информации о объявлении **/
    @GetMapping("{id}")
    public FullAdDto getAds(@PathVariable Integer id) {
        return new FullAdDto();
    }


    /*** Method to delete an ad   / Метод для удаления объявления **/
    @DeleteMapping("{id}")
    public AdDto removeAd(Authentication authentication, @PathVariable int id) {
        return new AdDto();
    }


    /*** Method to update an ad data  / Метод для обновления информации об объявлении **/
    @PatchMapping("{id}")
    public AdDto updateAds(Authentication authentication, @PathVariable int id, @RequestBody CreateAds createAds) {
        return new AdDto();
    }


    /*** Method to get an authorized user ad  / Метод для получения объявления авторизованного пользователя **/
    @GetMapping("me")
    public ResponseWrapperAds getAdsMe() {
        return new ResponseWrapperAds();
    }


    /*** Method to update an ad image  / Метод для обновления картинки объявления ***/
    @PatchMapping(value = "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AdDto updateImage(@PathVariable Integer id, @RequestParam MultipartFile image) {
        return new AdDto();
    }



    /*** METHODS TO DEAL WITH COMMENTS / МЕТОДЫ ДЛЯ РАБОТЫ С КОММЕНТАРИЯМИ ***/


    /*** Method to get an ad comments  / Метод для получения комментариев объявления ***/
    @GetMapping("{id}/comments")
    public ResponseWrapperComment getComments(@PathVariable Integer id) {
        return new ResponseWrapperComment();
    }


    /*** Method to add a comment to ad  / Метод для добавления комментария к объявлению ***/
    @PostMapping("{id}/comments")
    public CommentDto addComment(@PathVariable Integer id,
                                 @RequestBody CreateComment createComment,
                                 Authentication authentication) {
        return new CommentDto();
    }


    /*** Method to delete a comment from ad  / Метод для удаления комментария из объявления ***/
    @DeleteMapping("{id}")
    public CommentDto removeComment(Authentication authentication, int id) {
        return new CommentDto();
    }


    /*** Method to update an ad comment / Метод для обновления комментария объявления ***/
    @PatchMapping("{id}")
    public CommentDto updateComment(Authentication authentication,
                                    @PathVariable("adId") Integer adId,
                                    @PathVariable("commentId") Integer commentId,
                                    @RequestBody CommentDto commentDto) {
        return new CommentDto();
    }
}
