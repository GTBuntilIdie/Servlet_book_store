package org.example.mapper;

import org.example.dto.GenreDto;
import org.example.entity.Genre;

public class GenreMapper {

    private static final GenreMapper INSTANCE = new GenreMapper();
    public static GenreMapper getInstance() {
        return INSTANCE;
    }

    public GenreDto mapToGenreDto(Genre genre) {
        GenreDto dto = new GenreDto();
        dto.setId(genre.getId());
        dto.setTitle(genre.getTitle());
        return dto;
    }

    public Genre mapToGenreEntity(GenreDto dto) {
        Genre genre = new Genre();
        genre.setId(dto.getId());
        genre.setTitle(dto.getTitle());
        return genre;
    }

}
