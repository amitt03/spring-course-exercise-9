package springcourse.solutions.exercise9.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springcourse.solutions.exercise9.dao.api.IBookDao;
import springcourse.solutions.exercise9.dao.api.IMemberDao;
import springcourse.solutions.exercise9.model.Book;
import springcourse.solutions.exercise9.model.Genre;
import springcourse.solutions.exercise9.model.Member;
import springcourse.solutions.exercise9.service.api.ILibrary;
import springcourse.solutions.exercise9.util.BookAnalyzer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Amit Tal
 * @since 3/24/14
 */
@Service
public class Library implements ILibrary {

    private Logger logger = LoggerFactory.getLogger(Library.class);

    private IBookDao bookDao;
    private IMemberDao memberDao;
    private BookAnalyzer bookAnalyzer;

    @Autowired
    public Library(IBookDao bookDao, IMemberDao memberDao, BookAnalyzer bookAnalyzer) {
        this.bookDao = bookDao;
        this.memberDao = memberDao;
        this.bookAnalyzer = bookAnalyzer;
    }

    @PostConstruct
    public void open() {
        if (bookDao.checkConnection()) {
            logger.info("The library is now open");
        } else {
            logger.info("The library is temporarily unavailable");
        }
    }

    @Override
    public Member createMembership(Member member) {
        Member dbMember = memberDao.createMember(member);
        return dbMember;
    }

    @Override
    public Collection<Member> getAllMembers() {
        Collection<Member> allMembers = memberDao.getAllMembers();
        if (allMembers != null) {
            for (Member member : allMembers) {
                Collection<Book> loanedBooks = bookDao.getLoanedBooks(member.getMemberId());
                member.setLoanedBooks(loanedBooks);
            }
        }
        return allMembers;
    }

    @Override
    public Book addNewBook(Book newBook) {
        Genre genre = bookAnalyzer.analyzeBook(newBook);
        newBook.setGenre(genre);
        Book book = bookDao.addBook(newBook);
        return book;
    }

    @Override
    public void removeBook(String catalogId) {
        bookDao.removeBook(catalogId);
    }

    @Override
    public Collection<Book> getAllBooks() {
        return bookDao.getAllBooks();
    }

    @Override
    public Collection<Book> searchBooksByAuthor(String author) {
        Collection<Book> allBooks = getAllBooks();
        Collection<Book> booksByAuthor = new ArrayList<Book>();
        for (Book book : allBooks) {
            if (author.equals(book.getAuthor())) {
                booksByAuthor.add(book);
            }
        }
        return booksByAuthor;
    }

    @Override
    public Book loanBook(String catalogId, String memberId) {
        if (memberDao.memberExist(memberId)) {
            Book book = bookDao.loanBook(catalogId, memberId);
            return book;
        } else {
            throw new IllegalArgumentException("Member " + memberId + " is not a member of this library");
        }
    }

    @Override
    public Collection<Book> getLoanedBooks(String memberId) {
        if (memberDao.memberExist(memberId)) {
            Collection<Book> loanedBooks = bookDao.getLoanedBooks(memberId);
            return loanedBooks;
        } else {
            throw new IllegalArgumentException("Member " + memberId + " is not a member of this library");
        }
    }

    @Override
    public void returnBook(String catalogId, String memberId) {
        String returnedMemberId = bookDao.returnBook(catalogId);
        if (!memberId.equals(returnedMemberId)) {
            logger.warn("Returned book {} was loaned by {} BUT returned by {}", catalogId, memberId, returnedMemberId);
        }
    }

    @PreDestroy
    public void close() {
        logger.info("The library is now closed");
    }

}