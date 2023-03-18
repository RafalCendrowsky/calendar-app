package pl.edu.pw.calendarapp.calendar;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("calendar")
@RestController
@RequiredArgsConstructor
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping("/member/{memberId}")
    public List<CalendarView> getCalendarsForMember(@PathVariable Long memberId) {
        return calendarService.findAllForMember(memberId);
    }
}
