package org.example;

import org.example.dao.AuthorDao;
import org.example.dao.BookDao;
import org.example.dao.GenreDao;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.entity.Genre;
import org.example.service.BookService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class JDBCRunner {
    static BookDao bookDao = BookDao.getInstance();

    public static void main(String[] args) {


        bookDao.findById(9L);

    }
}
