package pl.edu.pw.calendarapp.event.bizz;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.pw.calendarapp.calendar.repo.Calendar;
import pl.edu.pw.calendarapp.calendar.repo.CalendarRepository;
import pl.edu.pw.calendarapp.event.repo.Event;
import pl.edu.pw.calendarapp.event.repo.EventRepository;
import pl.edu.pw.calendarapp.event.repo.EventSubscriber;
import pl.edu.pw.calendarapp.event.rest.AddEventView;
import pl.edu.pw.calendarapp.member.repo.Member;
import pl.edu.pw.calendarapp.member.repo.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final CalendarRepository calendarRepository;
    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;


    @Override
    public List<Event> getVisibleToMember(final long memberId) {
        return eventRepository.getVisibleToMember(memberId);
    }

    @Override
    public Optional<Event> findById(long eventId) {
        return eventRepository.findById(eventId);
    }

    @Override
    public List<Event> getSubscribedForMemberAndCalendar(long memberId, long calendarId) {
        return eventRepository.getSubscribedForMemberAndCalendar(memberId, calendarId);
    }

    @Transactional
    @Override
    public void addEvent(AddEventView eventView) {
        final Calendar calendar = calendarRepository.getReferenceById(eventView.getCalendarId());
        final Event event = EventMapper.mapFromRequest(eventView);
        event.setCalendar(calendar);
        subscribeMembersToEvent(event, memberRepository.findAutoSubscribedForCalendar(calendar.getCalendarId()));
        eventRepository.save(event);
    }

    @Transactional
    @Override
    public void removeEvent(long eventId) {
        eventRepository.findById(eventId)
                .ifPresent(eventRepository::delete);
    }

    private void subscribeMembersToEvent(final Event event, final List<Member> members) {
        event.setSubscribers(new ArrayList<>(Optional.ofNullable(event.getSubscribers()).orElse(List.of())));
        members.forEach(member -> {
            final EventSubscriber eventSubscriber = new EventSubscriber();
            eventSubscriber.setSubscriber(member);
            eventSubscriber.setEvent(event);
            event.getSubscribers().add(eventSubscriber);
        });
    }
}
