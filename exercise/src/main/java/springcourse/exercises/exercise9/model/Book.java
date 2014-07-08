package springcourse.exercises.exercise9.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author Amit Tal
 * @since 3/24/14
 */
public class Book implements Serializable {

    private String name;
    private String author;
    private String details;
    private String catalogId;
    private Genre genre;

    protected Book() {
        this.catalogId = UUID.randomUUID().toString();
    }

    public Book(String name, String author) {
        this.name = name;
        this.author = author;
        this.catalogId = UUID.randomUUID().toString();
    }

    public Book(String name, String author, String catalogId) {
        this.name = name;
        this.author = author;
        if (catalogId != null) {
            this.catalogId = catalogId;
        } else {
            this.catalogId = UUID.randomUUID().toString();
        }
    }

    @NotNull
    @Size(max = 50)
    @JsonProperty("title")
    @ApiModelProperty(required = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @Size(max = 50)
    @ApiModelProperty(required = true)
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @ApiModelProperty(required = true)
    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (author != null ? !author.equals(book.author) : book.author != null) return false;
        if (catalogId != null ? !catalogId.equals(book.catalogId) : book.catalogId != null) return false;
        if (details != null ? !details.equals(book.details) : book.details != null) return false;
        if (genre != book.genre) return false;
        if (name != null ? !name.equals(book.name) : book.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (details != null ? details.hashCode() : 0);
        result = 31 * result + (catalogId != null ? catalogId.hashCode() : 0);
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Book{");
        sb.append("name='").append(name).append('\'');
        sb.append(", author='").append(author).append('\'');
        sb.append(", details='").append(details).append('\'');
        sb.append(", catalogId='").append(catalogId).append('\'');
        sb.append(", genre=").append(genre);
        sb.append('}');
        return sb.toString();
    }
}
