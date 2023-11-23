package org.example.service;

import org.example.dao.GenreDao;
import org.example.dto.GenreDto;
import org.example.mapper.GenreMapper;

import java.util.Optional;

public class GenreService {

    private static final GenreService INSTANCE = new GenreService();
    public static GenreService getInstance() {
        return INSTANCE;
    }

    private final GenreMapper mapper = GenreMapper.getInstance();
    private final GenreDao dao = GenreDao.getInstance();

    public GenreDto create(GenreDto genreDto) {
        return mapper.mapToGenreDto(dao.save(mapper.mapToGenreEntity(genreDto)));
    }

    public GenreDto read(long id) {
        return dao.findById(id).map(mapper::mapToGenreDto).orElse(null);
    }

    public GenreDto update(long id, GenreDto genreDto) {
        return dao.findById(id)
                .map(entity -> {
                    entity.setTitle(genreDto.getTitle());
                    dao.update(entity);
                    return entity;
                })
                .map(mapper::mapToGenreDto)
                .orElse(null);
    }

    public void delete(long id) {
        Optional.ofNullable(read(id))
                .map(dto -> {
                    dao.delete(id);
                    return dto;
                });

    }
}
