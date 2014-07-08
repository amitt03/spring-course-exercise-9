package springcourse.solutions.exercise9.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.jmx.export.annotation.*;
import org.springframework.stereotype.Repository;
import springcourse.solutions.exercise9.dao.api.IBookDao;
import springcourse.solutions.exercise9.model.Book;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Amit Tal
 * @since 3/24/14
 */
@Repository
@Profile("prod")
@ManagedResource
public class BookInMemoryDao implements IBookDao {

    private Logger logger = LoggerFactory.getLogger(BookInMemoryDao.class);

    private ConcurrentHashMap<String, Book> books;
    private ConcurrentHashMap<String, String> loanedBooks;

    protected String dataStoreName;

    public BookInMemoryDao() {
        books = new ConcurrentHashMap<String, Book>();
        loanedBooks = new ConcurrentHashMap<String, String>();
        dataStoreName = "BookInMemoryDao";
    }

    @Override
    public boolean checkConnection() {
        logger.info("The connection is stable");
        return true;
    }

    @Override
    public Book addBook(Book book) {
        if (book.getGenre() == null) {
            throw new IllegalArgumentException("Genre cannot be null");
        }
        books.put(book.getCatalogId(), book);
        logger.info("Added book: {}", book);
        return book;
    }

    @Override
    @ManagedOperation
    @ManagedOperationParameters(@ManagedOperationParameter(name = "catalogId", description = "Remove book"))
    public void removeBook(String catalogId) {
        Book book = books.remove(catalogId);
        logger.info("Removed book: {}", book);
        if (book == null) {
            throw new IllegalArgumentException("Cannot remove book " + catalogId + " since it does not exist");
        }
    }

    @Override
    public Collection<Book> getAllBooks() {
        return books.values();
    }

    @Override
    public Book loanBook(String catalogId, String memberId) {
        Book book = books.get(catalogId);
        if (book == null) {
            throw new IllegalArgumentException("Book " + catalogId + " does not exist");
        }
        if (loanedBooks.containsKey(catalogId)) {
            throw new IllegalStateException("Book already loaned");
        }
        loanedBooks.put(catalogId, memberId);
        logger.info("member {} has loaned book {}", memberId, book);
        return book;
    }

    @Override
    @ManagedOperation
    @ManagedOperationParameters(@ManagedOperationParameter(name = "catalogId", description = "Return book"))
    public String returnBook(String catalogId) {
        String memberId = loanedBooks.remove(catalogId);
        logger.info("member {} has returned book {}", memberId, catalogId);
        return memberId;
    }

    @Override
    public Collection<Book> getLoanedBooks(String memberId) {
        Collection<Book> myBooks = new ArrayList<Book>();
        for (Map.Entry<String, String> entry : loanedBooks.entrySet()) {
            if (entry.getValue().equals(memberId)) {
                myBooks.add(books.get(entry.getKey()));
            }
        }
        return myBooks;
    }

    @ManagedOperation
    public int getBooksInventoryCount() {
        return books.size();
    }

    @ManagedOperation
    public int getLoanedBooksInventoryCount() {
        return loanedBooks.size();
    }

    @ManagedAttribute
    public String getDataStoreName() {
        return dataStoreName;
    }

    @ManagedAttribute
    public void setDataStoreName(String dataStoreName) {
        this.dataStoreName = dataStoreName;
    }
}
