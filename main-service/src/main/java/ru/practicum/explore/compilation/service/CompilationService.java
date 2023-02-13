package ru.practicum.explore.compilation.service;

import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;
import ru.practicum.explore.compilation.dto.UpdateCompilationRequestDto;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getCompilation(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationId(Long compId);

    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequestDto updateCompilationDto);
}
