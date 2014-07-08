package springcourse.solutions.exercise9.controller;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import springcourse.solutions.exercise9.controller.MemberController;
import springcourse.solutions.exercise9.model.Book;
import springcourse.solutions.exercise9.model.Member;
import springcourse.solutions.exercise9.service.api.ILibrary;

import java.util.ArrayList;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Amit Tal
 * @since 4/13/2014
 */
public class MemberControllerTest {

    @InjectMocks
    private MemberController memberController;

    @Mock
    private ILibrary library;

    private MockMvc mockMvc;

    private Gson gson;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
        this.gson = new Gson();
    }

    @Test
    public void testGetAllMembers() throws Exception {
        when(library.getAllMembers()).thenReturn(new ArrayList<Member>());
        this.mockMvc.perform(get("/members")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$").isArray()).
                andDo(print());
        verify(library).getAllMembers();
    }

    @Test
    public void testCreateMembership() throws Exception {
        Member member = new Member("Amit", "Tal", "amitt@sc101.com");
        String json = gson.toJson(member);
        when(library.createMembership(member)).thenReturn(member);
        this.mockMvc.perform(post("/members").
                content(json).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andExpect(jsonPath("$.memberId").exists()).
                andExpect(jsonPath("$.loanedBooks", hasSize(0))).
                andDo(print());
        verify(library).createMembership(member);
    }

    @Test
    public void testCreateInvalidMembership() throws Exception {
        Member member = new Member("Amit", "Tal", "invalidEmailAddress");
        String json = gson.toJson(member);
        when(library.createMembership(member)).thenReturn(member);
        this.mockMvc.perform(post("/members").
                content(json).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest()).
                andDo(print());
        verify(library, never()).createMembership(member);
    }

    @Test
    public void testLoanBook() throws Exception {
        String memberId = "1";
        String catalogId = "2";
        when(library.loanBook(eq(catalogId), eq(memberId))).thenReturn(new Book("stam", "book"));
        this.mockMvc.perform(post("/members/{memberId}/loanedBooks", memberId).
                content(catalogId)).
                andExpect(status().isCreated()).
                andDo(print());
        verify(library).loanBook(eq(catalogId), eq(memberId));
    }

    @Test
    public void testReadAllLoanedBooks() throws Exception {
        String memberId = "1";
        when(library.getLoanedBooks(eq(memberId))).thenReturn(new ArrayList<Book>());
        this.mockMvc.perform(get("/members/{memberId}/loanedBooks", memberId)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$").isArray()).
                andDo(print());
        verify(library).getLoanedBooks(eq(memberId));
    }

    @Test
    public void testReturnBooks() throws Exception {
        String memberId = "1";
        String catalogId = "2";
        this.mockMvc.perform(delete("/members/{memberId}/loanedBooks/{catalogId}", memberId, catalogId)).
                andExpect(status().isNoContent()).
                andDo(print());
        verify(library).returnBook(eq(catalogId), eq(memberId));
    }
}
