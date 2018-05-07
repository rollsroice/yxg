package com.taotao.manage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.service.ItemService;

@RequestMapping("item")
@Controller
public class ItemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    /**
     * 提供了REST接口，分页查询商品数据，商品数据是按照更新时间做倒序排序
     * 
     * @param page 当前页
     * @param rows 页面大小
     * @return 返回http状态为200时包含EasyUIResult（按照EasyUI的结构封装的对象）的数据，否则返回500状态
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    // 将EasyUIResult序列化为json数据返回
    public ResponseEntity<EasyUIResult> queryItemList(@RequestParam("page") Integer page,
            @RequestParam("rows") Integer rows) {
        try {
            if (LOGGER.isDebugEnabled()) {
                // 输出入参参数
                LOGGER.debug("分页查询商品数据，page = {}, rows = {}", page, rows);
            }
            // 通过分页参数查询数据，queryItemList扩展出的方法
            PageInfo<Item> pageInfo = this.itemService.queryItemList(page, rows);
            if (LOGGER.isDebugEnabled()) {
                // 输出入参参数
                LOGGER.debug("分页查询商品数据，pageInfo = {}", pageInfo);
            }

            EasyUIResult easyUIResult = new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
            // 200
            return ResponseEntity.ok(easyUIResult);
        } catch (Exception e) {
            LOGGER.error("分页查询商品出错!", e);
            // 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * 新增商品
     * 
     * @param item
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> saveItem(Item item, @RequestParam("desc") String desc,
            @RequestParam("itemParams") String itemParams) {
        try {
            Integer count = this.itemService.saveItem(item, desc, itemParams);
            if (count != 1) {
                // 500
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
            // 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.ok(null);
    }

    /**
     * 修改商品
     * 
     * @param item
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateItem(Item item, @RequestParam("desc") String desc) {
        try {
            Integer count = this.itemService.updateItem(item, desc);
            if (count != 1) {
                // 500
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
            // 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.ok(null);
    }

    /**
     * 根据ID查询商品
     * 
     * @param item
     * @return
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Item> queryItemById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(this.itemService.queryById(id));
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
            // 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
