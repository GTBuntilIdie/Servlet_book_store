package org.example.service;

import org.example.dao.BookDao;
import org.example.dto.BookDto;
import org.example.entity.Book;
import org.example.mapper.BookMapper;

public class BookService {

    private static final BookService INSTANCE = new BookService();
    public static BookService getInstance() {
        return INSTANCE;
    }


    private final BookMapper mapper = BookMapper.getInstance();
    private final BookDao dao = BookDao.getInstance();


    public BookDto create(BookDto bookDto) {
        Book book = mapper.mapToBookEntity(bookDto);
        dao.addGenre(bookDto);
        Book saved = dao.save(book);
        return mapper.mapToBookDto(saved);
    }

    public BookDto read(long id) {
        return dao.findById(id).map(mapper::mapToBookDto).orElse(null);
    }

    public BookDto update(long id, BookDto bookDtoIn) {
        return dao.findById(id)
                .map(entity -> {
                    var updated = mapper.mapToBookEntity(bookDtoIn);
                    updated.setId(entity.getId());
                    return dao.save(updated);
                })
                .map(mapper::mapToBookDto)
                .orElse(null);
    }

    public BookDto delete(long id) {
        return dao.findById(id)
                .map(entity -> {
                    dao.delete(id);
                    return entity;
                }).map(mapper::mapToBookDto)
                .orElse(null);
    }
}
