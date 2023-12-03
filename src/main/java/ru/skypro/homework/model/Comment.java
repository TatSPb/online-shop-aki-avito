package ru.skypro.homework.model;

import javax.persistence.*;


@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer commentId;
    @ManyToOne(targetEntity = Ad.class, fetch = FetchType.EAGER)
    @JoinColumn(name="ad_id")
    Ad ad;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name="author_id")
    User author;

    Long createdAt;
    String text;

    public Comment() {
    }

    public Comment(Ad ad, User author, Long createdAt, String text) {
        this.ad = ad;
        this.author = author;
        this.createdAt = createdAt;
        this.text = text;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
