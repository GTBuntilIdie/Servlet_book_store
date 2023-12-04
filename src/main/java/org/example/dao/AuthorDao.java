package org.example.dao;

import org.example.connection.ConnectionManager;
import org.example.entity.Author;
import org.example.exception.DaoException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDao implements DaoInterface<Author, Long> {


    private static final AuthorDao INSTANCE = new AuthorDao();
    public static AuthorDao getInstance() {
        return INSTANCE;
    }
    private static final String FIND_ALL_SQL = """
            SELECT id, name, surname
            FROM authors
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;
    private static final String DELETE_SQL = """
            DELETE FROM authors
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO authors (name, surname)
            VALUES (?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE authors
            SET name = ?,
                surname = ?         
            WHERE id = ?
            """;

    @Override
    public Optional<Author> findById(Long id, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            var resultSet = preparedStatement.executeQuery();
            Author author = null;
            if (resultSet.next()) {
                author = getAuthor(resultSet);
            }
            return Optional.ofNullable(author);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    private static Author getAuthor(ResultSet resultSet) throws SQLException {
        return new Author(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("surname")
        );
    }

    @Override
    public boolean deleteById(Long id, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Author> findAll(Connection connection) {
        try (var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Author> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(getAuthor(resultSet));
            }
            return products;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Author save(Author author, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, author.getName());
            preparedStatement.setString(2, author.getSurname());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                author.setId(generatedKeys.getLong("id"));
            }
            return author;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void update(Author author, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, author.getName());
            preparedStatement.setString(2, author.getSurname());
            preparedStatement.setLong(3, author.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }
}
