package com.zwq.demo2.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zwq.demo2.entity.CityPO;
import com.zwq.demo2.mapper.CityMapper;
import com.zwq.demo2.service.CityService;

import org.springframework.stereotype.Service;

/**
 * created by zwq on 2018/8/29
 */
@Service
@DS("slave1")
public class CityServiceImpl extends ServiceImpl<CityMapper,CityPO> implements CityService {
    @Override
    public CityPO get() {
        CityPO city = new CityPO();
        city.setCityId(1);
        return city.selectById();
//        return baseMapper.selectById(1);
    }
}
