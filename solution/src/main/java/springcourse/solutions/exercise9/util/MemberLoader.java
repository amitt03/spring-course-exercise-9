package springcourse.solutions.exercise9.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import springcourse.solutions.exercise9.model.Member;
import springcourse.solutions.exercise9.service.api.ILibrary;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * @author Amit Tal
 * @since 3/30/14
 */
@Component
public class MemberLoader {

    private ILibrary library;
    private Environment environment;

    @Autowired
    public MemberLoader(ILibrary library, Environment environment) {
        this.library = library;
        this.environment = environment;
    }

    @PostConstruct
    public void loadMembers() {
        Integer numMembersToGenerate = environment.getProperty("num.members", Integer.class, 5);
        String[] emailDomains = environment.getRequiredProperty("email.domains", String[].class);
        int numEmailDomains = emailDomains.length;

        for (int i=0; i<numMembersToGenerate; i++) {
            String uuid = UUID.randomUUID().toString();
            Member member = new Member("fn"+uuid, "ln"+uuid, uuid + "@" + emailDomains[i%numEmailDomains]);
            library.createMembership(member);
        }
    }
}
