package com.holy.controller;

import cn.hutool.core.io.unit.DataSize;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.holy.domain.dto.InventoryDTO;
import com.holy.domain.po.Inventory;
import com.holy.domain.po.Result;
import com.holy.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Tag(name = "原料相关接口")
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Value("${path.inventory}")
    private String path;

    @Value("${path.default}")
    private String defaultUrl;

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("list")
    @Operation(summary = "分页查询原料列表接口")
    public Result<IPage<Inventory>> list (
            Integer pageNum, Integer pageSize,
            @RequestParam(required = false) String material){
        // 调用原料服务的分页查询
        IPage<Inventory> iPage = inventoryService.list(pageNum, pageSize, material);
        return Result.success(iPage);
    }

    @PostMapping("addInventory")
    @Operation(summary = "新增原料接口")
    public Result addInventory(@RequestBody @Valid InventoryDTO inventoryDTO) {
        // 查询原料是否存在
        if(inventoryService.selectInventoryByName(inventoryDTO.getMaterial()) != null) {
            return Result.error("该原料已经存在");
        }
        // 封装Inventory 不设置id id自增
        Inventory inventory = new Inventory();
        inventory.setMaterial(inventoryDTO.getMaterial())
                .setStock(inventoryDTO.getStock())
                .setPictureUrl(defaultUrl);
        inventoryService.addInventory(inventory);
        return Result.success();
    }

    @PutMapping("updateInventory")
    @Operation(summary = "修改原料接口")
    public Result updateInventory(@RequestBody @Valid InventoryDTO inventoryDTO) {
        // 根据id查询Inventory
        Inventory inventory = inventoryService.selectInventoryById(inventoryDTO.getId());
        // 查询该原料是否存在
        if(inventory == null) return Result.error("该原料不存在");
        // 封装回inventory
        inventory.setMaterial(inventoryDTO.getMaterial())
                .setStock(inventoryDTO.getStock());
        // 修改原料信息
        inventoryService.updateInventory(inventory);
        return Result.success();
    }

    @DeleteMapping("deleteInventory")
    @Operation(summary = "删除原料接口")
    public Result deleteInventory(@RequestParam Integer inventoryId) {
        // 判断是否有该原料
        if(inventoryService.selectInventoryById(inventoryId) == null) return Result.error("该原料不存在");
        // 删除原料
        inventoryService.deleteInventory(inventoryId);
        return Result.success();
    }

    @PostMapping("upload")
    @Operation(summary = "修改原料图片接口")
    public Result updateInventoryPictureUrl(MultipartFile file, @RequestParam int inventoryId) {
        // 判断是否有该原料
        if(inventoryService.selectInventoryById(inventoryId) == null) return Result.error("该原料不存在");
        // 判断文件上传类型是否合法
        String contentType = file.getContentType();
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png") && !contentType.equals("image/jpg")){
            return Result.error("文件类型错误");
        }
        // 判断文件大小是否过大
        DataSize maxsize = DataSize.ofMegabytes(2);
        if(file.getSize() > maxsize.toBytes()) return Result.error("文件大小超过2MB");
        // 设置图片路径
        String fileName = path + "inventory_" + inventoryId + ".jpg";
        try {
            // 转存文件
            file.transferTo(new File(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 修改数据库文件地址
        inventoryService.updatePictureUrlById(inventoryId, fileName);
        return Result.success();
    }
}
