package springcourse.solutions.exercise9.controller.remote;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import springcourse.solutions.exercise9.model.Book;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Amit Tal
 * @since 4/13/2014
 */
public class LibraryControllerRemoteTest {

    private Logger logger = LoggerFactory.getLogger(LibraryControllerRemoteTest.class);

    private final String baseUrl = "http://localhost:8080/";

    private RestTemplate restTemplate;


    @Before
    public void setup() {
        this.restTemplate = new RestTemplate();
    }

    @Test
    //@Ignore
    public void testHappyFlow() {

        // Read initial library books
        logger.info("Read all books");
        Collection<Book> initialLibraryBooks = readAllLibraryBooks();
        Assert.assertNotNull("No books in library", initialLibraryBooks);
        int numOfInitialBooks = initialLibraryBooks.size();

        // Create book
        logger.info("Create book");
        Book newBook = createBook();
        Assert.assertNotNull("Book is null", newBook);
        Assert.assertNotNull("Book catalogId is null", newBook.getCatalogId());

        // Read (again) all books and verify new book exists
        logger.info("Read all books 2");
        Collection<Book> allLibraryBooks2 = readAllLibraryBooks();
        Assert.assertNotNull("No books in library", allLibraryBooks2);
        Assert.assertEquals("Expected number of books to grow by one", numOfInitialBooks + 1, allLibraryBooks2.size());
        assertBookExists(allLibraryBooks2, newBook);

        // Read books by author of new book
        logger.info("Read books by author: {}", newBook.getAuthor());
        Collection<Book> booksByAuthor = readBooksByAuthor(newBook.getAuthor());
        Assert.assertNotNull("No books found by author", booksByAuthor);
        Assert.assertTrue("Expected at least one book by author", booksByAuthor.size() >= 1);
        assertBookExists(booksByAuthor, newBook);

        // Delete book
        logger.info("Delete book: {}", newBook.getCatalogId());
        deleteBook(newBook.getCatalogId());

        // Read (again) all books and verify new book does not exists
        logger.info("Read all books 3");
        Collection<Book> allLibraryBooks3 = readAllLibraryBooks();
        Assert.assertNotNull(allLibraryBooks3);
        Assert.assertEquals("Expected number of books to grow by one", numOfInitialBooks, allLibraryBooks3.size());
        assertBookNotExists(allLibraryBooks3, newBook);
    }

    @Test
    //@Ignore
    public void testCreateInvalidBook() {
        try {
            Book book = new Book("This book name is too long! It should actually be less then 50 characters but it is not!", "So what?");
            restTemplate.postForObject(baseUrl + "/books", book, Book.class);
        } catch (HttpClientErrorException ex) {
            HttpStatus statusCode = ex.getStatusCode();
            Assert.assertEquals(HttpStatus.BAD_REQUEST, statusCode);
        }
    }

    private Collection<Book> readAllLibraryBooks() {
        Book[] response = restTemplate.getForObject(baseUrl + "/books", Book[].class);
        return Arrays.asList(response);
    }

    private Book createBook() {
        Book book = new Book("Spring in action 3rd edition", "Craig Walls");
        Book response = restTemplate.postForObject(baseUrl + "/books", book, Book.class);
        return response;
    }

    private Collection<Book> readBooksByAuthor(String author) {
        Book[] response = restTemplate.getForObject(baseUrl + "/books?author=" + author, Book[].class);
        return Arrays.asList(response);
    }

    private void deleteBook(String catalogId) {
        restTemplate.delete(baseUrl + "/books/{catalogId}", catalogId);
    }


    private void assertBookExists(Collection<Book> books, Book book) {
        assertBookExistence(books, book, true);
    }

    private void assertBookNotExists(Collection<Book> books, Book book) {
        assertBookExistence(books, book, false);
    }

    private void assertBookExistence(Collection<Book> books, Book book, boolean assertBookExists) {
        Assert.assertNotNull("No books exist", books);
        Assert.assertFalse("No books exist", books.isEmpty());
        Assert.assertNotNull("Book is null",  book);
        Assert.assertNotNull("Book catalog is missing",  book.getCatalogId());
        boolean bookFound = false;
        for (Book b : books) {
            if (b.equals(book)) {
                bookFound = true;
                break;
            }
        }
        if (assertBookExists) {
            Assert.assertTrue(bookFound);
        } else {
            Assert.assertFalse(bookFound);
        }
    }
}

