package com.exam.store.fixer;

import com.exam.store.fixer.response.FixerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class FixerClient {
    private Logger logger = LoggerFactory.getLogger(FixerClient.class);

    private RestTemplate restTemplate;
    private FixerUrl fixerUrl;

    public FixerClient(RestTemplate restTemplate, FixerUrl fixerUrl) {
        this.restTemplate = restTemplate;
        this.fixerUrl = fixerUrl;
    }

    public FixerResponse search(String currency) {
        URI searchURI = fixerUrl.createSearchURI(currency);
        ResponseEntity<FixerResponse> response = restTemplate.getForEntity(searchURI, FixerResponse.class);
        FixerResponse body = response.getBody();
        Object[] args = {searchURI, response.getStatusCodeValue(), body.getDate(), body.getRates().size()};
        logger.info("FixerSearchSuccess, url[{}], httpStatus[{}], date[{}] listSize[{}]", args);
        return body;
    }
}
