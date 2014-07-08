package springcourse.exercises.exercise9.controller;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import springcourse.exercises.exercise9.application.AppConfig;
import springcourse.exercises.exercise9.controller.config.ControllerConfig;
import springcourse.exercises.exercise9.controller.exception.Error;
import springcourse.exercises.exercise9.model.Book;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Amit Tal
 * @since 4/13/2014
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {AppConfig.class, ControllerConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LibraryControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private Gson gson;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        this.gson = new Gson();
    }

    @Test
    public void testCreateBook() throws Exception {
        String bookJson = "{\"title\":\"Pro Spring 3\",\"author\":\"Clarence Ho\"}";
        this.mockMvc.perform(post("/books").
                content(bookJson).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andDo(print());
    }

    @Test
    public void testDeleteBook() throws Exception {
        this.mockMvc.perform(delete("/books/{catalogId}", "dummy-catalog-id")).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.code").value(Error.Code.INVALID_INPUT.toString())).
                andDo(print());
    }

    @Test
    public void testReadAllBooks() throws Exception {
        this.mockMvc.perform(get("/books")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$").isArray()).
                andExpect(jsonPath("$", hasSize(6))).
                andDo(print()).
                andReturn();
    }

    @Test
    public void testReadBooksByAuthor() throws Exception {
        this.mockMvc.perform(get("/books").param("author", "J. R. R. Tolkien")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$").isArray()).
                andExpect(jsonPath("$", hasSize(4))).
                andDo(print()).
                andReturn();
    }

    @Test
    public void testCreateInvalidBook() throws Exception {
        Book book = new Book("This book name is too long! It should actually be less then 50 characters but it is not!", "So what?");
        String bookJson = gson.toJson(book);
        this.mockMvc.perform(post("/books").
                content(bookJson).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().is5xxServerError()).
                andDo(print());
    }

    @Test
    public void testBookNameChangedToTitle() throws Exception {
        String bookJson = "{\"title\":\"The Jackson Cookbook\",\"author\":\"Ted M. Young\"}";
        this.mockMvc.perform(post("/books").
                content(bookJson).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated()).
                andExpect(jsonPath("$.title").value("The Jackson Cookbook")).
                andDo(print());
    }
}
