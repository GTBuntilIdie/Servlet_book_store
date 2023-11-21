package org.example;

import org.example.dao.BookDao;
import org.example.dao.GenreDao;
import org.example.entity.Author;
import org.example.entity.Book;

import java.time.LocalDate;

public class JDBCRunner {
    private static final BookDao bookDao = BookDao.getInstance();
    public static void main(String[] args) {
        Author author = new Author();
        author.setId(1L);
        author.setName("Lev");
        author.setSurname("Tolstoy");

        LocalDate localDate = LocalDate.now();

        Book book = new Book();
        book.setTitle("Voina i mir");
        book.setAuthor(author);
        book.setPublicationDate(localDate);

        bookDao.save(book);


    }
}
