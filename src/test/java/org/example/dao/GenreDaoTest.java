package org.example.dao;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.example.entity.Genre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import org.testcontainers.containers.PostgreSQLContainer;

class GenreDaoTest {
    private final static GenreDao genreDao = GenreDao.getInstance();
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
        String title = "Test5";

        Optional<Genre> foundGenre = genreDao.findById(id, connection);

        Assertions.assertTrue(foundGenre.isPresent());
        Assertions.assertEquals(id, foundGenre.get().getId());
        Assertions.assertEquals(title, foundGenre.get().getTitle());
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;

        genreDao.deleteById(id, connection);
        Assertions.assertFalse(genreDao.findById(1L, connection).isPresent());
    }

    @Test
    public void findAll() {
        Long id3 = 3L;
        String title3 = "Test3";
        createGenre(id3, title3);
        Long id4 = 4L;
        String title4 = "Test4";
        createGenre(id4, title4);

        var all = genreDao.findAll(connection);

        Assertions.assertEquals(3, all.size());
        Assertions.assertEquals("Test3", all.get(1).getTitle());
        Assertions.assertEquals("Test5", all.get(0).getTitle());
    }

    @Test
    public void testSave() {

        String title5 = "Test5";

        Genre genre = new Genre();
        genre.setTitle(title5);

        Genre savedGenre = genreDao.save(genre, connection);

        Assertions.assertNotNull(savedGenre);
        Assertions.assertNotNull(savedGenre.getId());
        Assertions.assertEquals("Test5", savedGenre.getTitle());
    }

    @Test
    public void testUpdate() {

        createGenre(6L, "BeforeUpdate");

        Genre newGenre = new Genre();
        newGenre.setId(6L);
        newGenre.setTitle("AfterUpdate");
        genreDao.update(newGenre, connection);
        var genreTitle = genreDao.findById(6L, connection).get().getTitle();

        Assertions.assertEquals("AfterUpdate", genreTitle);
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