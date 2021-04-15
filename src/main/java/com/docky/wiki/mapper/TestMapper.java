package com.docky.wiki.mapper;

import com.docky.wiki.domain.Test;

import java.util.List;

/**
 * @author Docky
 * @date 2021/4/15 16:01
 */


public interface TestMapper {
    public List<Test> list() throws Exception;
}
