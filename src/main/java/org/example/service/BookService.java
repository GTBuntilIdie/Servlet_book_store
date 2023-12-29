package org.example.service;

import org.example.connection.ConnectionManager;
import org.example.dao.BookDao;
import org.example.dao.GenreDao;
import org.example.dto.BookDto;
import org.example.dto.GenreDto;
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
    private final BookDao dao = BookDao.getInstance();
    private final GenreDao genreDao = GenreDao.getInstance();


    public BookDto create(BookDto bookDto) {
        try (Connection connection = ConnectionManager.get()) {
            return BookMapper.toDto(dao
                    .save(BookMapper.toEntity(bookDto), connection));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public BookDto read(long id) {
        try (Connection connection = ConnectionManager.get()) {
            return dao.findById(id, connection)
                    .map(BookMapper::toDto)
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
                        entity.setAuthor(BookMapper.toEntity(newDto).getAuthor());
                        entity.setGenres(getBookGenres(newDto.getGenres(), connection));
                        dao.update(entity, connection);
                        return entity;
                    })
                    .map(BookMapper::toDto)
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
                    }).map(BookMapper::toDto);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private Set<Genre> getBookGenres(Set<GenreDto> genresIds, Connection connection) {
        Set<Genre> set = new HashSet<>();
        for (GenreDto genreDto : genresIds) {
            set.add(genreDao.findById(genreDto.getId(), connection).get());
        }
        return set;
    }
}
