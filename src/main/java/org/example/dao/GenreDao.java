package org.example.dao;

import org.example.connection.ConnectionManager;
import org.example.entity.Genre;
import org.example.exception.DaoException;

import java.sql.*;
import java.util.*;

public class GenreDao implements DaoInterface<Genre, Long> {

    private static final GenreDao INSTANCE = new GenreDao();
    public static GenreDao getInstance() {
        return INSTANCE;
    }

    private static final String FIND_ALL_SQL = """
            SELECT id, title
            FROM genres
            """;
    private static final String DELETE_SQL = """
            DELETE FROM genres
            WHERE id = ?
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT id, title
            FROM genres
            WHERE id = ?
            """;
    private static final String GET_BOOK_GENRES_SQL= """
            SELECT genre_id
            FROM books_to_genres
            WHERE book_id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO genres (title)
            VALUES (?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE genres
            SET title = ?         
            WHERE id = ?
            """;


    private GenreDao() {
    }

    @Override
    public Optional<Genre> findById(Long id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            var resultSet = preparedStatement.executeQuery();
            Genre genre = null;
            if (resultSet.next()) {
                genre = new Genre(
                        resultSet.getLong("id"),
                        resultSet.getString("title")
                );
            }
            return Optional.ofNullable(genre);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public Set<Genre> getBookGenresByBookId(Long bookID) {

        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(GET_BOOK_GENRES_SQL)) {
            preparedStatement.setLong(1, bookID);
            Set<Genre> genresSet = new HashSet<>();

            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                genresSet.add(getGenre(resultSet));
            }
            return genresSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Genre> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Genre> genres = new ArrayList<>();
            while (resultSet.next()) {
                genres.add(getGenre(resultSet));
            }
            return genres;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private static Genre getGenre(ResultSet resultSet) throws SQLException {
        return new Genre(
                resultSet.getLong("id"),
                resultSet.getString("title")
        );
    }

    @Override
    public Genre save(Genre genre) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, genre.getTitle());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                genre.setId(generatedKeys.getLong("id"));
            }
            return genre;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void update(Genre genre) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, genre.getTitle());
            preparedStatement.setLong(2, genre.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }
}
