package zjian.redis.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ZjianRedisCustomer implements Serializable{

	
	//客户ID，状态，来源，咨询分类，心跳时间， 创建时间
	
	private String id;
	
	private String source;
	
	private String type;
	
	private int heartbeatTime = 0;
	
	private int status;
	
	private String createDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getHeartbeatTime() {
		return heartbeatTime;
	}

	public void setHeartbeatTime(int heartbeatTime) {
		this.heartbeatTime = heartbeatTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	@Override
	public String toString() {
		return "ZjianRedisCustomer [id=" + id + ", source=" + source
				+ ", type=" + type + ", heartbeatTime=" + heartbeatTime
				+ ", status=" + status + ", createDate=" + createDate + "]";
	}
	
	
	
	
}
