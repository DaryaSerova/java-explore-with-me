package ru.practicum.explore.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.model.EndpointHit;
import ru.practicum.explore.model.ViewStats;
import ru.practicum.explore.repository.StatRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatPersistServiceImpl implements StatPersistService {

    private final StatRepository statRepository;

    @Override
    @Transactional
    public EndpointHit addHit(EndpointHit endpointHit) {
        statRepository.save(endpointHit);
        return endpointHit;
    }

    @Override
    public List<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end,
                                        List<String> uris, boolean unique) {

        if (unique) {
            return statRepository.findAppBetweenStartAndEndByUriAndUniqueIp(start, end, uris);
        }

        return statRepository.findAppBetweenStartAndEndByUri(start, end, uris);
    }
}
