package com.holy.controller;

import cn.hutool.core.io.unit.DataSize;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.holy.AliOSSUtil;
import com.holy.domain.dto.InventoryDTO;
import com.holy.domain.po.Inventory;
import com.holy.domain.po.Result;
import com.holy.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.holy.AliOSSUtil.DEFAULT_URL;

@Tag(name = "原料相关接口")
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/list")
    @Operation(summary = "分页查询原料列表接口")
    public Result<IPage<Inventory>> list (
            Integer pageNum, Integer pageSize,
            @RequestParam(required = false) String material){
        // 调用原料服务的分页查询
        IPage<Inventory> iPage = inventoryService.list(pageNum, pageSize, material);
        return Result.success(iPage);
    }

    @PostMapping("/addInventory")
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
                .setPictureUrl(DEFAULT_URL);
        inventoryService.addInventory(inventory);
        return Result.success(null,"原料新增成功");
    }

    @PutMapping("/updateInventory")
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
        return Result.success(null,"原料修改成功");
    }

    @DeleteMapping("/deleteInventory")
    @Operation(summary = "删除原料接口")
    public Result deleteInventory(@RequestParam Integer inventoryId) {
        // 判断是否有该原料
        if(inventoryService.selectInventoryById(inventoryId) == null) return Result.error("该原料不存在");
        // 删除原料
        inventoryService.deleteInventory(inventoryId);
        return Result.success(null, "原料删除成功");
    }

    @PostMapping("/upload")
    @Operation(summary = "修改原料图片接口")
    public Result updateInventoryPictureUrl(@RequestParam Integer id, MultipartFile file) {
        // 判断是否有该原料
        if(inventoryService.selectInventoryById(id) == null) return Result.error("该原料不存在");
        // 判断文件上传类型是否合法
        String contentType = file.getContentType();
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png") && !contentType.equals("image/jpg")){
            return Result.error("文件类型错误");
        }
        // 判断文件大小是否过大
        DataSize maxsize = DataSize.ofMegabytes(2);
        if(file.getSize() > maxsize.toBytes()) return Result.error("文件大小超过2MB");
        // 重命名文件
        String fileName = "inventory_" + id + file.getOriginalFilename().substring(
                file.getOriginalFilename().lastIndexOf("."));
        String url = "";
        try {
            // 转存文件到AliOSS云服务器
            url = AliOSSUtil.uploadFile(fileName,file.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 修改数据库文件地址
        inventoryService.updatePictureUrlById(id, url);
        return Result.success(url,"图片上传成功");
    }
}
