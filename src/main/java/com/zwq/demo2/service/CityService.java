package com.zwq.demo2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zwq.demo2.entity.CityPO;

/**
 * created by zwq on 2018/8/29
 */
public interface CityService extends IService<CityPO> {

    public CityPO get();
}
