package org.example.dao;

import org.example.connection.ConnectionManager;
import org.example.entity.Book;
import org.example.exception.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private final AuthorDao authorDao = AuthorDao.getInstance();
    private final GenreDao genreDao = GenreDao.getInstance();

    @Override
    public Optional<Book> findById(Long id) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            var resultSet = preparedStatement.executeQuery();
            Book book = null;
            if (resultSet.next()) {
                book = getBook(resultSet);
            }
            return Optional.ofNullable(book);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Book getBook(ResultSet resultSet) throws SQLException {
        return new Book(
                resultSet.getLong("id"),
                resultSet.getString("title"),
                resultSet.getTimestamp("publicationDate").toLocalDateTime().toLocalDate(),
                authorDao.findById(resultSet.getLong("author_id")).orElse(null),
                genreDao.getBookGenresByBookId(resultSet.getLong("id"))
                );
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
    public List<Book> findAll() {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {
            var resultSet = preparedStatement.executeQuery();
            List<Book> books = new ArrayList<>();
            while (resultSet.next()) {
                books.add(getBook(resultSet));
            }
            return books;

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Book save(Book book) {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setTimestamp(2, convertTime(book.getPublicationDate()));
            preparedStatement.setLong(3, book.getAuthor().getId());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                book.setId(generatedKeys.getLong("id"));
            }
            return book;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Timestamp convertTime(LocalDate date) {
        return Timestamp.valueOf(date.atStartOfDay());
    }

}
