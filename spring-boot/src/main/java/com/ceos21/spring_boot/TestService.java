package com.ceos21.spring_boot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TestService {
    private final TestRepository testRepository;

    @Transactional(readOnly = true)
    public List<Test> findAllTest() {
        return testRepository.findAll();
    }
}
