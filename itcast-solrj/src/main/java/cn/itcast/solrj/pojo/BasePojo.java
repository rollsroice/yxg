package cn.itcast.solrj.pojo;

import java.util.Date;

//增加第一次111密码23.27
public abstract class BasePojo {

	private Date created;
	private Date updated;

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

}
