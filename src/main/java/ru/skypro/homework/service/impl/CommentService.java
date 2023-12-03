package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CommentCount;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static ru.skypro.homework.utils.AuthorizationUtils.isUserCommentAuthorOrAdmin;

@Service
public class CommentService {
    Logger LOG = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;

    private final UserService userService;

    public CommentService(CommentRepository commentRepository, AdRepository adRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.adRepository = adRepository;
        this.userService = userService;
    }

    /**
     * Маппер для трансформации entity объекта в DTO объект.
     *
     * @param comment - модель комментария (Comment comment)
     * @return CommentDTO - DTO-объект комментария
     */
    public CommentDTO commentToCommentDTO(Comment comment) {
        return new CommentDTO(
                comment.getAuthor().getId(),
                comment.getAuthor().getImage() == null ? null : "/users/avatar/" + comment.getAuthor().getId(),
                comment.getAuthor().getFirstName(),
                comment.getCreatedAt(),
                comment.getCommentId(),
                comment.getText());
    }

    /**
     * Метод для получения всех комментариев к объявлению.
     *
     * @param adId - идентификатор объявления (Integer adId)
     * @return список комментариев к объявлению.
     */
    public CommentCount getCommentsOfAd(Integer adId) {
        LOG.info("Was invoked method GET_COMMENTS_OF_AD");
        Ad ad = adRepository.findById(adId).orElseThrow();
        List<Comment> commentList = commentRepository.findCommentsByAd(ad);
        List<CommentDTO> commentsDTOList = commentList.stream()
                .map(this::commentToCommentDTO)
                .collect(Collectors.toList());
        return new CommentCount(commentsDTOList.size(), commentsDTOList);
    }

    /**
     * Метод для добавления комментария к объявлению.
     *
     * @param adId        - идентификатор объявления (Integer adId)
     * @param textComment - объект с новым комментарием (CreateOrUpdateComment textComment)
     * @return DTO-объект нового комментария.
     */
    public CommentDTO addCommentToAd(Integer adId, CreateOrUpdateComment textComment) {
        LOG.info("Was invoked method AD_COMMENTS_TO_AD");
        User user = userService.getAuthUser();
        Ad ad = adRepository.findById(adId).orElseThrow();
        Comment comment = new Comment(ad,
                user,
                Instant.now().toEpochMilli(), //дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970
                textComment.getText());
        commentRepository.save(comment);
        return commentToCommentDTO(comment);
    }

    /**
     * Метод для удаления комментария из объявления.
     *
     * @param adId      - идентификатор объявления (Integer adId)
     * @param commentId - идентификатор комментария (Integer commentId)
     */
    public void deleteCommentById(Integer adId, Integer commentId) {
        LOG.info("Was invoked method DELETE_COMMENT_BY_ID");
        User user = userService.getAuthUser();
        Comment comment = getCommentByAdIdAndCommentID(adId, commentId);
        if (isUserCommentAuthorOrAdmin(comment, user)) {
            commentRepository.delete(comment);
        } else throw new RuntimeException("Такой комментарий не найден");
    }

    /**
     * Метод для обновления комментария.
     *
     * @param adId      - идентификатор объявления (Integer adId)
     * @param commentId - идентификатор комментария (Integer commentId)
     * @param newText   - объект с новым комментарием (CreateOrUpdateComment newText)
     * @return DTO-объект нового комментария.
     */

    public CommentDTO updateCommentById(Integer adId, Integer commentId, CreateOrUpdateComment newText) {
        LOG.info("Was invoked method UPDATE_COMMENT_BY_ID");
        User user = userService.getAuthUser();
        Comment comment = getCommentByAdIdAndCommentID(adId, commentId);
        if (isUserCommentAuthorOrAdmin(comment, user)) {
            comment.setText(newText.getText());
            commentRepository.save(comment);
            return commentToCommentDTO(comment);
        } else throw new RuntimeException("Такой комментарий не найден");
    }

    /**
     * Метод для получения комментария по ID объявления и ID комментария.
     *
     * @param adId      - идентификатор объявления (Integer adId)
     * @param commentId - идентификатор комментария (Integer commentId)
     * @return объект комментария.
     */
    public Comment getCommentByAdIdAndCommentID(Integer adId, Integer commentId) {
        return commentRepository.findCommentByAdPkAndCommentId(adId, commentId);
    }
}
