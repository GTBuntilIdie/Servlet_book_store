package org.example.mapper;

import org.example.dao.AuthorDao;
import org.example.dao.GenreDao;
import org.example.dto.BookDto;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.entity.Genre;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookMapperTest {

    @InjectMocks
    private BookMapper bookMapper;
    @Mock
    private AuthorDao authorDao = AuthorDao.getInstance();
    @Mock
    private Connection connection;
    @Mock
    private GenreDao genreDao = GenreDao.getInstance();

    BookMapper mapper = BookMapper.getInstance();
    Author author = new Author(1L, "name", "surname");
    LocalDate date = LocalDate.now();

    Genre genre1 = new Genre(1l, "genre1");
    Genre genre2 = new Genre(2l, "genre2");
    Set<Genre> genres = new HashSet<>();
    Set<Long> genresIds = new HashSet<>();


    @Test
    public void testMapToBookDto() {
        genres.add(genre1);
        genres.add(genre2);

        Book book = mock(Book.class);
        when(book.getId()).thenReturn(1L);
        when(book.getTitle()).thenReturn("Test Book");
        when(book.getAuthor()).thenReturn(author);
        when(book.getPublicationDate()).thenReturn(date);
        when(book.getGenres()).thenReturn(genres);

        BookDto dto = mapper.mapToBookDto(book);

        assertEquals(1L, dto.getId());
        assertEquals("Test Book", dto.getTitle());
    }

    @Test
    public void testMapToBookEntity() {
        Mockito.when(authorDao.findById(1L,connection)).thenReturn(Optional.ofNullable(author));
        Mockito.when(genreDao.findById(1L,connection)).thenReturn(Optional.ofNullable(genre1));
        Mockito.when(genreDao.findById(2L,connection)).thenReturn(Optional.ofNullable(genre2));

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Test dto");
        bookDto.setPublicationDate(date);
        bookDto.setAuthorId(author.getId());

        genresIds.add(genre1.getId());
        genresIds.add(genre2.getId());
        bookDto.setGenreIds(genresIds);

        var result = mapper.mapToBookEntity(bookDto, connection);

        // Assert
        assertEquals(bookDto.getId(), result.getId());
        assertEquals(bookDto.getTitle(), result.getTitle());
        assertEquals(bookDto.getPublicationDate(), result.getPublicationDate());
        assertEquals(author, result.getAuthor());
    }

}