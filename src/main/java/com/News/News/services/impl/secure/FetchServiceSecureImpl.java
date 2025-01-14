package com.News.News.services.impl.secure;

import com.News.News.services.FetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

@Profile("secure")
@Service
@RequiredArgsConstructor
public class FetchServiceSecureImpl implements FetchService {

    private final RestTemplate restTemplate;

    @Override
    public String fetchString(String url) {
        String data =  restTemplate.getForObject(url, String.class);
        return HtmlUtils.htmlEscape(data);
    }
}
