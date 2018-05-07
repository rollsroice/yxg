package com.taotao.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manage.mapper.ContentMapper;
import com.taotao.manage.pojo.Content;

@Service
public class ContentService extends BaseService<Content>{
    
    @Autowired
    private ContentMapper contentMapper;

    public PageInfo<Content> queryContentList(Long categoryId, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        List<Content> list = this.contentMapper.queryContentList(categoryId);
        return new PageInfo<Content>(list);
    }

}
