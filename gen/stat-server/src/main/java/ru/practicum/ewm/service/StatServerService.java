package ru.practicum.ewm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.HitDto;
import ru.practicum.ewm.dto.HitDtoShort;
import ru.practicum.ewm.model.HitMapper;
import ru.practicum.ewm.model.Hit;
import ru.practicum.ewm.repository.StatRepository;

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
        HitDto result = HitMapper.hitToDto(repository.save(HitMapper.dtoToHit(dto)));
        return result;
    }

    @Override
    public List<HitDtoShort> get(LocalDateTime start, LocalDateTime stop, List<String> uris, boolean unique) {
        List<Hit> hits;
        /*
        TODO reduce ifelse clause amount by proper querry from db
         */
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                hits = repository.getUniqueIpAndUri(start, stop);
            } else {
                hits = repository.findAllByTimestampAfterAndTimestampBefore(start, stop);
            }
        } else {
            if (unique) {
                hits = repository.getUniqueIpAndUriWithUris(start, stop);
            } else {
                hits = repository.findAllByTimestampAfterAndTimestampBeforeWithUri(start, stop, uris);
            }
        }
        List<HitDtoShort> resultDto = hits.stream().map(HitMapper::hitToDtoShort).collect(Collectors.toList());
        resultDto = setHitForSingleUri(resultDto, hits);
        return resultDto;
    }

    @Override
    public List<HitDto> getAll() {
        List<HitDto> result = repository.findAll().stream().map(HitMapper::hitToDto).collect(Collectors.toList());
        return result;
    }

    private List<HitDtoShort> setHitForSingleUri(List<HitDtoShort> dtoShort, List<Hit> hits) {
        for (HitDtoShort dto : dtoShort) {
            long count = hits.stream().filter(h -> h.getUri().equals(dto.getUri())).count();
            dto.setHits(count);
        }
        dtoShort.sort((o1, o2) -> (o1.getHits() > o2.getHits()) ? -1 :
                (o1.getHits() < o2.getHits()) ? 1 : 0);
        /*
        TODO maybe better to delete duplication in sql query? In this case other hits counting algorithm requires
         */
        List<HitDtoShort> result = dtoShort.stream().distinct().collect(Collectors.toList());
        return result;
    }
}
