package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Comment findCommentByAdPkAndCommentId(Integer adId, Integer commentId);

    List<Comment> findCommentsByAd(Ad ad);

    void deleteByAdPkAndCommentId(Integer adId, Integer commentId);

    Comment findCommentByCommentId(Integer commentId);

}
