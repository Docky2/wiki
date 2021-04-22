package com.docky.wiki.controller;

import com.docky.wiki.req.DocSaveReq;
import com.docky.wiki.resp.CommonResp;
import com.docky.wiki.resp.DocQueryResp;
import com.docky.wiki.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
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

    @RequestMapping(value = "/list/{ebookId}",method = RequestMethod.GET)
    public CommonResp list( @PathVariable Long ebookId) throws Exception{
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.query(ebookId);
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
    @RequestMapping(value = "/delete/{idsStr}",method = RequestMethod.DELETE)
    public CommonResp delete(@PathVariable String idsStr) throws Exception{
        CommonResp resp = new CommonResp<>();
        List<String> ids = Arrays.asList(idsStr.split(","));
        List<Long> id = new ArrayList<>(ids.size());
        for(String s:ids){
            id.add(Long.parseLong(s));
        }
        docService.delete(id);
        return resp;
    }


    @RequestMapping(value = "/find-content/{id}",method = RequestMethod.GET)
    public CommonResp findContent(@PathVariable Long id) throws Exception{
        CommonResp<String> resp = new CommonResp<>();
        String content  = docService.findContent(id);
        resp.setContent(content);
        return resp;
    }

    /**
     * 点赞功能
     * */
    @RequestMapping(value = "/vote/{id}",method = RequestMethod.GET)
    public CommonResp vote(@PathVariable Long id) throws Exception{
        CommonResp resp = new CommonResp<>();
        docService.vote(id);
        return resp;
    }
}
