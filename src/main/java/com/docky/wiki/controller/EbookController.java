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
    @GetMapping("/list")
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
}
