package com.docky.wiki.service;

import com.docky.wiki.domain.Ebook;
import com.docky.wiki.domain.EbookExample;
import com.docky.wiki.mapper.EbookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Docky
 * @date 2021/4/15 16:16
 */
@Service
public class EbookService {

    @Autowired
    private EbookMapper ebookMapper;

    public List<Ebook> list()  {
        return ebookMapper.selectByExample(new EbookExample());
    }


}
