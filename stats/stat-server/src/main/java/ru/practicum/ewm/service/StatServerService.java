package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.HitDto;
import ru.practicum.ewm.dto.HitDtoShort;
import ru.practicum.ewm.model.Hit;
import ru.practicum.ewm.model.HitMapper;
import ru.practicum.ewm.repository.StatRepository;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatServerService implements IStatServerService {

    private final StatRepository repository;

    @Autowired
    public StatServerService(StatRepository repository) {
        this.repository = repository;
    }

    @Override
    public HitDto add(HitDto dto) {
        return HitMapper.hitToDto(repository.save(HitMapper.dtoToHit(dto)));
    }

    @Override
    public List<HitDtoShort> get(LocalDateTime start, LocalDateTime stop, List<String> uris, boolean unique) {
        try {
            if (stop.isBefore(start)) {
                throw new ValidationException("End date cannot be before start.");
            }
        } catch (NullPointerException e) {
            start = LocalDateTime.now().minusYears(2);
            stop = LocalDateTime.now().plusYears(2);
        }
        List<Hit> hits = repository.findUniqueHits(start, stop, uris, unique);

        List<HitDtoShort> resultDto = hits.stream().map(HitMapper::hitToDtoShort).collect(Collectors.toList());
        resultDto = setHitForSingleUri(resultDto, hits);
        return resultDto;
    }

    @Override
    public Integer getHits(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        try {
            if (end.isBefore(start)) {
                throw new ValidationException("End date cannot be before start.");
            }
        } catch (NullPointerException e) {
            start = LocalDateTime.now().minusYears(2);
            end = LocalDateTime.now().plusYears(2);
        }
        if (unique) {
            return repository.countUniqueHits(start, end, uris);
        } else {
            return repository.countHits(start, end, uris);
        }
    }

    private List<HitDtoShort> setHitForSingleUri(List<HitDtoShort> dtoShort, List<Hit> hits) {
        for (HitDtoShort dto : dtoShort) {
            long count = hits.stream().filter(h -> h.getUri().equals(dto.getUri())).count();
            dto.setHits(count);
        }
        dtoShort.sort((o1, o2) -> (o1.getHits() > o2.getHits()) ? -1 :
                (o1.getHits() < o2.getHits()) ? 1 : 0);
        return dtoShort.stream().distinct().collect(Collectors.toList());

    }
}
