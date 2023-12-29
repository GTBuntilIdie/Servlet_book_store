package org.example.mapper;

import org.example.dto.AuthorDto;
import org.example.dto.BookDto;
import org.example.dto.GenreDto;
import org.example.entity.Author;
import org.example.entity.Book;
import org.example.entity.Genre;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookMapperTest {

    Author author = new Author(1L, "name", "surname");
    LocalDate date = LocalDate.now();

    GenreDto genreDto1 = new GenreDto();
    GenreDto genreDto2 = new GenreDto();
    Genre genre1 = new Genre(1l, "genre1");
    Genre genre2 = new Genre(2l, "genre2");
    Set<Genre> genres = new HashSet<>();
    Set<GenreDto> genreDtos = new HashSet<>();

    @Test
    public void testMapToBookDto() {
        genreDto1.setId(1L);
        genreDto2.setId(2L);

        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);

        Set<GenreDto> expSet = new HashSet<>();
        expSet.add(genreDto1);
        expSet.add(genreDto2);

        genres.add(genre1);
        genres.add(genre2);

        genreDtos.add(genreDto1);
        genreDtos.add(genreDto2);

        Book book = mock(Book.class);
        when(book.getId()).thenReturn(1L);
        when(book.getTitle()).thenReturn("Test Book");
        when(book.getAuthor()).thenReturn(author);
        when(book.getPublicationDate()).thenReturn(date);
        when(book.getGenres()).thenReturn(genres);

        BookDto dto = BookMapper.toDto(book);

        assertEquals("Test Book", dto.getTitle());
        assertEquals(1L, authorDto.getId());
        assertEquals(dto.getGenres(), genreDtos);

    }

    @Test
    public void testMapToBookEntity() {

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Test dto");
        bookDto.setPublicationDate(date);
        /*bookDto.setAuthorId(author.getId());*/

        /*genreDtos.add(genre1);
        genreDtos.add(genre2.getId());
        bookDto.setGenreIds(genreDtos);*/

       /* var result = mapper.toEntity(bookDto);

        assertEquals(bookDto.getTitle(), result.getTitle());
        assertEquals(bookDto.getPublicationDate(), result.getPublicationDate());
*/    }

}