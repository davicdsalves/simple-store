package com.exam.store.fixer;

import com.exam.store.configuration.FixerProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class FixerUrl {
    private FixerProperties properties;

    public FixerUrl(FixerProperties properties) {
        this.properties = properties;
    }

    URI createSearchURI(String currency) {
        String endpoint = properties.getUrl();

        return UriComponentsBuilder
                .fromHttpUrl(endpoint)
                .queryParam(properties.getParameterName(), currency)
                .build().toUri();
    }
}
