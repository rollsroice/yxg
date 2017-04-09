package cn.itcast.crawler;

import java.io.File;
import java.util.Map;

import cn.itcast.crawler.service.HttpService;
import cn.itcast.crawler.service.PropertieService;

/**
 * 图片下载
 * 
 * @author 传智播客 张志君
 * 
 * @date 2014年12月19日 下午4:30:21
 * 
 * @version V1.0
 */
public class ImageDownloadCrawler implements Crawler {

	/**
	 * key：原始图片地址 value:新图片地址
	 */
	private Map<String, String> urlMapping;

	public ImageDownloadCrawler(Map<String, String> urlMapping) {
		this.urlMapping = urlMapping;
	}

	@Override
	public void run() {
		if (this.urlMapping == null || this.urlMapping.isEmpty()) {
			return;
		}
		HttpService httpService = Main.applicationContext.getBean(HttpService.class);
		PropertieService propertieService = Main.applicationContext.getBean(PropertieService.class);
		for (Map.Entry<String, String> entry : this.urlMapping.entrySet()) {
			try {
				httpService.downloadFile(entry.getKey(), new File(propertieService.IMAGE_DIR + entry.getValue()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
