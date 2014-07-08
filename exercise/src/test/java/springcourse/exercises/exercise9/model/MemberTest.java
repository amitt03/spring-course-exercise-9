package springcourse.exercises.exercise9.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * @author amira
 * @since 4/17/2014
 */
public class MemberTest {
    private static final Logger log = LoggerFactory.getLogger(MemberTest.class);
    public static final String FIRST_NAME = "Christopher";
    public static final String LAST_NAME = "Walken";
    public static final String MEMBER_ID = UUID.randomUUID().toString();
    public static final String EMAIL = "walken20@mail.imdb.com";
    public static final Collection<Book> BOOKS = Collections.unmodifiableList(Arrays.asList(new Book("A Tangled Tale", "Lewis Carroll")));

    private Member uut;

    @Before
    public void setUp() throws Exception {
        uut = new Member();
    }

    @Test
    public void testArgumentCtor() throws Exception {
        uut.setFirstName(FIRST_NAME);
        uut.setLastName(LAST_NAME);
        uut.setMemberId(MEMBER_ID);
        uut.setEmail(EMAIL);
        Member otheruut = new Member(FIRST_NAME, LAST_NAME, MEMBER_ID, EMAIL);
        Assert.assertEquals("member is different built using arguments constructor than using setters", uut, otheruut);
    }

    @Test
    public void testNullMemberId() throws Exception {
        Assert.assertNotNull(uut.getMemberId());
        Member otheruut = new Member(FIRST_NAME, LAST_NAME, null, EMAIL);
        Assert.assertNotNull(otheruut.getMemberId());
        uut.setMemberId(null);
        Assert.assertNull(uut.getMemberId());
    }

    @Test
    public void testGetSetFirstName() throws Exception {
        uut.setFirstName(FIRST_NAME);
        Assert.assertEquals(FIRST_NAME, uut.getFirstName());
    }

    @Test
    public void testGetSetLastName() throws Exception {
        uut.setLastName(LAST_NAME);
        Assert.assertEquals(LAST_NAME, uut.getLastName());
    }

    @Test
    public void testGetSetBooks() throws Exception {
        uut.setLoanedBooks(BOOKS);
        Assert.assertEquals(BOOKS, uut.getLoanedBooks());
    }

    @Test
    public void testGetSetMemberId() throws Exception {
        uut.setMemberId(MEMBER_ID);
        Assert.assertEquals(MEMBER_ID, uut.getMemberId());
    }

    @Test
    public void testGetSetEmail() throws Exception {
        uut.setEmail(EMAIL);
        Assert.assertEquals(EMAIL, uut.getEmail());
    }

}
