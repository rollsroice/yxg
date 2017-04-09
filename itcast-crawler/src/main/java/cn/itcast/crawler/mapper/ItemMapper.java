package cn.itcast.crawler.mapper;

import java.util.Collection;

import org.apache.ibatis.annotations.Param;

import cn.itcast.crawler.pojo.Item;

public interface ItemMapper {

	/**
	 * 新增商品
	 * 
	 * @param item
	 *            商品对象
	 * @return
	 */
	public Long saveItems(@Param("items") Collection<Item> items);
	
}
