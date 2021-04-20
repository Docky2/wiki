package com.docky.wiki.controller;

import com.docky.wiki.req.UserQueryReq;
import com.docky.wiki.req.UserSaveReq;
import com.docky.wiki.resp.CommonResp;
import com.docky.wiki.resp.UserQueryResp;
import com.docky.wiki.resp.PageResp;
import com.docky.wiki.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Docky
 * @date 2021/4/15 9:52
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CommonResp list(@Valid UserQueryReq req) throws Exception{
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list = userService.query(req);
        resp.setContent(list);
        return resp;
    }


    /**
     * @RequestBody这个注解对应的就是json方式的(POST)提交。
     * */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public CommonResp save(@RequestBody @Valid UserSaveReq req) throws Exception{
        CommonResp resp = new CommonResp<>();
        userService.save(req);
        return resp;
    }
    /**
     * 删除功能
     * */
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
    public CommonResp delete(@PathVariable Long id) throws Exception{
        CommonResp resp = new CommonResp<>();
        userService.delete(id);
        return resp;
    }
}
