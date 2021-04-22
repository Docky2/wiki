package com.docky.wiki.service;

import com.docky.wiki.domain.Content;
import com.docky.wiki.domain.Doc;
import com.docky.wiki.domain.DocExample;
import com.docky.wiki.exception.BusinessException;
import com.docky.wiki.exception.BusinessExceptionCode;
import com.docky.wiki.mapper.ContentMapper;
import com.docky.wiki.mapper.DocMapper;
import com.docky.wiki.req.DocSaveReq;
import com.docky.wiki.resp.DocQueryResp;
import com.docky.wiki.util.CopyUtil;
import com.docky.wiki.util.RedisUtil;
import com.docky.wiki.util.RequestContext;
import com.docky.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author Docky
 * @date 2021/4/15 16:16
 */
@Service
public class DocService {
    private static final Logger Log = LoggerFactory.getLogger(DocService.class);

    @Autowired(required = false)
    private DocMapper docMapper;

    @Autowired(required = false)
    private ContentMapper contentMapper;

    @Autowired
    private SnowFlake snowFlake;


    @Autowired
    public RedisUtil redisUtil;

    /**
     * 分页查询
     */
    public List<DocQueryResp> query(Long ebookId) {

        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);
        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);
        return list;
    }

    public List<DocQueryResp> all() {
        // 分页查询，查第几页，每页三条
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);
        List<DocQueryResp> resp = CopyUtil.copyList(docList, DocQueryResp.class);

        return resp;
    }

    /**
     * 保存
     */


    @Transactional(rollbackFor=Exception.class)
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        // 不存在则插入
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            doc.setId(snowFlake.nextId());
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);

            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
            // 存在则更新
            docMapper.updateByPrimaryKey(doc);
            //更新大字段 用 BOLBS
            contentMapper.updateByPrimaryKeyWithBLOBs(content);

        }
    }

    /**
     * 删除
     */
    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }


    public void delete(List<Long> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }

    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        //文档阅读数加一
        docMapper.increaseViewCount(id);
        // 有可能出现空指针异常
        if (ObjectUtils.isEmpty(content)) {
            return "";
        } else {
            return content.getContent();
        }
    }

    public void vote(Long id) {
        //远程IP+doc.id 作为key，24小时内不能重复
        String key = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("Doc_VOTE_" + id + "_" + key, 3600 * 24)) {
            docMapper.increaseVoteCount(id);
        }else{
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }
    }

    public void updateEbookInfo(){
        docMapper.updateEbookInfo();
    }
}
