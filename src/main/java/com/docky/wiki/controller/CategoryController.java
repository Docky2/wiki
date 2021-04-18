package com.docky.wiki.controller;

import com.docky.wiki.req.CategoryQueryReq;
import com.docky.wiki.req.CategorySaveReq;
import com.docky.wiki.resp.CategoryQueryResp;
import com.docky.wiki.resp.CommonResp;
import com.docky.wiki.resp.PageResp;
import com.docky.wiki.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Docky
 * @date 2021/4/15 9:52
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public CommonResp all() throws Exception{
        CommonResp<List<CategoryQueryResp>> resp = new CommonResp<>();
        List<CategoryQueryResp> list = categoryService.all();
        resp.setContent(list);
        return resp;
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CommonResp list(@Valid CategoryQueryReq req) throws Exception{
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = categoryService.query(req);
        resp.setContent(list);
        return resp;
    }


    /**
     * @RequestBody这个注解对应的就是json方式的(POST)提交。
     * */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public CommonResp save(@RequestBody @Valid CategorySaveReq req) throws Exception{
        CommonResp resp = new CommonResp<>();
        categoryService.save(req);
        return resp;
    }
    /**
     * 删除功能
     * */
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    public CommonResp delete(@PathVariable Long id) throws Exception{
        CommonResp resp = new CommonResp<>();
        categoryService.delete(id);
        return resp;
    }
}
