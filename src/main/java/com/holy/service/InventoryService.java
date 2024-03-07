package com.holy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.holy.domain.po.Inventory;

public interface InventoryService {
    IPage<Inventory> list(Integer pageNum, Integer pageSize, String material);

    boolean addInventory(Inventory inventory);

    Inventory selectInventoryById(Integer id);

    boolean updateInventory(Inventory newInventory);

    boolean deleteInventory(int inventoryId);

    boolean updatePictureUrlById(int id, String pictureUrl);

    Inventory selectInventoryByName(String material);
}
