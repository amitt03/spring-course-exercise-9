package springcourse.solutions.exercise9.controller.remote;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import springcourse.solutions.exercise9.model.Book;
import springcourse.solutions.exercise9.model.Member;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Amit Tal
 * @since 4/22/2014
 */
public class MemberControllerRemoteTest {

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

        // Read initial members
        logger.info("Read all members");
        Collection<Member> members = readAllMembers();
        Assert.assertNotNull("No members in library", members);
        int numOfInitialMembers = members.size();

        // Create a new membership
        logger.info("Create membership");
        Member newMember = createMembership();
        Assert.assertNotNull("Member is null", newMember);
        Assert.assertNotNull("Member id is null", newMember.getMemberId());

        // Validate member has no loaned books
        Collection<Book> loanedBooks = readLoanedBooks(newMember.getMemberId());
        Assert.assertTrue(loanedBooks == null || loanedBooks.isEmpty());

        // Verify member was added
        logger.info("Read all members 2");
        Collection<Member> members2 = readAllMembers();
        Assert.assertNotNull("No members in library", members2);
        Assert.assertEquals("Expected number of members to grow by one", numOfInitialMembers + 1, members2.size());

        // Read all library book
        logger.info("Read all library books");
        Collection<Book> allBooks = readAllLibraryBooks();
        Assert.assertTrue("No books in library", allBooks != null && !allBooks.isEmpty());
        Book aBook = allBooks.iterator().next();

        // Loan book
        logger.info("Loan book {}", aBook);
        Book loanedBook = loanBook(newMember.getMemberId(), aBook.getCatalogId());
        Assert.assertNotNull("Loan book returned null", loanedBook);
        Assert.assertEquals("Wrong book was loaned", aBook, loanedBook);

        // Validate member has one loaned books
        logger.info("Read member {} loaned books", newMember.getMemberId());
        Collection<Book> loanedBooks2 = readLoanedBooks(newMember.getMemberId());
        Assert.assertNotNull("Expected one loaned book", loanedBooks2);
        Assert.assertEquals("Expected one loaned book", 1, loanedBooks2.size());

        // Return book
        logger.info("Return loaned book");
        returnBook(newMember.getMemberId(), aBook.getCatalogId());

        // Validate member has no loaned books
        logger.info("Read member {} loaned books (again)", newMember.getMemberId());
        Collection<Book> loanedBooks3 = readLoanedBooks(newMember.getMemberId());
        Assert.assertTrue("Expected no loaned book", loanedBooks3 == null || loanedBooks3.isEmpty());
    }

    @Test
    //@Ignore
    public void testLoanSameBookByTwoMembers() {

        // Create two memberships
        logger.info("Create membership 1");
        Member newMember1 = createMembership();
        logger.info("Create membership 2");
        Member newMember2 = createMembership();

        // Read all library book
        logger.info("Read all library books");
        Collection<Book> allBooks = readAllLibraryBooks();
        Book aBook = allBooks.iterator().next();

        // Member 1 loans a book
        logger.info("Loan book {} by member {}", aBook, newMember1.getMemberId());
        loanBook(newMember1.getMemberId(), aBook.getCatalogId());

        // Member 2 tries to loan the same book but should fail since member 1 has not returned the book yet
        try {
            logger.info("(try) Loan book {} by member {}", aBook, newMember2.getMemberId());
            Book loanedBook2 = loanBook(newMember2.getMemberId(), aBook.getCatalogId());
            Assert.fail("Expected load book by second member to fail since it is already loaned by first member");
        } catch (Exception ex) { // TODO Refine exception
            logger.info("Loan book has failed as expected: {}", ex.getMessage());
        }

        // Return book
        logger.info("Return loaned book");
        returnBook(newMember1.getMemberId(), aBook.getCatalogId());

        // Member 2 loans the book and returns it
        logger.info("Loan book {} by member {}", aBook, newMember2.getMemberId());
        Book loanedBook = loanBook(newMember2.getMemberId(), aBook.getCatalogId());
        returnBook(newMember2.getMemberId(), aBook.getCatalogId());
    }

    private Collection<Member> readAllMembers() {
        Member[] response = restTemplate.getForObject(baseUrl + "/members", Member[].class);
        return Arrays.asList(response);
    }

    private Member createMembership() {
        Member member = new Member("John", "Doe", "john@doe.com");
        Member response = restTemplate.postForObject(baseUrl + "/members", member, Member.class);
        return response;
    }

    private Collection<Book> readLoanedBooks(String memberId) {
        Book[] response = restTemplate.getForObject(baseUrl + "/members/{memberId}/loanedBooks", Book[].class, memberId);
        return Arrays.asList(response);
    }

    private Book loanBook(String memberId, String catalogId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        String json = new Gson().toJson(catalogId);
        HttpEntity<String> request = new HttpEntity<String>(json, httpHeaders);
        final ResponseEntity<Book> response = restTemplate.exchange(baseUrl + "/members/{memberId}/loanedBooks", HttpMethod.POST, request, Book.class, memberId);
        return response.getBody();
    }

    private void returnBook(String memberId, String catalogId) {
        restTemplate.delete(baseUrl + "/members/{memberId}/loanedBooks/{catalogId}", memberId, catalogId);
    }

    private Collection<Book> readAllLibraryBooks() {
        Book[] response = restTemplate.getForObject(baseUrl + "/books", Book[].class);
        return Arrays.asList(response);
    }
}
