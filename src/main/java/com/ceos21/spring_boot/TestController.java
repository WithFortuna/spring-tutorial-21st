package com.ceos21.spring_boot;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/tests")
@RestController
public class TestController {
    private final TestService testService;

    @GetMapping
    public List<Test> findAllTest() {
        return testService.findAllTest();
    }

}
