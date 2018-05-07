package com.taotao.manage.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.manage.pojo.ItemParam;
import com.taotao.manage.service.ItemParamService;

@RequestMapping("item/param")
@Controller
public class ItemParamController {

    @Autowired
    private ItemParamService itemParamService;

    /**
     * 根据商品类目id查找模板
     * 
     * @param itemCatId
     * @return 如果查到返回实体数据，否则返回null
     */
    @RequestMapping(value = "{itemCatId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ItemParam> queryItemParamByItemCatId(@PathVariable("itemCatId") Long itemCatId) {
        ItemParam queryParam = new ItemParam();
        queryParam.setItemCatId(itemCatId);
        List<ItemParam> itemParams = this.itemParamService.queryList(queryParam);
        if (itemParams == null || itemParams.isEmpty()) {
            return ResponseEntity.ok(null);
        }
        return ResponseEntity.ok(itemParams.get(0));
    }
    
    /**
     * 新增模板
     * 
     * @param itemCatId
     * @param paramData
     * @return
     */
    @RequestMapping(value = "{itemCatId}", method = RequestMethod.POST)
    public ResponseEntity<Void> saveItemParam(@PathVariable("itemCatId") Long itemCatId,@RequestParam("paramData")String paramData){
        try {
            ItemParam itemParam = new ItemParam();
            itemParam.setCreated(new Date());
            itemParam.setUpdated(itemParam.getCreated());
            itemParam.setItemCatId(itemCatId);
            itemParam.setParamData(paramData);
            this.itemParamService.save(itemParam);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
