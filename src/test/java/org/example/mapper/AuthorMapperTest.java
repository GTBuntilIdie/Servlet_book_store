package org.example.mapper;

import org.example.dto.AuthorDto;
import org.example.entity.Author;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorMapperTest {

    @Test
    public void testMapToAuthorDto() {
        Author author = new Author();
        author.setId(1L);
        author.setName("John");
        author.setSurname("Doe");

        AuthorDto dto = AuthorMapper.toDto(author);

        assertEquals(author.getId(), dto.getId());
        assertEquals(author.getName(), dto.getName());
        assertEquals(author.getSurname(), dto.getSurname());

    }

    @Test
    public void testMapToAuthorEntity() {
        AuthorDto dto = new AuthorDto();
        dto.setId(1L);
        dto.setName("John");
        dto.setSurname("Doe");

        Author author = AuthorMapper.toEntity(dto);

        assertEquals(dto.getId(), author.getId());
        assertEquals(dto.getName(), author.getName());
        assertEquals(dto.getSurname(), author.getSurname());

    }
}