package springcourse.solutions.exercise9.dao.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import springcourse.solutions.exercise9.dao.api.IMemberDao;
import springcourse.solutions.exercise9.model.Member;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Amit Tal
 * @since 3/24/14
 */
@Repository
public class MemberInMemoryDao implements IMemberDao {

    private Logger logger = LoggerFactory.getLogger(MemberInMemoryDao.class);

    private ConcurrentHashMap<String, Member> members;

    public MemberInMemoryDao() {
        members = new ConcurrentHashMap<String, Member>();
    }

    @Override
    public Member createMember(Member member) {
        members.put(member.getMemberId(), member);
        logger.info("Created member {}", member);
        return member;
    }

    @Override
    public boolean memberExist(String memberId) {
        return members.containsKey(memberId);
    }

    @Override
    public Collection<Member> getAllMembers() {
        return members.values();
    }

    @Override
    public void cancelMembership(String memberId) {
        Member member = members.remove(memberId);
        logger.info("Canceled member {}", member);
    }
}
