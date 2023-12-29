package org.example.dao;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.entity.Genre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


class BookDaoTest {
    private final static BookDao bookDao = BookDao.getInstance();
    private static Connection connection;
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );
    @BeforeAll
    static void beforeAll() {
        postgres.start();
        try {
            connection = DriverManager.getConnection(
                    postgres.getJdbcUrl(),
                    postgres.getUsername(),
                    postgres.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Database database = null;
        try {
            database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        Liquibase liquibase = new Liquibase("db/changelog/changelog-master.xml", new ClassLoaderResourceAccessor(), database);
        try {
            liquibase.update("");
        } catch (LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testFindById() {

        Long id = 1L;
        String title = "title5";
        Long authorId = 5L;
        Optional<Book> foundBook = bookDao.findById(id, connection);

        Assertions.assertTrue(foundBook.isPresent());
        Assertions.assertEquals(id, foundBook.get().getId());
        Assertions.assertEquals(title, foundBook.get().getTitle());
        Assertions.assertEquals(authorId, foundBook.get().getAuthor().getId());
    }

    @Test
    public void testDeleteById() {

        Long id = 1L;

        bookDao.deleteById(id, connection);
        Assertions.assertFalse(bookDao.findById(1L, connection).isPresent());
    }

    @Test
    public void findAll() {
        Author author3 = new Author(3L, "name3", "surname3");
        createAuthor(3L, "name3", "surname3");
        Genre genre3 = new Genre(3L, "title3");
        createGenre(3L, "title3");
        Set<Genre> genres3 = new HashSet<>();
        genres3.add(genre3);

        Long id3 = 3L;
        String title3 = "title3";
        LocalDate publicationDate3 = LocalDate.now();

        createBook(id3, title3, publicationDate3, author3.getId());
        createBookGenres(id3, genres3);

        Author author4 = new Author(4L, "name4", "surname4");
        createAuthor(4L, "name4", "surname4");
        Genre genre4 = new Genre(4L, "title4");
        createGenre(4L, "title4");
        Set<Genre> genres4 = new HashSet<>();
        genres4.add(genre4);

        Long id4 = 4L;
        String title4 = "title4";
        LocalDate publicationDate4 = LocalDate.now();

        createBook(id4, title4, publicationDate4, author4.getId());
        createBookGenres(id4, genres4);

        var all = bookDao.findAll(connection);

        Assertions.assertEquals(3, all.size());
        Assertions.assertEquals("title3", all.get(1).getTitle());
        Assertions.assertEquals("title4", all.get(2).getTitle());
    }

    @Test
    public void testSave() {
        Author author5 = new Author(5L, "name5", "surname5");
        createAuthor(5L, "name5", "surname5");
        Genre genre5 = new Genre(5L, "title5");
        createGenre(5L, "title5");
        Set<Genre> genres5 = new HashSet<>();
        genres5.add(genre5);

        String title5 = "title5";
        LocalDate publicationDate5 = LocalDate.now();

        Book book = new Book();
        book.setTitle(title5);
        book.setPublicationDate(publicationDate5);
        book.setAuthor(author5);
        book.setGenres(genres5);

        Book savedBook = bookDao.save(book, connection);

        Assertions.assertNotNull(savedBook);
        Assertions.assertNotNull(savedBook.getId());
        Assertions.assertEquals("title5", savedBook.getTitle());
    }

    @Test
    public void testUpdate() {

        Author author6 = new Author(6L, "name6", "surname6");
        createAuthor(6L, "name6", "surname6");

        Author author7 = new Author(7L, "name7", "surname7");
        createAuthor(7L, "name7", "surname7");
        Genre genre6 = new Genre(6L, "title6");
        createGenre(6L, "title6");
        Set<Genre> genres6 = new HashSet<>();
        genres6.add(genre6);

        Long id6 = 6L;
        String title6 = "title6";
        LocalDate publicationDate6 = LocalDate.now();

        createBook(id6, title6, publicationDate6, author6.getId());
        createBookGenres(id6, genres6);

        Book book = new Book();
        book.setId(6L);
        book.setTitle("after");
        book.setAuthor(author7);
        book.setPublicationDate(publicationDate6);
        book.setGenres(genres6);
        bookDao.update(book, connection);
        var getTitle = bookDao.findById(6L, connection).get().getTitle();

        Assertions.assertEquals("after", getTitle);
    }

    private void createBook(Long id, String title, LocalDate publicationDate, Long authorId) {

        String sql = "INSERT INTO books (id, title, publication_date, author_id) VALUES (?, ?, ?, ?)";
        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, title);
            preparedStatement.setDate(3, Date.valueOf(publicationDate));
            preparedStatement.setLong(4, authorId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void createBookGenres(Long bookId, Set<Genre> genreSet) {
        String sql = "INSERT INTO books_to_genres (book_id, genre_id) VALUES (?, ?)";
        try (var preparedStatement = connection.prepareStatement(sql)) {
            for (Genre element : genreSet) {
                preparedStatement.setLong(1, bookId);
                preparedStatement.setLong(2, element.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void createAuthor(Long id, String name, String surname) {

        String sql = "INSERT INTO authors (id, name, surname) VALUES (?, ?, ?)";
        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, surname);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void createGenre(Long id, String title) {

        String sql = "INSERT INTO genres (id, title) VALUES (?, ?)";
        try (var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, title);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}