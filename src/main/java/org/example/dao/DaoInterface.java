package org.example.dao;


import org.example.entity.Book;
import org.example.entity.Genre;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface DaoInterface<T, K> {
    Optional<T> findById(K id, Connection connection);

    boolean deleteById(K id, Connection connection);

    List<T> findAll(Connection connection);

    T save(T t, Connection connection);

}

