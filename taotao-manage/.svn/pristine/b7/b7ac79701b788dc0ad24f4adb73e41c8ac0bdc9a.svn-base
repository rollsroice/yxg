package com.taotao.manage.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.taotao.common.bean.EasyUIResult;
import com.taotao.common.service.ApiService;
import com.taotao.manage.pojo.Content;
import com.taotao.manage.service.ContentService;

@RequestMapping("content")
@Controller
public class ContentController {

    @Autowired
    private ContentService contentService;
    
    @Autowired
    private ApiService apiService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<EasyUIResult> queryContent(@RequestParam("categoryId") Long categoryId,
            @RequestParam("page") Integer page, @RequestParam("rows") Integer rows) {
        PageInfo<Content> pageInfo = this.contentService.queryContentList(categoryId, page, rows);
        EasyUIResult easyUIResult = new EasyUIResult(pageInfo.getTotal(), pageInfo.getList());
        return ResponseEntity.ok(easyUIResult);
    }

    /**
     * 新增内容
     * @param content
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> queryContent(Content content) {
        content.setCreated(new Date());
        content.setUpdated(content.getCreated());
        this.contentService.save(content);
        
        //通知前台系统更新，大广告位数据
        if(content.getCategoryId().intValue() == 11){
            String url = "http://www.taotao.com/redis/index/big/ad.html";
            try {
                this.apiService.doGet(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return ResponseEntity.ok(null);
    }

}
