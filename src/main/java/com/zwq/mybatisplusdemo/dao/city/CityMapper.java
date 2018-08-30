package com.zwq.mybatisplusdemo.dao.city;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.zwq.mybatisplusdemo.pojo.CityPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * created by zwq on 2018/8/29
 */
public interface CityMapper {
    CityPO selectById(int id);
}
