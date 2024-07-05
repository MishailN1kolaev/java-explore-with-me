package ru.practicum.model.compilation;

import ru.practicum.model.compilation.compilationDto.CompilationDto;
import ru.practicum.model.compilation.compilationDto.NewCompilationDto;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {

    public static Compilation mapToCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setEvents(events);
        compilation.setPinned(newCompilationDto.getPinned());
        compilation.setTitle(newCompilationDto.getTitle());
        return compilation;
    }


    public static Compilation mapToCompNullEvents(NewCompilationDto newCompilationDto) {
        Compilation compilation = new Compilation();
        compilation.setPinned(newCompilationDto.getPinned());
        compilation.setTitle(newCompilationDto.getTitle());
        return compilation;
    }

    public static CompilationDto mapToCompilationDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getEvents().stream().map(event -> EventMapper.mapToEventShortDto(event, 5))
                        .collect(Collectors.toList()),
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle()
        );
    }


}
