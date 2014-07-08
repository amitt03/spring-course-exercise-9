package springcourse.exercises.exercise9.controller;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import springcourse.exercises.exercise9.application.AppConfig;
import springcourse.exercises.exercise9.controller.config.ControllerConfig;
import springcourse.exercises.exercise9.dao.api.IBookDao;
import springcourse.exercises.exercise9.dao.impl.BookUsuallyInMemoryDao2;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class LibraryControllerTest2 {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private IBookDao bookDao;

    private MockMvc mockMvc;

    private Gson gson;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        this.gson = new Gson();
        Assert.assertTrue("Expected book dao to be of type BookUsuallyInMemoryDao2", bookDao instanceof BookUsuallyInMemoryDao2);
        BookUsuallyInMemoryDao2 dao = (BookUsuallyInMemoryDao2)bookDao;
        dao.setDecisionMakingOn(false);
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
}
