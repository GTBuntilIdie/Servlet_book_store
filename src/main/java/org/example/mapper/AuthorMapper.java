package org.example.mapper;

import org.example.dto.AuthorDto;
import org.example.entity.Author;

public class AuthorMapper {

    public AuthorDto mapToAuthorDto(Author author) {
        AuthorDto dto = new AuthorDto();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setSurname(author.getSurname());
        return dto;
    }

    public Author mapToAuthorEntity(AuthorDto dto) {
        Author author = new Author();
        author.setId(dto.getId());
        author.setName(dto.getName());
        author.setSurname(dto.getSurname());
        return author;
    }
}
