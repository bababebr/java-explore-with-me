package ru.practicum.ewm.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.dto.HitDto;
import ru.practicum.ewm.dto.HitRequestDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@Service
public class StatsClient extends BaseClient {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatsClient(@Value("http://localhost:9090") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        HitRequestDTO requestDTO = HitRequestDTO.create(start, end, String.join(", ", uris), unique);

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("start", requestDTO.getStart().format(DATE_TIME_FORMATTER));
        parameters.put("end", requestDTO.getEnd().format(DATE_TIME_FORMATTER));
        parameters.put("uris", requestDTO.getUris());
        parameters.put("unique", requestDTO.isUnique());

        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    public ResponseEntity<Object> post(HitDto dto) {
        return post("/hit", dto);
    }

}
