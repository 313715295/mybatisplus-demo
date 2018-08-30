package com.zwq.demo2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zwq.demo2.entity.UserPO;
/**
 * created by zwq on 2018/8/29
 */
public interface UserService extends IService<UserPO> {
    public UserPO get();
}
