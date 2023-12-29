package org.example.mapper;

import org.example.dto.GenreDto;
import org.example.entity.Genre;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreMapperTest {

    @Test
    void testMapToGenreDto() {
        Genre genre = new Genre();
        genre.setTitle("Action");

        GenreDto dto = GenreMapper.toDto(genre);
        assertEquals(genre.getTitle(), dto.getTitle());
    }
    @Test
    public void testMapToGenreEntity() {
        GenreDto dto = new GenreDto();
        dto.setTitle("Action");
        Genre genre = GenreMapper.toEntity(dto);

        assertEquals(dto.getId(), genre.getId());
        assertEquals(dto.getTitle(), genre.getTitle());
    }
}