package org.example.mapper;

import org.example.dto.GenreDto;
import org.example.entity.Genre;

public class GenreMapper {

    public static GenreDto toDto(Genre genre) {
        GenreDto genreDto = new GenreDto();
        genreDto.setId(genre.getId());
        genreDto.setTitle(genre.getTitle());
        return genreDto;
    }

    public static Genre toEntity(GenreDto genreDto) {
        Genre genre = new Genre();
        genre.setId(genreDto.getId());
        genre.setTitle(genreDto.getTitle());
        return genre;
    }
}
