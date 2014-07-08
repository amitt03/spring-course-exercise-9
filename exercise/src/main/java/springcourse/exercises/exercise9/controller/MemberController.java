package springcourse.exercises.exercise9.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springcourse.exercises.exercise9.model.Book;
import springcourse.exercises.exercise9.model.Member;
import springcourse.exercises.exercise9.service.api.ILibrary;

import javax.validation.Valid;
import java.util.Collection;

/**
 * @author Amit Tal
 * @since 4/10/2014
 */
@RestController
@RequestMapping("/members")
public class MemberController {

    private Logger logger = LoggerFactory.getLogger(MemberController.class);

    private ILibrary library;

    @Autowired
    public void setLibrary(ILibrary library) {
        this.library = library;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Member> getAllMembers() {
        Collection<Member> allMembers = library.getAllMembers();
        return allMembers;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Member createMembership(@Valid
                                 @RequestBody Member member) {
        Member membership = library.createMembership(member);
        return membership;
    }

    @RequestMapping(value = "/{memberId}/loanedBooks", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Book loanBook(@PathVariable String memberId, @RequestBody String catalogId) {
        Book book = library.loanBook(catalogId, memberId);
        return book;
    }

    @RequestMapping(value = "/{memberId}/loanedBooks", method = RequestMethod.GET)
    public Collection<Book> readAllLoanedBooks(@PathVariable String memberId) {
        Collection<Book> loanedBooks = library.getLoanedBooks(memberId);
        return loanedBooks;
    }

    @RequestMapping(value = "/{memberId}/loanedBooks/{catalogId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void returnBook(@PathVariable String memberId, @PathVariable String catalogId) {
        library.returnBook(catalogId, memberId);
    }
}
