package ru.skypro.homework.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/*** Comment data model / Модель данных комментария ***/

@Entity
@Data
@Table(name = "comments")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CommentModel {

    /*** Comment Id / Уникальный идентификатор комментария ***/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /*** Date&Time of comment creation / Дата и время создания комментария ***/
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /*** Comment text content / Текст комментария ***/
    @Column(name = "text")
    private String text;

    /*** Comment author / Автор комментария ***/
    @ManyToOne
    private UserModel userModel;

    /*** Ad data model / Модель данных объявления ***/
    @ManyToOne
    private AdsModel adsModel;

    public CommentModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public AdsModel getAdsModel() {
        return adsModel;
    }

    public void setAdsModel(AdsModel adsModel) {
        this.adsModel = adsModel;
    }
}
