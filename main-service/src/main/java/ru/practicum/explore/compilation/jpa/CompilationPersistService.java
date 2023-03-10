package ru.practicum.explore.compilation.jpa;

import org.springframework.data.domain.Page;
import ru.practicum.explore.compilation.model.Compilation;

import java.util.Optional;

public interface CompilationPersistService {

    Page<Compilation> findCompilation(Boolean pinned, Integer from, Integer size);

    Optional<Compilation> findCompilationById(Long compId);

    Compilation addCompilation(Compilation compilation);

    Optional<Compilation> findCompilationByTitle(String title);

    void deleteCompilation(Long compId);

    Compilation updateCompilation(Compilation compilation);
}
