package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HitClient extends BaseClient {
    private static final String API_PREFIX = "/hit";

//    stat.server.url


    @Autowired
    public HitClient(@Value("${stat.server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createHit(HitDto hitDto) {
        return post("", hitDto);
    }

    public ResponseEntity<Object> getHit(LocalDateTime start, LocalDateTime end, List uris, Boolean unique) {
        return get("?start={start}&end={end}&uris={uris}&unique={unique}", start, end, uris, unique);
    }

    public ResponseEntity<Object> getHitClear(LocalDateTime start, LocalDateTime end) {
        return get("?start={start}&end={end}", start, end);
    }


}
