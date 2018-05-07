package com.taotao.manage.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.mapper.ItemMapper;
import com.taotao.manage.pojo.Item;
import com.taotao.manage.pojo.ItemParamItem;

@Service
public class ItemService extends BaseService<Item> {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemDescService itemDescService;

    @Autowired
    private ItemParamItemService itemParamItemService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public PageInfo<Item> queryItemList(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        return new PageInfo<Item>(this.itemMapper.queryItemListOrderByUpdated());
    }

    public Integer saveItem(Item item, String desc, String itemParams) {
        // 设置初始化数据
        item.setCreated(new Date());
        item.setUpdated(item.getCreated());
        item.setStatus(1);

        // 强制设置id为null，安全角度
        item.setId(null);
        Integer count = 0;
        count = super.save(item);

        // 完成商品描述的新增
        count = this.itemDescService.saveItemDesc(item.getId(), desc);

        // 保存商品规格参数数据
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setCreated(new Date());
        itemParamItem.setUpdated(itemParamItem.getCreated());
        itemParamItem.setItemId(item.getId());
        itemParamItem.setParamData(itemParams);
        count = this.itemParamItemService.save(itemParamItem);

        // 发送RabbitMQ消息
        sendMsg("insert", item.getId());
        return count;
    }

    private void sendMsg(String type, Long id) {
        Map<String, Object> msg = new HashMap<String, Object>();
        msg.put("id", id);
        msg.put("type", type);
        try {
            this.rabbitTemplate.convertAndSend("item." + type, MAPPER.writeValueAsString(msg));
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
        }
    }

    public Integer updateItem(Item item, String desc) {
        // 数据限制
        item.setCreated(null);

        // 更新数据
        item.setUpdated(new Date());

        Integer count = 0;
        count = super.updateSelective(item);

        // 更新ItemDesc数据
        count = this.itemDescService.updateItemDesc(item.getId(), desc);

        // 发送RabbitMQ消息
        sendMsg("update", item.getId());

        return count;
    }

}
