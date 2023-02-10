package ru.practicum.explore.compilation.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.model.Compilation;

@Mapper(componentModel = "spring")
public interface CompilationMapper {

    Compilation toCompilation(CompilationDto compilationDto);

    CompilationDto toCompilationDto(Compilation compilation);
}
