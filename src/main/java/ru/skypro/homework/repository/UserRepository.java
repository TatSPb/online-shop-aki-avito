package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.model.User;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {


    Optional<User> findUserByUsername(String username);
    Optional<User> findUserById(Integer id);

    Optional<User> findUserByPassword(String password);
}
