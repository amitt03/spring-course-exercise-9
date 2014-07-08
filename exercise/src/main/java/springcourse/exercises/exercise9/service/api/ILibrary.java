package springcourse.exercises.exercise9.service.api;

import springcourse.exercises.exercise9.model.Book;
import springcourse.exercises.exercise9.model.Member;

import java.util.Collection;

/**
 * @author Amit Tal
 * @since 4/6/14
 */
public interface ILibrary {


    Collection<Book> getAllBooks();

    Book addNewBook(Book newBook);

    void removeBook(String catalogId);

    Collection<Book> searchBooksByAuthor(String author);


    Member createMembership(Member member);

    Collection<Member> getAllMembers();

    Book loanBook(String catalogId, String memberId);

    Collection<Book> getLoanedBooks(String memberId);

    void returnBook(String catalogId, String memberId);
}
