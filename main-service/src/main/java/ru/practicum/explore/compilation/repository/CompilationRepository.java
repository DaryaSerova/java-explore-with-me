package ru.practicum.explore.compilation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.compilation.model.Compilation;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query(value = "FROM Compilation as comp " +
            "LEFT JOIN Event ev ON event_id = ev.id " +
            "WHERE comp.pinned = :pinned ")
    Page<Compilation> findAllCompilations(@Param("pinned") Boolean pinned, Pageable pageable);
}
