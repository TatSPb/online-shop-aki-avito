package ru.skypro.homework.model;

import lombok.*;

import javax.persistence.*;

/*** Image data model / Модель данных изображения ***/
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ImageModel {

    /*** Image Id / Уникальный идентификатор изображения ***/
    @Id
    @GeneratedValue //(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Lob
    private byte[] image;

    /*** Ad data model / Модель данных объявления ***/
    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY)
    private AdsModel adsModel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public AdsModel getAdsModel() {
        return adsModel;
    }

    public void setAdsModel(AdsModel adsModel) {
        this.adsModel = adsModel;
    }
}
