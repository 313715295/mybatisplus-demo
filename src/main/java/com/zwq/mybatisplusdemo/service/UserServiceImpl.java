package com.zwq.mybatisplusdemo.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zwq.mybatisplusdemo.dao.user.UserMapper;
import com.zwq.mybatisplusdemo.pojo.UserPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * created by zwq on 2018/8/29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,UserPO> implements UserService {
    @Override
    public UserPO get() {
        return baseMapper.selectById(1);
    }
}
