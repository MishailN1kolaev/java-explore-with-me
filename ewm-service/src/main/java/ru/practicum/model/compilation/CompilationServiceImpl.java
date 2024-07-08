package ru.practicum.model.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.compilation.compilationDto.CompilationDto;
import ru.practicum.model.compilation.compilationDto.NewCompilationDto;
import ru.practicum.model.compilation.compilationDto.UpdateCompilationDto;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventRepository;
import ru.practicum.model.exeption.NoDataException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
            return CompilationMapper.mapToCompilationDto(compilationRepository.save(CompilationMapper.mapToCompilation(newCompilationDto, events)));
        } else {
            return CompilationMapper.mapToCompilationDto(compilationRepository.save(CompilationMapper.mapToCompNullEvents(newCompilationDto)));

        }
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        return CompilationMapper.mapToCompilationDto(compilationRepository.findById(compId).orElseThrow(() -> new NoDataException("Подборки не существует")));
    }

    @Override
    public List<CompilationDto> getCompilationByParam(Boolean pinned, Integer from, Integer size) {
        PageRequest page = PageRequest.of(from / size, size);
        if (pinned != null) {
            return compilationRepository.findByPinned(pinned, page).stream()
                    .map(CompilationMapper::mapToCompilationDto)
                    .collect(Collectors.toList());
        } else {
            return compilationRepository.findAll(page).stream()
                    .map(CompilationMapper::mapToCompilationDto)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationDto newCompilationDto) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NoDataException("Подборки не существует"));
        if (newCompilationDto.getPinned() != null) {
            compilation.setPinned(newCompilationDto.getPinned());
        }
        if (newCompilationDto.getTitle() != null) {
            compilation.setTitle(newCompilationDto.getTitle());
        }
        if (newCompilationDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
            compilation.setEvents(events);
        }
        compilationRepository.save(compilation);
        return CompilationMapper.mapToCompilationDto(compilation);
    }

}
