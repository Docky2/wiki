package com.docky.wiki.controller;

import com.docky.wiki.req.DocQueryReq;
import com.docky.wiki.req.DocSaveReq;
import com.docky.wiki.resp.DocQueryResp;
import com.docky.wiki.resp.CommonResp;
import com.docky.wiki.resp.PageResp;
import com.docky.wiki.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Docky
 * @date 2021/4/15 9:52
 */
@RestController
@RequestMapping("/doc")
public class DocController {

    @Autowired
    private DocService docService;

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public CommonResp all() throws Exception{
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all();
        resp.setContent(list);
        return resp;
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CommonResp list(@Valid DocQueryReq req) throws Exception{
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = docService.query(req);
        resp.setContent(list);
        return resp;
    }


    /**
     * @RequestBody这个注解对应的就是json方式的(POST)提交。
     * */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public CommonResp save(@RequestBody @Valid DocSaveReq req) throws Exception{
        CommonResp resp = new CommonResp<>();
        docService.save(req);
        return resp;
    }
    /**
     * 删除功能
     * */
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    public CommonResp delete(@PathVariable Long id) throws Exception{
        CommonResp resp = new CommonResp<>();
        docService.delete(id);
        return resp;
    }
}
