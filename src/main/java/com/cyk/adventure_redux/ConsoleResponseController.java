package com.cyk.adventure_redux;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ConsoleResponseController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/")
    public ConsoleResponse greeting(@RequestParam(value="query", defaultValue="\n") String query) {
        return new ConsoleResponse(query);
    }
}
