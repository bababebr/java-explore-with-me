package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.models.location.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

}
