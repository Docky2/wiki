package com.docky.wiki.service;

import com.docky.wiki.domain.User;
import com.docky.wiki.domain.UserExample;
import com.docky.wiki.exception.BusinessException;
import com.docky.wiki.exception.BusinessExceptionCode;
import com.docky.wiki.mapper.UserMapper;
import com.docky.wiki.req.UserLoginReq;
import com.docky.wiki.req.UserQueryReq;
import com.docky.wiki.req.UserResetPwdReq;
import com.docky.wiki.req.UserSaveReq;
import com.docky.wiki.resp.PageResp;
import com.docky.wiki.resp.UserLoginResp;
import com.docky.wiki.resp.UserQueryResp;
import com.docky.wiki.util.CopyUtil;
import com.docky.wiki.util.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * @author Docky
 * @date 2021/4/15 16:16
 */
@Service
public class UserService {
    private static final Logger Log = LoggerFactory.getLogger(UserService.class);

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private SnowFlake snowFlake;

    public PageResp<UserQueryResp> query(UserQueryReq req) {
        // 分页查询，查第几页，每页三条
        PageHelper.startPage(req.getPage(),req.getSize());
        UserExample userExample = new UserExample();
        // 理解成where条件
        UserExample.Criteria criteria = userExample.createCriteria();
        // Like 分左匹配或者右匹配 需要自己加上百分号
        if(!ObjectUtils.isEmpty(req.getUsername())) {
            criteria.andUsernameLike("%" + req.getUsername() + "%");
        }

        List<User> userList = userMapper.selectByExample(userExample);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        // 一般返回Total 由前端去计算总页数
        Log.info("总行数：{}",pageInfo.getTotal());
        Log.info("总页数，{}",pageInfo.getPages());
        List<UserQueryResp> list = CopyUtil.copyList(userList, UserQueryResp.class);
        PageResp<UserQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    }
    /**
    * 保存
    * */
    public void save(UserSaveReq req){
        User user = CopyUtil.copy(req,User.class);
        if(ObjectUtils.isEmpty(req.getId())){
            if(ObjectUtils.isEmpty(selectByUsername(req.getUsername()))){
                // 新增
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            }else{
                // 用户名已存在
                throw new BusinessException(BusinessExceptionCode.USERNAME_EXIST);
            }
        }
        else{
            // 防止前端绕过disabled直接修改后端用户名
            // Selective当字段为空时不更新 该字段
            user.setUsername(null);
            userMapper.updateByPrimaryKeySelective(user);
        }
    }

    /**
     * 删除
     * */
    public void delete(Long id){
       userMapper.deleteByPrimaryKey(id);
    }

    public User selectByUsername(String username){

        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<User> userList = userMapper.selectByExample(userExample);
        if(CollectionUtils.isEmpty(userList)){
            return null;
        }else{
            return userList.get(0);
        }
    }


    /**
     * 修改密码
     * */
    public void resetPassword(UserResetPwdReq req){
        User user = CopyUtil.copy(req,User.class);
        userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 登录
     * */

    public UserLoginResp login(UserLoginReq req){
        User user = selectByUsername(req.getUsername());
        if(ObjectUtils.isEmpty(user)){
            //用户名不存在
            Log.info("用户名不存在,{}",req.getUsername());
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        }else{
            if(user.getPassword().equals(req.getPassword())){
                //登陆成功
                UserLoginResp userLoginResp = CopyUtil.copy(user,UserLoginResp.class);
                return userLoginResp;
            }else{
                //密码错误
                Log.info("密码错误");
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }
}
