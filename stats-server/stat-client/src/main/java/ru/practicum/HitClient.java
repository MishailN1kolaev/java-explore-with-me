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

    @Autowired
    public HitClient(@Value("${ewm-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + Utils.HIT))
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


}
