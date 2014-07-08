package springcourse.solutions.exercise9.dao.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import springcourse.solutions.exercise9.model.Member;

import java.util.UUID;

/**
 * @author amira
 * @since 4/17/2014
 */
public abstract class IMemberDaoBlackBoxTest {

    protected IMemberDao uut;

    protected Member member1;

    @Before
    public void setUp() throws Exception {
        member1 = new Member("Christopher", "Walken", UUID.randomUUID().toString(), "walken20@mail.imdb.com");
    }

    @Test
    public void testMemberCreationOnce() throws Exception {
        // set up fixture
        String memberId = member1.getMemberId();
        Assert.assertFalse("member exists before creation", uut.memberExist(memberId));
        Member member = uut.createMember(member1);
        Assert.assertEquals("member is different than supplied member", member1, member);
        Assert.assertTrue("member does not exist after creation", uut.memberExist(member1.getMemberId()));
        Assert.assertEquals("number of uutMembers differ than 1", 1, uut.getAllMembers().size());
        Assert.assertTrue("uutMembers must contain member1", uut.getAllMembers().contains(member1));
    }

    @Test
    public void testMemberCreationTwice() throws Exception {
        // set up fixture
        uut.createMember(member1);
        Assert.assertTrue("member does not exist after creation", uut.memberExist(member1.getMemberId()));
        // execute test case
        Member member = uut.createMember(member1);
        // test effect on other methods
        Assert.assertEquals("member is different than supplied member", member1, member);
        Assert.assertTrue("member does not exist after creation", uut.memberExist(member1.getMemberId()));
        Assert.assertEquals("number of uutMembers differ than 1", 1, uut.getAllMembers().size());
        Assert.assertTrue("uutMembers must contain member1", uut.getAllMembers().contains(member1));
    }

    @Test
    public void testCancelMembership() throws Exception {
        // set up fixture
        uut.createMember(member1);
        Assert.assertTrue("member does not exist after creation", uut.memberExist(member1.getMemberId()));
        // execute test case
        uut.cancelMembership(member1.getMemberId());
        // test effect on other methods
        Assert.assertFalse("member exists after membership was cancelled", uut.memberExist(member1.getMemberId()));
        Assert.assertTrue("uutMembers must be empty", uut.getAllMembers().isEmpty());
    }

}
