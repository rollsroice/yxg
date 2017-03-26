package cn.itcast.solrj.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;

import cn.itcast.solrj.pojo.Foo;

public class SolrjService {

	// 定义http的solr服务
	private HttpSolrServer httpSolrServer;

	public SolrjService(HttpSolrServer httpSolrServer) {
		this.httpSolrServer = httpSolrServer;
	}

	/**
	 * 新增数据到solr服务 this.httpSolrServer = httpSolrServer; solrjService = new
	 * SolrjService(httpSolrServer);
	 * 
	 * @param foo
	 * @throws Exception
	 */
	public void add(Foo foo) throws Exception {
		this.httpSolrServer.addBean(foo); // 添加数据到solr服务器
		this.httpSolrServer.commit(); // 提交
	}

	public void delete(List<String> ids) throws Exception {
		this.httpSolrServer.deleteById(ids);
		this.httpSolrServer.commit(); // 提交
	}

	public List<Foo> search(String keywords, Integer page, Integer rows) throws Exception {
		SolrQuery solrQuery = new SolrQuery(); // 构造搜索条件
		solrQuery.setQuery("title:" + keywords); // 搜索关键词

		// 自己写一个 传入第一页 （1-1)*10 start 从0开始
		// 传入第二页2-1=1 1*10 从10开始 返回10条记录
		// 传入第三页3-1=2 2*10 从20开始 返回10条记录
		solrQuery.setStart((Math.max(page, 1) - 1) * rows);
		solrQuery.setRows(rows);

		// 是否需要高亮
		boolean isHighlighting = !StringUtils.equals("*", keywords)
				&& StringUtils.isNotEmpty(keywords);

		if (isHighlighting) {
			// 设置高亮
			solrQuery.setHighlight(true); // 开启高亮组件
			solrQuery.addHighlightField("title");// 高亮字段
			solrQuery.setHighlightSimplePre("<em>");// 标记，高亮关键字前缀
			solrQuery.setHighlightSimplePost("</em>");// 后缀
		}

		// 执行查询
		QueryResponse queryResponse = this.httpSolrServer.query(solrQuery);
		List<Foo> foos = queryResponse.getBeans(Foo.class);
		if (isHighlighting) {
			// 将高亮的标题数据写回到数据对象中
			Map<String, Map<String, List<String>>> map = queryResponse.getHighlighting();
			for (Map.Entry<String, Map<String, List<String>>> highlighting : map
					.entrySet()) {
				System.out
						.println(highlighting.getKey() + "==" + highlighting.getValue());
				for (Foo foo : foos) {

					if (!highlighting.getKey().equals(foo.getId().toString())) {
						continue;
					}
					foo.setTitle(StringUtils.join(highlighting.getValue().get("title"),
							""));
					break;
				}
			}
		}

		return foos;
	}

}