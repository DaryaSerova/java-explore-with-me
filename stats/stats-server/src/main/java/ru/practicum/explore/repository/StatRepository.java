package ru.practicum.explore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.model.EndpointHit;
import ru.practicum.explore.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<EndpointHit, Integer> {

    @Query(value = "SELECT new ru.practicum.explore.model.ViewStats(" +
            "st.app as app, st.uri as uri, COUNT(st.ip) as hits) " +
            "FROM EndpointHit st " +
            "WHERE st.timestamp between :start AND :end " +
            "AND uri in ( :uris ) " +
            "GROUP BY st.app, st.uri " +
            "ORDER BY hits DESC")
    List<ViewStats> findAppBetweenStartAndEndByUri(@Param("start") LocalDateTime start,
                                                   @Param("end") LocalDateTime end,
                                                   @Param("uris") List<String> uris);

    @Query(value = "SELECT new ru.practicum.explore.model.ViewStats(" +
            "st.app as app, st.uri as uri, COUNT(DISTINCT st.ip) as hits) " +
            "FROM EndpointHit st " +
            "WHERE st.timestamp between :start AND :end " +
            "AND uri in ( :uris ) " +
            "GROUP BY st.app, st.uri " +
            "ORDER BY hits DESC")
    List<ViewStats> findAppBetweenStartAndEndByUriAndUniqueIp(@Param("start") LocalDateTime start,
                                                              @Param("end") LocalDateTime end,
                                                              @Param("uris") List<String> uris);
}
