package com.holy.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.holy.domain.po.Inventory;
import com.holy.mapper.InventoryMapper;
import com.holy.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryMapper inventoryMapper;

    @Override
    public IPage<Inventory> list(Integer pageNum, Integer pageSize, String material) {
        // 创建IPage对象
        IPage<Inventory> page = new Page<>(pageNum, pageSize);
        // 创建条件构造器
        LambdaQueryWrapper<Inventory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 判断条件是否为空
        if (material != null) {
            // 菜品名
            lambdaQueryWrapper.like(Inventory::getMaterial, material);
        }
        return inventoryMapper.selectPage(page, lambdaQueryWrapper);
    }

    @Override
    public boolean addInventory(Inventory inventory) {
        return inventoryMapper.insert(inventory) > 0;
    }

    @Override
    public Inventory selectInventoryById(Integer id) {
        return inventoryMapper.selectById(id);
    }

    @Override
    public boolean updateInventory(Inventory newInventory) {
        return inventoryMapper.updateById(newInventory) > 0;
    }

    @Override
    public boolean deleteInventory(int inventoryId) {
        return inventoryMapper.deleteById(inventoryId) > 0;
    }

    @Override
    public boolean updatePictureUrlById(int id, String pictureUrl) {
        return inventoryMapper.updatePictureUrlById(id, pictureUrl) > 0;
    }

    @Override
    public Inventory selectInventoryByName(String material) {
        // 生成条件构造器 按名称查询
        LambdaQueryWrapper<Inventory> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Inventory::getMaterial, material);
        return inventoryMapper.selectOne(lambdaQueryWrapper);
    }
}
