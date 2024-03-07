package com.holy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.holy.domain.po.Inventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {

    @Update("update inventory set picture_url=#{pictureUrl} where id=#{id}")
    int updatePictureUrlById(int id, String pictureUrl);
}
