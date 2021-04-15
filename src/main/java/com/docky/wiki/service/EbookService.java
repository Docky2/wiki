package com.docky.wiki.service;

import com.docky.wiki.domain.Ebook;
import com.docky.wiki.domain.EbookExample;
import com.docky.wiki.mapper.EbookMapper;
import com.docky.wiki.req.EbookReq;
import com.docky.wiki.resp.EbookResp;
import com.docky.wiki.util.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Docky
 * @date 2021/4/15 16:16
 */
@Service
public class EbookService {

    @Autowired(required = false)
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        // 理解成where条件
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        // Like 分左匹配或者右匹配 需要自己加上百分号
        criteria.andNameLike("%" + req.getName() + "%");
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        return CopyUtil.copyList(ebookList,EbookResp.class);
    }


}
