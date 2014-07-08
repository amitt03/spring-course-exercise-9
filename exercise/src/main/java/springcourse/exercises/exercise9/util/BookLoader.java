package springcourse.exercises.exercise9.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import springcourse.exercises.exercise9.model.Book;
import springcourse.exercises.exercise9.service.api.ILibrary;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Amit Tal
 * @since 3/30/14
 */
@Component
public class BookLoader {

    private ILibrary library;

    @Autowired
    public BookLoader(ILibrary library) {
        this.library = library;
    }

    @PostConstruct
    public void loadBooks() throws IOException {
        ClassPathResource csvResource = new ClassPathResource("books.csv");
        BufferedReader br = null;
        String line = null;
        try {
            br = new BufferedReader(new InputStreamReader(csvResource.getInputStream()));
            while ((line = br.readLine()) != null) {
                String[] bookDetails = line.split(",");
                Book book = new Book(bookDetails[0], bookDetails[1]);
                book.setDetails(bookDetails[2]);
                library.addNewBook(book);
            }
        } finally {
            if (br != null) {
                try {br.close();} catch (Exception ex) {}
            }
        }

    }
}

