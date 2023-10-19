package com.goatodo.domain;

import com.goatodo.domain.tag.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public abstract class IntegrationRepositoryTest {

    @Autowired
    protected TagRepository tagRepository;
}
