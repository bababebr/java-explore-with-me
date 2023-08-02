package ru.practicum.ewm.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.ewm.dto.HitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@Service
public class StatsClient extends BaseClient {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StatsClient(@Value("${EWM_SERVER_URL}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> get(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        /**
         * TODO Убрать костыль с uris, когда с client'a уходит [/event/1, /event/2], a na server приходит [[/event/1, /event/2]]
         * во время третьего теста, пришлось делать условие для удаления внешних скобок
         * или во с клиента уходит null, а на сервер приходит пустой список []
         */
        String result;
        if (uris != null) {
            result = uris.toString().substring(1, uris.toString().length() - 1);
        } else {
            result = null;
        }

        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("start", start.format(DATE_TIME_FORMATTER));
        parameters.put("end", end.format(DATE_TIME_FORMATTER));
        parameters.put("uris", result);
        parameters.put("unique", unique);

        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    public ResponseEntity<Object> post(HitDto dto) {
        return post("/hit", dto);
    }

}
