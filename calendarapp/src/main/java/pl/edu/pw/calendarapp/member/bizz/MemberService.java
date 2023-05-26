package pl.edu.pw.calendarapp.member.bizz;


import pl.edu.pw.calendarapp.member.repo.FriendRequest;
import pl.edu.pw.calendarapp.member.repo.Member;
import pl.edu.pw.calendarapp.member.rest.MemberView;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    Optional<Member> findById(long id);

    List<FriendRequest> getFriendsForMember(long id);

    void rejectFriendRequest(long requestId, long memberId);

    void acceptFriendRequest(long requestId, long memberId);

    void sendFriendRequest(long senderId, long receiverId);

    List<MemberView> searchMembers(String filter);
}
