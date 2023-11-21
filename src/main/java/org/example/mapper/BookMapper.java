package org.example.mapper;


import org.example.dao.AuthorDao;
import org.example.dao.GenreDao;
import org.example.dto.BookDto;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.entity.Genre;

import java.util.HashSet;
import java.util.Set;

public class BookMapper {

    private final AuthorDao authorDao = AuthorDao.getInstance();
    private final GenreDao genreDao = GenreDao.getInstance();

    public BookDto mapToBookDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setPublicationDate(book.getPublicationDate());
        dto.setAuthorId(book.getAuthor().getId());

        Set<Long> genreIdsSet = new HashSet<>();
        for (Genre genre : book.getGenres()) {
            genreIdsSet.add(genre.getId());
        }
        dto.setGenreIds(genreIdsSet);

        return dto;
    }

    public Book mapToBookEntity(BookDto dto) {
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setPublicationDate(dto.getPublicationDate());

        Author author = authorDao.findById(dto.getAuthorId()).orElse(null);
        book.setAuthor(author);

        Set<Genre> genreSet = new HashSet<>();
        for (Long genreId : dto.getGenreIds()) {
            var genre = genreDao.findById(genreId).orElse(null);
            genreSet.add(genre);
        }

        book.setGenres(genreSet);

        return book;
    }
}
