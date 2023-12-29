package org.example.mapper;

import org.example.dto.BookDto;
import org.example.entity.Book;

import java.util.stream.Collectors;

public class BookMapper {
    public static BookDto toDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setPublicationDate(book.getPublicationDate());
        bookDto.setAuthor(AuthorMapper.toDto(book.getAuthor()));
        bookDto.setGenres(book.getGenres().stream()
                .map(GenreMapper::toDto)
                .collect(Collectors.toSet()));
        return bookDto;
    }

    public static Book toEntity(BookDto bookDto) {
        Book book = new Book();
        book.setId(bookDto.getId());
        book.setTitle(bookDto.getTitle());
        book.setPublicationDate(bookDto.getPublicationDate());
        book.setAuthor(AuthorMapper.toEntity(bookDto.getAuthor()));
        book.setGenres(bookDto.getGenres().stream()
                .map(GenreMapper::toEntity)
                .collect(Collectors.toSet()));
        return book;
    }
}
