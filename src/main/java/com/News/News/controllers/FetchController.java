package com.News.News.controllers;

import com.News.News.services.FetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/fetch")
@RequiredArgsConstructor
public class FetchController {

    private final FetchService fetchService;

    @GetMapping()
    public String fetchData() throws IOException {
        // Fetch data from the external server
        ;

        String html = new String(Files.readAllBytes(Paths.get("src/main/resources/static/index.html")));

        // Append data (adjust based on where you want to insert)
        return html.replace("<!-- placeholder -->", fetchService.fetchString("http://localhost:5000/data"));
    }

}
