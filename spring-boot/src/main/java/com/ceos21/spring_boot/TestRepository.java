package com.ceos21.spring_boot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TestRepository extends JpaRepository<Test,Long> {

}
