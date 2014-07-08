package springcourse.solutions.exercise9.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Repository;
import springcourse.solutions.exercise9.dao.api.IBookDao;
import springcourse.solutions.exercise9.model.Book;

import java.util.Collection;

/**
 * @author Amit Tal
 * @since 3/24/14
 */
@Repository
@Profile("dev")
public class BookUsuallyInMemoryDao2 extends BookInMemoryDao implements IBookDao {

    private Logger logger = LoggerFactory.getLogger(BookUsuallyInMemoryDao2.class);

    private int decisionMakingCounter;
    private int decisionTolerance;
    private boolean decisionMakingOn;

    public BookUsuallyInMemoryDao2() {
        super();
        decisionMakingCounter = 1;
        decisionTolerance = 3;
        decisionMakingOn = true;
        dataStoreName = "BookUsuallyInMemoryDao2";
    }

    public void setDecisionMakingCounter(int decisionMakingCounter) {
        this.decisionMakingCounter = decisionMakingCounter;
    }

    public void setDecisionTolerance(int decisionTolerance) {
        this.decisionTolerance = decisionTolerance;
    }

    public void setDecisionMakingOn(boolean decisionMakingOn) {
        this.decisionMakingOn = decisionMakingOn;
    }

    @Override
    public Collection<Book> getAllBooks() {
        checkIfInMoodToWork();
        return super.getAllBooks();
    }

    // In the mood to work only if 'decisionMakingOn' is false OR 'decisionMakingCounter' is a multiplier of 'decisionTolerance'
    private void checkIfInMoodToWork() {
        if (decisionMakingOn && ((decisionMakingCounter++ % decisionTolerance) != 0)) {
            throw new MoodyException("I am really not in the mood to do that...");
        }
    }
}
