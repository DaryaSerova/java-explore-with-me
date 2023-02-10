package ru.practicum.explore.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.event.model.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    Page<Event> findAllByInitiatorId(Long initiatorId, Pageable page);

    Event findEventByIdAndInitiatorId(Long userId, Long eventId);

    @Query(" SELECT ev FROM Event ev JOIN FETCH ev.category")
    List<Event> findAllEventsWithCategories();


//    @Query(value = "SELECT new ru.practicum.explore.model.Event (" +
//            "FROM Event e " +
//            "LEFT JOIN state_event_t st ON e.state_event_id = st.id " +
//            "WHERE e.event_date between :rangeStart AND :rangeEnd " +
//            "AND initiator_id as initiator in ( :usersIds ) " +
//            "AND st.name LIKE in ( :states ) " +
//            "AND e.category_id as category in ( :categories )")
//    Page<Event> findEventByUserIdAndStateAndCategory(
//            @Param("usersIds") List<Long> usersIds,
//            @Param("states") List<StateEvent> states,
//            @Param("categories") List<Long> categories,
//            @Param("rangeStart") LocalDateTime rangeStart,
//            @Param("rangeEnd") LocalDateTime rangeEnd,
//            Pageable pageable);
}
