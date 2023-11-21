package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.UserModel;

import java.util.List;
import java.util.Optional;

/*** User repository / Репозиторий пользователей ***/
@Repository
public interface UserRepository extends JpaRepository <UserModel, Integer>{
    @Query(value = "select * from users order by id", nativeQuery = true)
    List<UserModel> findAllUsers();
    Optional<UserModel> getUserByUsernameIgnoreCase(String username);
    Optional<UserModel> findByUsername(String username);


}
