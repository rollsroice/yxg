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
import org.junit.Test;

import cn.itcast.solrj.pojo.EasyUIResult;
import cn.itcast.solrj.pojo.Item;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ItemDateImport {

	String baseUrl = "http://yxg.manage.com/rest/item?page={page}&rows=100";
	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Test
	public void handerData() {
		String solrurl = "http://solr.yxg.com/yxg";
		HttpSolrServer httpSolrServer = new HttpSolrServer(solrurl); // 定义solr的server
		httpSolrServer.setParser(new XMLResponseParser()); // 设置响应解析器
		httpSolrServer.setMaxRetries(1); // 设置重试次数，推荐设置为1
		httpSolrServer.setConnectionTimeout(500); // 建立连接的最长时间

		Integer page = 1;
		Integer pageSize = 100;
		do {
			String url = StringUtils.replace(baseUrl, "{page}", page + "");
			String jsonDate;
			try {
				jsonDate = doGet(url);
				EasyUIResult easyUIResult = formatToList(jsonDate, Item.class);
				List<Item> items = (List<Item>) easyUIResult.getRows();
				pageSize = items.size();
				page++;
				httpSolrServer.addBeans(items);
				httpSolrServer.commit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} while (pageSize == 100);
	}

	public static EasyUIResult formatToList(String jsonData, Class<?> clazz) {
		try {
			JsonNode jsonNode = MAPPER.readTree(jsonData);
			JsonNode data = jsonNode.get("rows");
			List<?> list = null;
			if (data.isArray() && data.size() > 0) {
				list = MAPPER.readValue(data.traverse(), MAPPER.getTypeFactory()
						.constructCollectionType(List.class, clazz));
			}
			return new EasyUIResult(jsonNode.get("total").longValue(), list);
		} catch (Exception e) {
			return null;
		}
	}

	public String doGet(String url) throws Exception {
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
				String content = EntityUtils.toString(response.getEntity(), "UTF-8");
				return content;
			}
			return null;
		} finally {
			if (response != null) {
				response.close();
			}
			// 相当于关闭浏览器
			httpclient.close();
		}

	}
}
