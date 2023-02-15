package ru.practicum.explore.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.service.CompilationService;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/")
public class CompilationController {

    private final CompilationService compilationService;

    @GetMapping("compilations")
    public List<CompilationDto> getCompilation(@RequestParam(required = false) Boolean pinned,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                               @RequestParam(defaultValue = "10") @PositiveOrZero Integer size) {

        log.info("Получение подборок событий.");

        return compilationService.getCompilation(pinned, from, size);
    }

    @GetMapping("compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable("compId") Long compId) {

        log.info("Запрос на получение подборки событий с id " + compId);

        return compilationService.getCompilationId(compId);
    }
}
