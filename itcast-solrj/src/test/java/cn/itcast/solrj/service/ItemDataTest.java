package cn.itcast.solrj.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.junit.Before;
import org.junit.Test;

import cn.itcast.solrj.pojo.EasyUIResult;
import cn.itcast.solrj.pojo.Item;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ItemDataTest {

	private HttpSolrServer httpSolrServer;

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Before
	public void setUp() throws Exception {
		// 在url中指定core名称：taotao 测试yxg
		String url = "http://solr.yxg.com/yxg";
		HttpSolrServer httpSolrServer = new HttpSolrServer(url); // 定义solr的server
		httpSolrServer.setParser(new XMLResponseParser()); // 设置响应解析器
		httpSolrServer.setMaxRetries(1); // 设置重试次数，推荐设置为1
		httpSolrServer.setConnectionTimeout(500); // 建立连接的最长时间
		this.httpSolrServer = httpSolrServer;
	}

	@Test
	public void testUpdate() throws Exception {
		String jsonData = doGet("http://manage.taotao.com/rest/item/1188043");
		Item item = MAPPER.readValue(jsonData, Item.class);
		item.setStatus(2);

		httpSolrServer.addBean(item);
		httpSolrServer.commit();
	}

	@Test
	public void testData() throws Exception {
		// 从接口获取数据
		String url = "http://localhost:8081/rest/item?page={page}&rows=500";
		Integer page = 1;
		Integer pageSize = 500;
		do {
			String jsonData = doGet(StringUtils.replace(url, "{page}", page + ""));
			EasyUIResult easyUIResult = EasyUIResult.formatToList(jsonData, Item.class);
			List<Item> items = (List<Item>) easyUIResult.getRows();
			this.httpSolrServer.addBeans(items);
			this.httpSolrServer.commit();

			pageSize = items.size();
			page++;
		} while (pageSize == 500);
	}

	private String doGet(String url) throws Exception {
		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();

		// 创建http GET请求
		HttpGet httpGet = new HttpGet(url);

		CloseableHttpResponse response = null;
		try {
			// 执行请求
			response = httpclient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} finally {
			if (response != null) {
				response.close();
			}
			// 相当于关闭浏览器
			httpclient.close();
		}
		return null;
	}

}
