package org.example.service;

import org.example.dao.BookDao;
import org.example.dto.BookDto;
import org.example.entity.Book;
import org.example.mapper.BookMapper;

import java.util.Optional;

public class BookService {

    private static final BookService INSTANCE = new BookService();
    public static BookService getInstance() {
        return INSTANCE;
    }


    private final BookMapper mapper = BookMapper.getInstance();
    private final BookDao dao = BookDao.getInstance();


    public BookDto create(BookDto bookDto) {
        return mapper.mapToBookDto(dao.save(mapper.mapToBookEntity(bookDto)));

    }

    public BookDto read(long id) {
        return dao.findById(id).map(mapper::mapToBookDto).orElse(null);
    }

    public BookDto update(long id, BookDto newDto) {
        return dao.findById(id)
                .map(entity -> {
                    entity.setTitle(newDto.getTitle());
                    entity.setPublicationDate(newDto.getPublicationDate());
                    entity.setAuthor(mapper.mapToBookEntity(newDto).getAuthor());
                    dao.update(entity);
                    return entity;
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
