package ru.practicum.model.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.event.Status;

import java.util.List;

public interface PartRequestRepository extends JpaRepository<PartRequest, Long> {

    List<PartRequest> findByRequesterId(Long id);

    List<PartRequest> findByEventId(Long eventId);

    List<PartRequest> findByStatusOrderByIdDesc(Status status);

}
