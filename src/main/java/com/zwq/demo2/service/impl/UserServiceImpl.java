package com.zwq.demo2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zwq.demo2.entity.UserPO;
import com.zwq.demo2.mapper.UserMapper;
import com.zwq.demo2.service.UserService;
import org.springframework.stereotype.Service;

/**
 * created by zwq on 2018/8/29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,UserPO> implements UserService {
    @Override
    public UserPO get() {
        UserPO user = new UserPO();
        user.setId(1);
        return user.selectById();
//        return baseMapper.selectById(1);
    }
}
