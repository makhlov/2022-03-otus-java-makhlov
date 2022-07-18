package ru.otus.persistance.dao;

import ru.otus.domain.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> findById(long id);
    Optional<User> findByLogin(String login);
}