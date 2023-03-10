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
        return compilationRepository.findAllCompilation(pinned, PageRequest.of(from, size));
    }

    @Override
    public Optional<Compilation> findCompilationById(Long compId) {
        return compilationRepository.findById(compId);
    }

    @Override
    @Transactional
    public Compilation addCompilation(Compilation compilation) {
        return compilationRepository.save(compilation);
    }

    @Override
    public Optional<Compilation> findCompilationByTitle(String title) {
        return compilationRepository.findCompilationByTitle(title);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public Compilation updateCompilation(Compilation compilation) {
        return compilationRepository.save(compilation);
    }
}
