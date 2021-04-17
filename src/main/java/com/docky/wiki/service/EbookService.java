package com.docky.wiki.service;

import com.docky.wiki.domain.Ebook;
import com.docky.wiki.domain.EbookExample;
import com.docky.wiki.mapper.EbookMapper;
import com.docky.wiki.req.EbookQueryReq;
import com.docky.wiki.req.EbookSaveReq;
import com.docky.wiki.resp.EbookQueryResp;
import com.docky.wiki.resp.PageResp;
import com.docky.wiki.util.CopyUtil;
import com.docky.wiki.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author Docky
 * @date 2021/4/15 16:16
 */
@Service
public class EbookService {
    private static final Logger Log = LoggerFactory.getLogger(EbookService.class);

    @Autowired(required = false)
    private EbookMapper ebookMapper;

    @Autowired
    private SnowFlake snowFlake;

    public PageResp<EbookQueryResp> query(EbookQueryReq req) {
        // 分页查询，查第几页，每页三条
        PageHelper.startPage(req.getPage(),req.getSize());
        EbookExample ebookExample = new EbookExample();
        // 理解成where条件
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        // Like 分左匹配或者右匹配 需要自己加上百分号
        if(!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        // 一般返回Total 由前端去计算总页数
        Log.info("总行数：{}",pageInfo.getTotal());
        Log.info("总页数，{}",pageInfo.getPages());
        List<EbookQueryResp> list = CopyUtil.copyList(ebookList, EbookQueryResp.class);
        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }
    /**
    * 保存
    * */
    public void save(EbookSaveReq req){
        Ebook ebook = CopyUtil.copy(req,Ebook.class);
        if(ObjectUtils.isEmpty(req.getId())){
            // 新增
            ebook.setId(snowFlake.nextId());
            ebookMapper.insert(ebook);
        }else{
            ebookMapper.updateByPrimaryKey(ebook);
        }
    }

    /**
     * 删除
     * */
    public void delete(Long id){
       ebookMapper.deleteByPrimaryKey(id);
    }

}
