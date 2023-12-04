package ru.skypro.homework.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ads")
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;

    @ManyToOne(targetEntity = User.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @OneToOne(targetEntity = Image.class, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private Image image;


    private String description;
    private Integer price;
    private String title;

    @OneToMany(mappedBy = "ad", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Comment> comments =new ArrayList<>();

    public Ad(User author, Image image, Integer pk, String description, Integer price, String title) {
        this.author = author;
        this.image = image;
        this.pk = pk;
        this.description = description;
        this.price = price;
        this.title = title;
    }

    public Ad(User author, String description, Image image, Integer price, String title) {
        this.author = author;
        this.image = image;
        this.description = description;
        this.price = price;
        this.title = title;
    }

    public Ad(Integer author, String image, Integer price, String title) {
    }
}
