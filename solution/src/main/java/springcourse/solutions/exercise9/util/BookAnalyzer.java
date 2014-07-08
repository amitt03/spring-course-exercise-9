package springcourse.solutions.exercise9.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import springcourse.solutions.exercise9.model.Book;
import springcourse.solutions.exercise9.model.Genre;

import javax.annotation.PostConstruct;

/**
 * @author Amit Tal
 * @since 3/24/14
 */
@Component
public class BookAnalyzer {

    @Value("${analyze.romance}")
    private String[] romanceKeyWord;

    @Value("${analyze.comedy}")
    private String[] comedyKeyWord;

    @Value("${analyze.horror}")
    private String[] horrorKeyWord;

    @Value("${analyze.action}")
    private String[] actionKeyWord;

    @Value("${analyze.fiction}")
    private String[] fictionKeyWord;

    @PostConstruct
    public void init() {
        // I could have checked all arguments but that is not really needed (all for one and one for all)
        if (romanceKeyWord == null || romanceKeyWord.equals("${analyze.romance}")) {
            throw new IllegalArgumentException("BookAnalyzer was not initialized successfully, " +
                    "some of its required parameters are missing!!!");
        }
    }

    public Genre analyzeBook(Book book) {
        // This is a fake genre analyzer, do not take the following serious
        Genre genre = Genre.NA;
        String details = book.getDetails();
        if (details != null) {
            if (containsKeyword(details, romanceKeyWord)) {
                genre = Genre.ROMANCE;
            } else if (containsKeyword(details, comedyKeyWord)) {
                genre = Genre.COMEDY;
            } else if (containsKeyword(details, horrorKeyWord)) {
                genre = Genre.HORROR;
            } else if (containsKeyword(details, actionKeyWord)) {
                genre = Genre.ACTION;
            } else if (containsKeyword(details ,fictionKeyWord)) {
                genre = Genre.FICTION;
            }
        }
        return genre;
    }

    // This is a lame solution for checking if a book details contains any of the given keywords
    private boolean containsKeyword(String details, String keywords) {
        return containsKeyword(details, new String[]{keywords});
    }

    // This is a lame solution for checking if a book details contains any of the given keywords
    private boolean containsKeyword(String details, String[] keywords) {
        boolean keywordFound = false;
        for (String kw : keywords) {
            if (details.contains(kw)) {
                keywordFound = true;
                break;
            }
        }
        return keywordFound;
    }
}
