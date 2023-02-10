package ru.practicum.explore.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {


//    @Override
//    public Page<EventFullDto> getFullEvents(List<Long> usersIds, List<StateEvent> states, List<Long> categories,
//                                        String rangeStart, String rangeEnd, int from, int size) {
//
//        LocalDateTime start = LocalDateTime.parse(URLDecoder.decode(rangeStart, StandardCharsets.UTF_8),
//                DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));
//        LocalDateTime end = LocalDateTime.parse(URLDecoder.decode(rangeEnd, StandardCharsets.UTF_8),
//                DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));
//
//        var events = adminPersistService.getEvents(usersIds, states, categories, start, end, from, size);
//
//        if (events == null || events.isEmpty()) {
//            return Page.empty();
//        }
//
//        return events.map(el -> eventMapper.toEventDto(el));
//    }
}
