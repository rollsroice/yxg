package com.taotao.manage.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.taotao.manage.pojo.ItemDesc;

@Service
public class ItemDescService extends BaseService<ItemDesc> {

    public Integer saveItemDesc(Long id, String desc) {
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(id);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(itemDesc.getCreated());
        return super.save(itemDesc);
    }

    public Integer updateItemDesc(Long id, String desc) {
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(id);
        itemDesc.setItemDesc(desc);
        itemDesc.setUpdated(new Date());
        return super.updateSelective(itemDesc);
    }

}
