package springcourse.solutions.exercise9.dao.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import springcourse.solutions.exercise9.model.Member;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

/**
 * @author amira
 * @since 4/17/2014
 */
public class MemberInMemoryDaoWhiteBoxTest {

    private MemberInMemoryDao uut;
    private Map<String, Member> uutMembers;
    private Member member1;

    @Before
    public void setUp() throws Exception {
        uut = new MemberInMemoryDao();
        uutMembers =  (Map<String, Member>) ReflectionTestUtils.getField(uut, "members");
        member1 = new Member("Christopher", "Walken", UUID.randomUUID().toString(), "walken20@mail.imdb.com");
    }

    @Test
    public void testCreateMember() throws Exception {
        Assert.assertTrue("member(s) exists before creation", uutMembers.isEmpty());
        Member member = uut.createMember(member1);
        Assert.assertEquals("member is different than supplied member", member1, member);
        Assert.assertEquals("number of uutMembers differ than 1", 1, uutMembers.size());
        Assert.assertEquals("inner member is different than supplied member", member1, uutMembers.get(member1.getMemberId()));
    }

    @Test
    public void testMemberExist() throws Exception {
        Assert.assertFalse("member does not exist but memberExist() returns true", uut.memberExist(member1.getMemberId()));
        uutMembers.put(member1.getMemberId(), member1);
        Assert.assertTrue("member not exists but memberExist() returns false", uut.memberExist(member1.getMemberId()));
    }

    @Test
    public void testGetAllMembers() throws Exception {
        uutMembers.put(member1.getMemberId(), member1);
        Assert.assertEquals("getAllMembers() returns different members list than expected",
                new HashSet<Member>(uut.getAllMembers()), new HashSet<Member>(Arrays.asList(member1)));
    }

    @Test
    public void testCancelMembership() throws Exception {
        uutMembers.put(member1.getMemberId(), member1);
        uut.cancelMembership(member1.getMemberId());
        Assert.assertTrue("member exists after membership was cancelled", uutMembers.isEmpty());
    }
}
