package org.example.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Set;

public class BookDto {

    private Long id;
    @NotBlank
    @Size(max = 20)
    private String title;

    @NotNull
    @Past
    private LocalDate publicationDate;

    @NotBlank
    @Positive
    private long authorId;

    @NotEmpty
    private Set<@Positive Long> genreIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public Set<Long> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(Set<Long> genreIds) {
        this.genreIds = genreIds;
    }
}
