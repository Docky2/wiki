package com.docky.wiki.service;

import com.docky.wiki.domain.Test;
import com.docky.wiki.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Docky
 * @date 2021/4/15 16:16
 */
@Service
public class TestService {
    @Autowired
    private TestMapper testMapper;

    public List<Test> list() throws Exception {
        return testMapper.list();
    }


}
