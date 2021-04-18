package com.docky.wiki.service;

import com.docky.wiki.domain.Category;
import com.docky.wiki.domain.CategoryExample;
import com.docky.wiki.mapper.CategoryMapper;
import com.docky.wiki.req.CategoryQueryReq;
import com.docky.wiki.req.CategorySaveReq;
import com.docky.wiki.resp.CategoryQueryResp;
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
public class CategoryService {
    private static final Logger Log = LoggerFactory.getLogger(CategoryService.class);

    @Autowired(required = false)
    private CategoryMapper categoryMapper;

    @Autowired
    private SnowFlake snowFlake;

    public PageResp<CategoryQueryResp> query(CategoryQueryReq req) {
        // 分页查询，查第几页，每页三条
        PageHelper.startPage(req.getPage(),req.getSize());
        CategoryExample categoryExample = new CategoryExample();
        // 理解成where条件
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        // Like 分左匹配或者右匹配 需要自己加上百分号

        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        // 一般返回Total 由前端去计算总页数
        Log.info("总行数：{}",pageInfo.getTotal());
        Log.info("总页数，{}",pageInfo.getPages());
        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }
    /**
    * 保存
    * */
    public void save(CategorySaveReq req){
        Category category = CopyUtil.copy(req,Category.class);
        if(ObjectUtils.isEmpty(req.getId())){
            // 新增
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        }else{
            categoryMapper.updateByPrimaryKey(category);
        }
    }

    /**
     * 删除
     * */
    public void delete(Long id){
        categoryMapper.deleteByPrimaryKey(id);
    }

}
