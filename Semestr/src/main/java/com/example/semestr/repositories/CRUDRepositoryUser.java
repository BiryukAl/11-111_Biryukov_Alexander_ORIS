package com.example.semestr.repositories;

import com.example.semestr.entities.User;
import com.example.semestr.exceptions.DbException;
import com.example.semestr.exceptions.NoFoundRows;
import com.example.semestr.exceptions.NotUniqueLogin;

import java.util.List;

public interface CRUDRepositoryUser {

    void save(User user) throws DbException;

    List<User> findAll();

    User findById(Long id);

    void update(User user) throws NoFoundRows, NotUniqueLogin;

    void delete(Long id);

}
