package springcourse.solutions.exercise9.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoAnnotatedContextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springcourse.solutions.exercise9.dao.api.IBookDao;
import springcourse.solutions.exercise9.dao.api.IMemberDao;
import springcourse.solutions.exercise9.model.Book;
import springcourse.solutions.exercise9.model.Genre;
import springcourse.solutions.exercise9.service.impl.Library;
import springcourse.solutions.exercise9.util.BookAnalyzer;

import java.util.Collection;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringockitoAnnotatedContextLoader.class)
public class LibraryTest {

    public static final Genre EXPECTED_GENRE = Genre.NA;
    public static final String MEMBER_ID = "123";

    @Configuration
    @Import(Library.class)
    static class MyConfig {}

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    @ReplaceWithMock
    private IBookDao bookDao;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    @ReplaceWithMock
    private IMemberDao memberDao;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    @ReplaceWithMock
    private BookAnalyzer bookAnalyzer;

    @Autowired
    private Library uut;

    @Test
    public void testAddNewBook() throws Exception {
        // set up mock fixture
        Book argument = mock(Book.class);
        Book daoResult = mock(Book.class);
        when(bookAnalyzer.analyzeBook(same(argument))).thenReturn(EXPECTED_GENRE);
        when(bookDao.addBook(same(argument))).thenReturn(daoResult);
        // run test case
        Book methodResult = uut.addNewBook(argument);
        // verify result
        Assert.assertSame("not the same book returned", daoResult, methodResult);
        // verify method side-effects
        verify(argument).setGenre(Genre.NA);
    }

    @Test
    public void testGetLoanedBooksMemberExists() throws Exception {
        // set up mock fixture
        Collection<Book> books = mock(Collection.class);
        when(memberDao.memberExist(same(MEMBER_ID))).thenReturn(true);
        when(bookDao.getLoanedBooks(same(MEMBER_ID))).thenReturn(books);
        // run test case
        Collection<Book> result = uut.getLoanedBooks(MEMBER_ID);
        // verify result
        Assert.assertSame("not the same books collection returned", books, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLoanedBooksMemberDoesNotExist() throws Exception {
        // set up mock fixture
        when(memberDao.memberExist(eq(MEMBER_ID))).thenReturn(false);
        // run test case
        uut.getLoanedBooks(MEMBER_ID);
    }
}