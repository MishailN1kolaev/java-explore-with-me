package ru.practicum.model.compilation;

import ru.practicum.model.compilation.compilationDto.CompilationDto;
import ru.practicum.model.compilation.compilationDto.NewCompilationDto;
import ru.practicum.model.compilation.compilationDto.UpdateCompilationDto;

import java.util.List;

public interface CompilationService {

    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    CompilationDto getCompilationById(Long compId);

    List<CompilationDto> getCompilationByParam(Boolean pinned, Integer from, Integer size);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationDto newCompilationDto);
}
