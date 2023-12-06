package org.example.mapper;

import org.example.dto.GenreDto;
import org.example.entity.Genre;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreMapperTest {
    private GenreMapper genreMapper = GenreMapper.getInstance();
    @Test
    void testMapToGenreDto() {
        Genre genre = new Genre();
        genre.setId(1L);
        genre.setTitle("Action");

        GenreDto dto = genreMapper.mapToGenreDto(genre);
        assertEquals(genre.getId(), dto.getId());
        assertEquals(genre.getTitle(), dto.getTitle());
    }
    @Test
    public void testMapToGenreEntity() {
        GenreDto dto = new GenreDto();
        dto.setId(1L);
        dto.setTitle("Action");

        Genre genre = genreMapper.mapToGenreEntity(dto);

        assertEquals(dto.getId(), genre.getId());
        assertEquals(dto.getTitle(), genre.getTitle());
    }
}