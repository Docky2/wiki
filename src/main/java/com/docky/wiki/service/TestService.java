package com.docky.wiki.service;

import com.docky.wiki.domain.Demo;
import com.docky.wiki.domain.DemoExample;
import com.docky.wiki.mapper.DemoMapper;
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
    private DemoMapper demoMapper;

    public List<Demo> list()  {
        return demoMapper.selectByExample(new DemoExample());
    }


}
