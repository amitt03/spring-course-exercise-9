package springcourse.exercises.exercise9.controller;

import com.wordnik.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springcourse.exercises.exercise9.model.Book;
import springcourse.exercises.exercise9.service.api.ILibrary;

import javax.validation.Valid;
import java.util.Collection;

/**
 * @author Amit Tal
 * @since 4/6/14
 */
@RestController
@RequestMapping(value = "/books", produces = "application/json")
@Api(value="/books", description = "Library books resource")
public class LibraryController {

    private Logger logger = LoggerFactory.getLogger(LibraryController.class);

    private ILibrary library;

    @Autowired
    public void setLibrary(ILibrary library) {
        this.library = library;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add a new book to the library", response = Book.class, httpMethod = "POST", position = 3)
    public Book createBook(@Valid
                           @ApiParam(value = "Book data")
                           @RequestBody Book book) {
        logger.info("Request to create book {}", book);
        Book result = library.addNewBook(book);
        logger.info("Created book {}", result);
        return result;
    }

    @RequestMapping(value = "/{catalogId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete a book from the library", httpMethod = "DELETE", position = 4)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "The book catalog id is invalid")})
    public void deleteBook(@ApiParam(name = "catalogId", value = "The book catalog id")
                           @PathVariable String catalogId) {
        logger.info("Request to delete book {}", catalogId);
        library.removeBook(catalogId);
        logger.info("Book {} deleted", catalogId);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get all the library books", httpMethod="GET", response = Book.class, position = 1)
    public Collection<Book> readAllBooks() {
        logger.info("Request to read all books");
        Collection<Book> allBooks = library.getAllBooks();
        logger.info("read {} books", (allBooks == null ? 0 : allBooks.size()));
        return allBooks;
    }

    @RequestMapping(method = RequestMethod.GET, params = "author")
    @ApiOperation(value = "Get all books of a specific author", httpMethod="GET", response = Book.class, position = 2)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Author name is invalid")})
    public Collection<Book> readBooksByAuthor(@ApiParam(name = "author", value = "The book author")
                                              @RequestParam String author) {
        logger.info("Request to read all books by author {}", author);
        Collection<Book> books = library.searchBooksByAuthor(author);
        logger.info("read {} books by author {}", (books == null ? 0 : books.size()), author);
        return books;
    }
}
