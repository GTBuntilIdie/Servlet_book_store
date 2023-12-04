package org.example.service;

import org.example.connection.ConnectionManager;
import org.example.dao.BookDao;
import org.example.dao.GenreDao;
import org.example.dto.BookDto;
import org.example.entity.Genre;
import org.example.mapper.BookMapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class BookService {

    private static final BookService INSTANCE = new BookService();
    public static BookService getInstance() {
        return INSTANCE;
    }


    private final BookMapper mapper = BookMapper.getInstance();
    private final BookDao dao = BookDao.getInstance();
    private final GenreDao genreDao = GenreDao.getInstance();


    public BookDto create(BookDto bookDto) {
        try (Connection connection = ConnectionManager.get()) {
            return mapper.mapToBookDto(dao
                    .save(mapper.mapToBookEntity(bookDto, connection), connection));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public BookDto read(long id) {
        try (Connection connection = ConnectionManager.get()) {
            return dao.findById(id, connection)
                    .map(mapper::mapToBookDto)
                    .orElse(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public BookDto update(long id, BookDto newDto) {
        try (Connection connection = ConnectionManager.get()) {
            return dao.findById(id, connection)
                    .map(entity -> {
                        entity.setTitle(newDto.getTitle());
                        entity.setPublicationDate(newDto.getPublicationDate());
                        entity.setAuthor(mapper.mapToBookEntity(newDto, connection).getAuthor());
                        entity.setGenres(getBookGenres(newDto.getGenreIds(), connection));
                        dao.update(entity, connection);
                        return entity;
                    })
                    .map(mapper::mapToBookDto)
                    .orElse(null);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(long id) {
        try (Connection connection = ConnectionManager.get()) {
            dao.findById(id, connection)
                    .map(entity -> {
                        dao.deleteById(id, connection);
                        return entity;
                    }).map(mapper::mapToBookDto);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private Set<Genre> getBookGenres(Set<Long> genresIds, Connection connection) {
        Set<Genre> set = new HashSet<>();
        for (Long id : genresIds) {
            set.add(genreDao.findById(id, connection).get());
        }
        return set;
    }
}
