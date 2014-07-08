package springcourse.solutions.exercise9.util;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.mock.env.MockPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import springcourse.solutions.exercise9.model.Book;
import springcourse.solutions.exercise9.model.Genre;

@RunWith(SpringJUnit4ClassRunner.class)    // 2a. load spring
@ContextConfiguration                      // 2b. configure application context
public class BookAnalyzerSetupTest {

    @Configuration
    @Import(BookAnalyzer.class)            // 2c. scan only BookAnalyzer
    static class MyConfig {
        @Bean            // 2d. use mockEnvVars as part of this test's fixture
        public PropertySourcesPlaceholderConfigurer configurer() {
            PropertySourcesPlaceholderConfigurer result = new PropertySourcesPlaceholderConfigurer();
            MutablePropertySources propertySources = new MutablePropertySources();
            propertySources.addLast(mockEnvVars);
            result.setPropertySources(propertySources);
            return result;
        }
    }

    @Autowired
    private BookAnalyzer analyzer;

    @Autowired
    private ApplicationContext ac;


    @Value("${analyze.romance}")
    private String[] romanceKeyWord;

    private static MockPropertySource mockEnvVars;

    private static Book book;

    @BeforeClass
    public static void setUp() throws Exception {
        mockEnvVars = new MockPropertySource()
                .withProperty("analyze.romance", "1,2")
                .withProperty("analyze.comedy", "3,4")
                .withProperty("analyze.horror", "5,6")
                .withProperty("analyze.action", "7,8")
                .withProperty("analyze.fiction", "9,A");
        book = new Book("The screwtape letters", "Lewis Carroll");
        book.setDetails("A fictional story about morality");
    }

    @Test
    public void testSpringSetup() throws Exception {
        Assert.notNull(ac, "2a. spring did not load!");
        Assert.notNull(analyzer, "2c. auto wiring failed!");
        org.junit.Assert.assertNotEquals("2c. AppConfig loaded (and scanned books-analyzer.properties)!", "kiss", romanceKeyWord[0]);
        org.junit.Assert.assertNotEquals("2d. no properties placeholder configurator loaded!", "${analyze.romance}", romanceKeyWord[0]);
        org.junit.Assert.assertEquals("2d. mockEnvVars not loaded!", "1", romanceKeyWord[0]);

        org.junit.Assert.assertEquals("the analyzer does not work!", Genre.FICTION, analyzer.analyzeBook(book));
    }
}