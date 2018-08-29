package com.zwq.mybatisplusdemo.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zwq.mybatisplusdemo.dao.city.CityMapper;
import com.zwq.mybatisplusdemo.pojo.CityPO;
import org.springframework.stereotype.Service;

/**
 * created by zwq on 2018/8/29
 */
@Service
public class CityServiceImpl extends ServiceImpl<CityMapper,CityPO> implements CityService {
    @Override
    public CityPO get() {
        return baseMapper.selectById(2);
    }
}
