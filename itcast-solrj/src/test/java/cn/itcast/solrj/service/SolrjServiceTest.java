package cn.itcast.solrj.service;

import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.junit.Before;
import org.junit.Test;

import cn.itcast.solrj.pojo.Foo;

public class SolrjServiceTest {

	private SolrjService solrjService;

	private HttpSolrServer httpSolrServer;

	@Before
	public void setUp() throws Exception {
		// 在url中指定core名称：taotao
		String url = "http://solr.yxg.com/yxg";
		HttpSolrServer httpSolrServer = new HttpSolrServer(url); // 定义solr的server
		httpSolrServer.setParser(new XMLResponseParser()); // 设置响应解析器
		httpSolrServer.setMaxRetries(1); // 设置重试次数，推荐设置为1
		httpSolrServer.setConnectionTimeout(500); // 建立连接的最长时间

		this.httpSolrServer = httpSolrServer;
		solrjService = new SolrjService(httpSolrServer);
	}

	@Test
	public void testAdd() throws Exception {
		Foo foo = new Foo();
		/*
		 * foo.setId(String.valueOf(System.currentTimeMillis()));
		 * foo.setTitle("乐视双卡双待");
		 */

		foo.setId("1473746553253");
		foo.setTitle("好电视");

		this.solrjService.add(foo);

	}

	@Test
	public void testDelete() throws Exception {
		this.solrjService.delete(Arrays.asList("1473731722322"));
	}

	@Test
	public void testSearch() throws Exception {
		List<Foo> foos = this.solrjService.search("乐视", 1, 10);
		for (Foo foo : foos) {
			System.out.println(foo);
		}
	}

	@Test
	public void testDeleteByQuery() throws Exception {
		httpSolrServer.deleteByQuery("*:*");
		httpSolrServer.commit();
	}

}
