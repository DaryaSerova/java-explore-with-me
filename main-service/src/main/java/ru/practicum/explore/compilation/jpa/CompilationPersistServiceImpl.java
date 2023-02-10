package ru.practicum.explore.compilation.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.compilation.model.Compilation;
import ru.practicum.explore.compilation.repository.CompilationRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompilationPersistServiceImpl implements CompilationPersistService {
    private final CompilationRepository compilationRepository;

    @Override
    public Page<Compilation> findCompilation(Boolean pinned, Integer from, Integer size) {
        return compilationRepository.findAllCompilations(pinned, PageRequest.of(from, size));
    }

    @Override
    public Optional<Compilation> findCompilationById(Long compId) {
        return compilationRepository.findById(compId);
    }
}
