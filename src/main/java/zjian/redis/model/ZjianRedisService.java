package zjian.redis.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ZjianRedisService implements Serializable{
	//座席ID，最大接待人数，己接待人数，服务器IP，状态，创建时间
	
	private String id;
	
	private int maxReceiveNum;
	
	private int receiveNum;
	
	private String serviceIp;
	
	private int status;
	
	private String createDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getMaxReceiveNum() {
		return maxReceiveNum;
	}

	public void setMaxReceiveNum(int maxReceiveNum) {
		this.maxReceiveNum = maxReceiveNum;
	}

	public int getReceiveNum() {
		return receiveNum;
	}

	public void setReceiveNum(int receiveNum) {
		this.receiveNum = receiveNum;
	}

	public String getServiceIp() {
		return serviceIp;
	}

	public void setServiceIp(String serviceIp) {
		this.serviceIp = serviceIp;
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
		return "ZjianRedisService [id=" + id + ", maxReceiveNum="
				+ maxReceiveNum + ", receiveNum=" + receiveNum + ", serviceIp="
				+ serviceIp + ", status=" + status + ", createDate="
				+ createDate + "]";
	}
	
	
}
