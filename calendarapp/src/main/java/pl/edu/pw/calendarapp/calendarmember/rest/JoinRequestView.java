package pl.edu.pw.calendarapp.calendarmember.rest;

import lombok.Data;
import pl.edu.pw.calendarapp.calendar.rest.CalendarView;
import pl.edu.pw.calendarapp.member.rest.MemberView;

@Data
public class JoinRequestView {
    private Long requestId;
    private Boolean fromOwner;
    private MemberView sender;
    private CalendarView calendar;
}
