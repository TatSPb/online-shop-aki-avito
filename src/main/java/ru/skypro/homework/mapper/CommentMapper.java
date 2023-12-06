package ru.skypro.homework.mapper;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.model.Comment;

@Service //иначе ошибка выскакивает, что конструктор не может найти bean
public class CommentMapper {
    /**
     * Маппер для трансформации entity-объекта Comment в DTO-объект CommentDTO.
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


}
