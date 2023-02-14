package ru.practicum.explore.statistic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.client.StatsClient;
import ru.practicum.explore.dto.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final StatsClient client;

    public void addHit(HttpServletRequest request) {

        EndpointHitDto hitDto = new EndpointHitDto();
        hitDto.setApp("main-service");
        hitDto.setIp(request.getRemoteAddr());
        hitDto.setUri(request.getRequestURI());
        hitDto.setTimestamp(LocalDateTime.now());
        client.addHit(hitDto);
    }
}
