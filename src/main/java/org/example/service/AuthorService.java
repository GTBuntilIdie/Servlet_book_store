package org.example.service;

import org.example.dao.AuthorDao;
import org.example.dto.AuthorDto;
import org.example.entity.Author;
import org.example.mapper.AuthorMapper;

public class AuthorService {

    private static final AuthorService INSTANCE = new AuthorService();
    public static AuthorService getInstance() {
        return INSTANCE;
    }


    private final AuthorMapper mapper = AuthorMapper.getInstance();
    private final AuthorDao dao = AuthorDao.getInstance();


    public AuthorDto create(AuthorDto authorDto) {
        Author author = mapper.mapToAuthorEntity(authorDto);
        Author saved = dao.save(author);
        return mapper.mapToAuthorDto(saved);
    }

    public AuthorDto read(long id) {
        return dao.findById(id).map(mapper::mapToAuthorDto).orElse(null);
    }

    public AuthorDto update(long id, AuthorDto authorDto) {
        return dao.findById(id)
                .map(entity -> {
                    entity.setName(authorDto.getName());
                    entity.setSurname(authorDto.getSurname());
                    dao.update(entity);
                    return entity;
                })
                .map(mapper::mapToAuthorDto)
                .orElse(null);
    }

    public AuthorDto delete(long id) {
        return dao.findById(id)
                .map(entity -> {
                    dao.delete(id);
                    return entity;
                }).map(mapper::mapToAuthorDto)
                .orElse(null);
    }
}
