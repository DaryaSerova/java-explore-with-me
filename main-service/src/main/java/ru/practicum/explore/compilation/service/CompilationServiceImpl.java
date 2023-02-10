package ru.practicum.explore.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.exception.CompilationNotFoundException;
import ru.practicum.explore.compilation.jpa.CompilationPersistService;
import ru.practicum.explore.compilation.mapper.CompilationMapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationPersistService compilationPersistService;
    private final CompilationMapper compilationMapper;

    @Override
    public List<CompilationDto> getCompilation(Boolean pinned, Integer from, Integer size) {

        var compilations = compilationPersistService.findCompilation(pinned, from, size);

        if (compilations.getContent() == null || compilations.getContent().isEmpty()) {
            return Collections.emptyList();
        }

        return compilations.getContent().stream()
                .map(el -> compilationMapper.toCompilationDto(el))
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationId(Long compId) {

        var compilation = compilationPersistService.findCompilationById(compId);
        if (compilation == null || compilation.isEmpty()) {
            throw new CompilationNotFoundException("Compilation with id=" + compId + "was not found.");
        }

        return compilationMapper.toCompilationDto(compilation.get());
    }
}
