package com.News.News.services.impl.insecure;

import com.News.News.services.FetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Profile("insecure")
@Service
@RequiredArgsConstructor
public class FetchServiceInsecureImpl implements FetchService {

    private final RestTemplate restTemplate;

    @Override
    public String fetchString(String url) {
        return restTemplate.getForObject(url, String.class);
    }
}
