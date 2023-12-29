package org.example.mapper;

import org.example.dao.BookDao;
import org.example.dto.AuthorDto;
import org.example.entity.Author;

public class AuthorMapper {

    private static final AuthorMapper INSTANCE = new AuthorMapper();
    public static AuthorMapper getInstance() {
        return INSTANCE;
    }

    public static AuthorDto toDto(Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setName(author.getName());
        authorDto.setSurname(author.getSurname());
        return authorDto;
    }

    public static Author toEntity(AuthorDto authorDto) {
        Author author = new Author();
        author.setId(authorDto.getId());
        author.setName(authorDto.getName());
        author.setSurname(authorDto.getSurname());
        return author;
    }
}
