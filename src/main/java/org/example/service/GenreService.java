package org.example.service;

import org.example.connection.ConnectionManager;
import org.example.dao.GenreDao;
import org.example.dto.GenreDto;
import org.example.mapper.GenreMapper;
import org.example.mapper.GenreMapperImpl;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public class GenreService {
    private static final GenreService INSTANCE = new GenreService();
    public static GenreService getInstance() {
        return INSTANCE;
    }
    private final GenreDao dao = GenreDao.getInstance();

    public GenreDto create(GenreDto genreDto) {
        try (Connection connection = ConnectionManager.get()) {
            return GenreMapper.toDto(dao.save(GenreMapper
                    .toEntity(genreDto), connection));}
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public GenreDto read(long id) {
        try (Connection connection = ConnectionManager.get()) {
            return dao.findById(id, connection)
                    .map(GenreMapper::toDto).orElse(null); }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public GenreDto update(long id, GenreDto genreDto) {
        try (Connection connection = ConnectionManager.get()) {
            return dao.findById(id, connection)
                .map(entity -> {
                    entity.setTitle(genreDto.getTitle());
                    dao.update(entity, connection);
                    return entity;
                })
                .map(GenreMapper::toDto)
                .orElse(null); }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(long id) {
        try (Connection connection = ConnectionManager.get()) {
            Optional.ofNullable(read(id))
                .map(dto -> {
                    dao.deleteById(id, connection);
                    return dto;
                }); }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
