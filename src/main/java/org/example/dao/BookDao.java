package org.example.dao;

import org.example.connection.ConnectionManager;
import org.example.entity.Book;
import org.example.entity.Genre;
import org.example.exception.DaoException;

import java.sql.*;
import java.sql.Date;
import java.time.ZoneId;
import java.util.*;

public class BookDao implements DaoInterface<Book, Long>{

    private static final BookDao INSTANCE = new BookDao();
    public static BookDao getInstance() {
        return INSTANCE;
    }

    private static final String FIND_ALL_SQL = """
            SELECT id, title, publication_date, author_id
            FROM books
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE id = ?
            """;

    private static final String DELETE_SQL = """
            DELETE FROM books
            WHERE id = ?
            """;
    private static final String SAVE_SQL = """
            INSERT INTO books (title, publication_date, author_id)
            VALUES (?, ?, ?)
            """;
    private static final String UPDATE_SQL = """
            UPDATE books
            SET title = ?,
                publication_date = ?,
                author_id = ?
            WHERE id = ?
            """;
    private static final String ADD_GENRE = """
            INSERT INTO books_to_genres (book_id, genre_id)
            VALUES (?, ?)
            """;
    private static final String DELETE_EXISTING_GENRES = """
            DELETE FROM books_to_genres
            WHERE book_id = ?
            """;

    private final AuthorDao authorDao = AuthorDao.getInstance();

    @Override
    public Optional<Book> findById(Long id, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            var resultSet = preparedStatement.executeQuery();
            Book book = null;
            if (resultSet.next()) {
                book = getBook(resultSet, connection);
            }
            return Optional.ofNullable(book);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Book getBook(ResultSet resultSet, Connection connection) throws SQLException {
        var set = new HashSet<Genre>();
        return new Book(
                resultSet.getLong("id"),
                resultSet.getString("title"),
                resultSet.getTimestamp("publication_date").toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                authorDao.findById(resultSet.getLong("author_id"), connection).orElse(null),
                set
        );
    }

    @Override
    public boolean deleteById(Long id, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(DELETE_SQL)) {
            preparedStatement.setLong(1, id);
            deleteBookGenres(id, connection);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Book> findAll(Connection connection) {
        try (var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Book> books = new ArrayList<>();
            while (resultSet.next()) {
                books.add(getBook(resultSet, connection));
            }
            return books;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Book save(Book book, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setDate(2, Date.valueOf(book.getPublicationDate()));
            preparedStatement.setLong(3, book.getAuthor().getId());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                book.setId(generatedKeys.getLong("id"));
            }
            addGenre(book);
            return book;
        } catch (SQLException e) {
            throw new DaoException(e);
        }

    }

    public void update(Book book, Connection connection) {
        try (var preparedStatement = connection.prepareStatement(UPDATE_SQL)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setDate(2, Date.valueOf(book.getPublicationDate()));
            preparedStatement.setLong(3, book.getAuthor().getId());
            preparedStatement.setLong(4, book.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throw new DaoException(throwables);
        }
    }
    public void deleteBookGenres(Long bookId, Connection connection) throws SQLException {
        try (var preparedStatement = connection.prepareStatement(DELETE_EXISTING_GENRES)) {
            preparedStatement.setLong(1, bookId);
            preparedStatement.executeUpdate();
        }
    }


    public void addGenre(Book book) {
        Set<Genre> genres = book.getGenres();
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(ADD_GENRE)) {
            for (Genre element : genres) {
                preparedStatement.setLong(1, book.getId());
                preparedStatement.setLong(2, element.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
