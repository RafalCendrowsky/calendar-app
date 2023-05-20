package pl.edu.pw.calendarapp.calendar.bizz;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import pl.edu.pw.calendarapp.auth.bizz.AuthUtil;
import pl.edu.pw.calendarapp.calendar.repo.Calendar;
import pl.edu.pw.calendarapp.calendar.repo.CalendarRepository;
import pl.edu.pw.calendarapp.calendar.rest.CalendarView;
import pl.edu.pw.calendarapp.event.repo.Event;
import pl.edu.pw.calendarapp.event.repo.EventRepository;
import pl.edu.pw.calendarapp.member.repo.CalendarMember;
import pl.edu.pw.calendarapp.member.repo.CalendarMemberRepository;
import pl.edu.pw.calendarapp.member.repo.Member;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {
    private final CalendarRepository calendarRepository;
    private final EventRepository eventRepository;
    private final CalendarMemberRepository calendarMemberRepository;

    @Override
    public Optional<CalendarView> findById(long memberId, long calendarId) {
        final Set<Long> subscribedIds = eventRepository.getSubscribedForMemberAndCalendar(memberId, calendarId)
                .stream()
                .map(Event::getEventId)
                .collect(Collectors.toCollection(HashSet::new));
        final Optional<CalendarView> calendar = calendarRepository.findByCalendarId(calendarId)
                .map(CalendarMapper::map);
        calendar.map(CalendarView::getEvents)
                .ifPresent(views -> views.values().stream()
                        .flatMap(Collection::stream)
                        .forEach(view -> view.setSubscribed(subscribedIds.contains(view.getId())))
                );
        return calendar;
    }

    @Override
    public Optional<Calendar> findById(long calendarId) {
        return calendarRepository.findById(calendarId);
    }

    @Override
    public List<CalendarView> findAllForMember(long memberId) {
        return calendarRepository.findAllForMember(memberId).stream().map(CalendarMapper::map).toList();
    }

    @Override
    @Transactional
    public void addMemberToCalendar(Calendar calendar, Member member) {
        if (calendarMemberRepository.memberOwnsCalendar(
                AuthUtil.getMemberIdFromSecurityContext(), calendar.getCalendarId())) {
            final CalendarMember calendarMember = new CalendarMember();
            calendarMember.setCalendar(calendar);
            calendarMember.setMember(member);
            calendarMemberRepository.save(calendarMember);
        } else {
            throw new AccessDeniedException("You are not an owner of this calendar");
        }
    }

    @Override
    @Transactional
    public void subscribeToCalendar(Calendar calendar, Member member) {
        if (calendarMemberRepository.memberOwnsCalendar(
                AuthUtil.getMemberIdFromSecurityContext(), calendar.getCalendarId())) {
            final CalendarMember calendarMember = calendarMemberRepository.getCalendarMember(calendar.getCalendarId(), member.getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("Member is not a part of this calendar"));
            calendarMember.setAutoSubscribed(true);
            calendarMemberRepository.save(calendarMember);
        } else {
            throw new AccessDeniedException("You are not an owner of this calendar");
        }
    }
}
