package cn.itcast.crawler.service;

import org.springframework.stereotype.Service;

import cn.itcast.crawler.spring.PropertyConfig;

@Service
public class PropertieService {
	
	@PropertyConfig
	public String IMAGE_DIR;
	
	@PropertyConfig
	public String MAX_POOL_SIZE;

}
