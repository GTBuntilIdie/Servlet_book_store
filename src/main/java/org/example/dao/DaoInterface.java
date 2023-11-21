package org.example.dao;

import java.util.List;
import java.util.Optional;

public interface DaoInterface<T, K> {

    Optional<T> findById(K id);

    boolean delete(K id);

    List<T> findAll();

    T save(T t);

}
