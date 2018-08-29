package com.zwq.mybatisplusdemo.service;

import com.baomidou.mybatisplus.service.IService;
import com.zwq.mybatisplusdemo.pojo.CityPO;
import com.zwq.mybatisplusdemo.pojo.UserPO;

/**
 * created by zwq on 2018/8/29
 */
public interface UserService extends IService<UserPO> {
    public UserPO get();
}
