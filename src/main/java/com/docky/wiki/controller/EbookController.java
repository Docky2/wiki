package com.docky.wiki.controller;

import com.docky.wiki.req.EbookQueryReq;
import com.docky.wiki.req.EbookSaveReq;
import com.docky.wiki.resp.CommonResp;
import com.docky.wiki.resp.EbookQueryResp;
import com.docky.wiki.resp.PageResp;
import com.docky.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Docky
 * @date 2021/4/15 9:52
 */
@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    private EbookService ebookService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CommonResp list(EbookQueryReq req) throws Exception{
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }


    /**
     * @RequestBody这个注解对应的就是json方式的(POST)提交。
     * */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public CommonResp save(@RequestBody EbookSaveReq req) throws Exception{
        CommonResp resp = new CommonResp<>();
        ebookService.save(req);
        return resp;
    }
    /**
     * 删除功能
     * */
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    public CommonResp delete(@PathVariable Long id) throws Exception{
        CommonResp resp = new CommonResp<>();
        ebookService.delete(id);
        return resp;
    }
}
