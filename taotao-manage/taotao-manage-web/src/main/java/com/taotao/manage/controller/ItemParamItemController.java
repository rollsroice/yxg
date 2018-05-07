package com.taotao.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.manage.pojo.ItemParamItem;
import com.taotao.manage.service.ItemParamItemService;

/**
 * 商品规格参数 数据
 */
@RequestMapping(value = "item/param/item")
@Controller
public class ItemParamItemController {

    @Autowired
    private ItemParamItemService itemParamItemService;

    /**
     * 通过商品id查询商品规格参数数据
     * 
     * @param itemId
     * @return
     */
    @RequestMapping(value = "{itemId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ItemParamItem> queryItemParamItem(@PathVariable("itemId") Long itemId) {
        ItemParamItem param = new ItemParamItem();
        param.setItemId(itemId);
        List<ItemParamItem> itemParamItems = this.itemParamItemService.queryList(param);
        if(!itemParamItems.isEmpty()){
            return ResponseEntity.ok(itemParamItems.get(0));
        }
        return ResponseEntity.ok(null);
    }

}
