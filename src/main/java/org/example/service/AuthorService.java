package org.example.service;

import org.example.connection.ConnectionManager;
import org.example.dao.AuthorDao;
import org.example.dto.AuthorDto;
import org.example.entity.Author;
import org.example.mapper.AuthorMapper;

import java.sql.SQLException;

public class AuthorService {

    private static final AuthorService INSTANCE = new AuthorService();
    public static AuthorService getInstance() {
        return INSTANCE;
    }


    private final AuthorMapper mapper = AuthorMapper.getInstance();
    private final AuthorDao dao = AuthorDao.getInstance();


    public AuthorDto create(AuthorDto authorDto) {
        try (var connection = ConnectionManager.get()) {
            Author author = mapper.mapToAuthorEntity(authorDto);
            Author saved = dao.save(author, connection);
            return mapper.mapToAuthorDto(saved); }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthorDto read(long id) {
        try (var connection = ConnectionManager.get()) {
            return dao.findById(id, connection).map(mapper::mapToAuthorDto).orElse(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthorDto update(long id, AuthorDto authorDto) {
        try (var connection = ConnectionManager.get()) {
            return dao.findById(id, connection)
                .map(entity -> {
                    entity.setName(authorDto.getName());
                    entity.setSurname(authorDto.getSurname());
                    dao.update(entity, connection);
                    return entity; }
                )
                .map(mapper::mapToAuthorDto)
                .orElse(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(long id) {
        try (var connection = ConnectionManager.get()) {
            dao.findById(id, connection)
                .map(entity -> {
                    dao.deleteById(id, connection);
                    return entity;
                }).map(mapper::mapToAuthorDto);
    } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
