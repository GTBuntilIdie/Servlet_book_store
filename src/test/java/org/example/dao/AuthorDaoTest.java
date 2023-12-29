package org.example.dao;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.entity.Author;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;


class AuthorDaoTest {
    private final static AuthorDao authorDao = AuthorDao.getInstance();
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
        String name = "name5";
        String surname = "surname5";
        Optional<Author> foundAuthor = authorDao.findById(id, connection);

        Assertions.assertTrue(foundAuthor.isPresent());
        Assertions.assertEquals(id, foundAuthor.get().getId());
        Assertions.assertEquals(name, foundAuthor.get().getName());
        Assertions.assertEquals(surname, foundAuthor.get().getSurname());
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;

        authorDao.deleteById(id, connection);
        Assertions.assertFalse(authorDao.findById(1L, connection).isPresent());
    }

    @Test
    public void findAll() {
        Long id3 = 3L;
        String name3 = "name3";
        String surname3 = "surname3";
        createAuthor(id3, name3, surname3);
        Long id4 = 4L;
        String name4 = "name4";
        String surname4 = "surname3";
        createAuthor(id4, name4, surname4);

        var all = authorDao.findAll(connection);

        Assertions.assertEquals(3, all.size());
        Assertions.assertEquals("name3", all.get(1).getName());
        Assertions.assertEquals("name4", all.get(2).getName());
    }

    @Test
    public void testSave() {

        String name5 = "name5";
        String surname5 = "surname5";

        Author author = new Author();
        author.setName(name5);
        author.setSurname(surname5);

        Author savedAuthor = authorDao.save(author, connection);

        Assertions.assertNotNull(savedAuthor);
        Assertions.assertNotNull(savedAuthor.getId());
        Assertions.assertEquals("name5", savedAuthor.getName());
    }

    @Test
    public void testUpdate() {

        createAuthor(6L, "name6", "surname6");

        Author newAuthor = new Author();
        newAuthor.setId(6L);
        newAuthor.setName("before");
        newAuthor.setSurname("before");
        authorDao.update(newAuthor, connection);
        var genreName = authorDao.findById(6L, connection).get().getName();

        Assertions.assertEquals("before", genreName);
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

}