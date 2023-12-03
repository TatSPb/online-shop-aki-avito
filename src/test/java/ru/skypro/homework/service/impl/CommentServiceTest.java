package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import ru.skypro.homework.dto.CommentDTO;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.Image;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

import java.time.Instant;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CommentServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    AdService adService;
    @Autowired
    AdRepository adRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    AuthService authService;

    @BeforeEach
    public void setUp() {
        authService.register(new RegisterReq("user@gmail.com", "password", "Max",
                "Maximov", "+7915556699", Role.USER), Role.USER);
    }

    /**
     * Тестирование метода commentToCommentDTO() с позитивным сценарием трансформации entity в DTO.
     **/
    @Test
    @WithMockUser("user@gmail.com")
    public void commentToCommentDTO_ReturnCorrectDTO() {
        User author = userService.getAuthUser();
        Ad ad = new Ad(author, new Image("imageName"), 1, "descr", 100, "title");
        Comment comment = new Comment(ad, author, Instant.now().toEpochMilli(), "text");
        CommentDTO commentDto = commentService.commentToCommentDTO(comment);

        Assertions.assertEquals(comment.getAuthor().getId(), commentDto.getAuthor());
        Assertions.assertEquals(comment.getAuthor().getFirstName(), commentDto.getAuthorFirstName());
        Assertions.assertEquals(comment.getCommentId(), commentDto.getPk());
        Assertions.assertEquals(comment.getText(), commentDto.getText());
    }

    /**
     * Тестирование метода getCommentsOfAdd() для получения всех комментариев к объявлению, позитивный сценарий.
     */
    @Test
    @WithMockUser("user@gmail.com")
    public void getCommentsOfAddReturnsAllExistsComments() {
        User author = userService.getAuthUser();
        Image savedImage = imageRepository.save(new Image("imageName"));
        Ad ad = adRepository.save(new Ad(author, "descr", savedImage,
                100, "title"));
        commentService.addCommentToAd(ad.getPk(), new CreateOrUpdateComment("textComment"));
        Assertions.assertEquals(1, commentRepository.findCommentsByAd(ad).size());
    }

    /**
     * Тестирование метода getCommentsOfAdd() для получения всех комментариев к объявлению
     * при отсутствии у объявления комментариев.
     */
    @Test
    @WithMockUser("user@gmail.com")
    public void getAllCommentReturnsEmptyListIfThereIsNoThisComments() {
        User author = userService.getAuthUser();
        Image savedImage = imageRepository.save(new Image("imageName"));
        Ad ad = adRepository.save(new Ad(author, "description", savedImage,
                500, "title"));
        int actual = commentRepository.findCommentsByAd(ad).size();
        Assertions.assertEquals(0, actual);
    }

    /**
     * Тестирование метода deleteCommentById() для удаления комментария к объявлению,
     * если пользователь является автором объявления.
     */
    @Test
    @WithMockUser("user@gmail.com")
    public void deleteCommentByIdIfUserIsAuthor() {
        User author = userService.getAuthUser();
        Image savedImage = imageRepository.save(new Image("imageName"));
        Ad ad = adRepository.save(new Ad(author, "description", savedImage,
                500, "title"));
        commentService.addCommentToAd(ad.getPk(), new CreateOrUpdateComment("textComment"));
        commentService.deleteCommentById(ad.getPk(), 1);
        int actual = commentRepository.findCommentsByAd(ad).size();
        Assertions.assertEquals(0, actual);
    }

    /**
     * Тестирование метода updateCommentById() для обновления комментария к объявлению,
     * позитивный сценарий.
     */
    @Test
    @WithMockUser("user@gmail.com")
    public void UpdateCommentByIdReturnsCorrectComment() {
        User author = userService.getAuthUser();
        Image savedImage = imageRepository.save(new Image("imageName"));
        Ad ad = adRepository.save(new Ad(author, "description", savedImage,
                500, "title"));
        commentService.addCommentToAd(ad.getPk(), new CreateOrUpdateComment("oldComment"));
        Comment newComment = commentRepository.findCommentByCommentId(1);
        String expected = "newComment";
        newComment.setText(expected);
        commentRepository.save(newComment);
        Assertions.assertEquals(expected, commentRepository.findCommentByCommentId(1).getText());
    }

    /**
     * Тестирование метода updateCommentById() для обновления комментария к объявлению,
     * проверка совпадения id автора объявления, имя автора, содержания комментария.
     */
    @Test
    @WithMockUser("user@gmail.com")
    public void getCorrectCommentByAdIdAndCommentID() {
        User author = userService.getAuthUser();
        Image savedImage = imageRepository.save(new Image("imageName"));
        Ad ad = adRepository.save(new Ad(author, "description", savedImage,
                500, "title"));
        commentService.addCommentToAd(ad.getPk(), new CreateOrUpdateComment("textComment"));
        Comment actual = commentRepository.findCommentByAdPkAndCommentId(ad.getPk(), 1);
        Comment expected = new Comment(ad, author, Instant.now().toEpochMilli(), "textComment");

        Assertions.assertEquals(expected.getAuthor().getId(), actual.getAuthor().getId());
        Assertions.assertEquals(expected.getAuthor().getFirstName(), actual.getAuthor().getFirstName());
        Assertions.assertEquals(expected.getText(), actual.getText());
    }
}

