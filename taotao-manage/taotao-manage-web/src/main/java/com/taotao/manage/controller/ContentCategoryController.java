package com.taotao.manage.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.manage.pojo.ContentCategory;
import com.taotao.manage.service.ContentCategoryService;

@RequestMapping("content/category")
@Controller
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<ContentCategory> queryContentCategory(
            @RequestParam(value = "id", defaultValue = "0") Long parentId) {
        ContentCategory contentCategory = new ContentCategory();
        contentCategory.setParentId(parentId);
        return this.contentCategoryService.queryList(contentCategory);
    }

    /**
     * 新增
     * 
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ContentCategory> saveContentCategory(ContentCategory contentCategory) {
        try {
            // 将contentCategory数据保存到数据库中
            contentCategory.setCreated(new Date());
            contentCategory.setUpdated(contentCategory.getCreated());
            contentCategory.setIsParent(false);
            contentCategory.setSortOrder(1);
            contentCategory.setStatus(1);
            this.contentCategoryService.save(contentCategory);

            // 判断该节点的父节点的isParent是否为true，如果不是，改为true
            ContentCategory parent = this.contentCategoryService.queryById(contentCategory.getParentId());
            if (!parent.getIsParent()) {
                parent.setIsParent(true);
                parent.setUpdated(new Date());
                this.contentCategoryService.updateSelective(parent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(contentCategory);
    }

    /**
     * 重命名
     * 
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> updateContentCategory(ContentCategory contentCategory) {
        try {
            // 将contentCategory数据保存到数据库中
            contentCategory.setUpdated(new Date());
            this.contentCategoryService.updateSelective(contentCategory);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.ok(null);
    }

    /**
     * 删除
     * 
     * @return
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteContentCategory(@RequestParam("parentId") Long parentId,
            @RequestParam("id") Long id) {
        // 待删除的数据id
        List<Long> ids = new ArrayList<Long>();
        // 删除当前id的节点
        ids.add(id);
        // 级联删除
        getAllCategory(ids, id);
        //执行删除
        this.contentCategoryService.deleteByIds(ids.toArray(new Long[]{}));

        // 判断该节点的父节点是否存在其他的子節點，如果不存在，设置isParent为false
        ids.clear();
        getAllCategory(ids, parentId);
        if(ids.isEmpty()){
            ContentCategory parent = new ContentCategory();
            parent.setIsParent(false);
            parent.setUpdated(new Date());
            parent.setId(parentId);
            this.contentCategoryService.updateSelective(parent);
        }
        
        return ResponseEntity.ok(null);
    }

    /**
     * 递归查找所有子节点
     * 
     * @param ids
     * @param parentId
     */
    private void getAllCategory(List<Long> ids, Long parentId) {
        ContentCategory param = new ContentCategory();
        param.setParentId(parentId);
        List<ContentCategory> contentCategories = this.contentCategoryService.queryList(param);
        if (contentCategories != null && !contentCategories.isEmpty()) {
            for (ContentCategory contentCategory : contentCategories) {
                ids.add(contentCategory.getId());
                getAllCategory(ids, contentCategory.getId());
            }
        }
    }

}
