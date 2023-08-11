package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.models.request.eventRequest.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
}
