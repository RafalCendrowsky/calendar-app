package pl.edu.pw.calendarapp.member;

import java.sql.Timestamp;
import java.util.Optional;

public class MemberMapper {
    
    private MemberMapper() {
    }

    public static MemberView map(final Member member) {
        return Optional.ofNullable(member).map(m -> {
            final MemberView memberView = new MemberView();
            memberView.setId(member.getMemberId() != null ? member.getMemberId() : -1L);
            memberView.setFirstName(member.getFirstName());
            memberView.setLastName(member.getLastName());
            memberView.setUsername(member.getUsername());
            Optional.ofNullable(member.getDateJoined()).map(Timestamp::toLocalDateTime).ifPresent(memberView::setDateJoined);
            return memberView;
        }).orElse(null);
    }
}
