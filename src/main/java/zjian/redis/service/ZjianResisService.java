package zjian.redis.service;

import java.util.Map;

import zjian.redis.model.ZjianRedisCustomer;
import zjian.redis.model.ZjianRedisService;

public interface  ZjianResisService {

	
//	1、	座席接口： string setZjianRedisService(ZjianRedisService)
//	传入：座席信息（座席ID，最大接待人数，己接待人数，服务器IP，状态，创建时间）
//	传出：key
//	2、	客户接口： string setZjianRedisCustomer(ZjianRedisCustomer)
//	传入：客户信息（客户ID，状态，来源，咨询分类，心跳时间， 创建时间）
//	传出：key
//	3、	客户心跳接口：  string setZjianRedisCustomerHeartbeat
//	传入：客户ID，心跳时间
//	传出：key
//	4、	客户-座席接口：string getZjianRedisService(String sZjianRedisCustomerKey)
//	传入：客户ID
//	传出：座席ID
//	5、	座席-客户list接口：List getZjianRedisCustomerList(String sZjianRedisServiceKey)
//	传入：座席ID
//	传出：客户list（一对多）

	
	/**
	 * 座席接口
	 * param 座席信息
	 * @return key
	 */
	public Map setZjianRedisService(ZjianRedisService pZjianRedisService);
	
	public Map setZjianRedisCustomer(ZjianRedisCustomer pZjianRedisCustomer);
	
	public Map setZjianRedisCustomerHeartbeat(String sZjianRedisCustomerKey,int heartbeatTime);
	
	public Map getZjianRedisService(String sZjianRedisCustomerKey);
	
	public Map getZjianRedisCustomerList(String sZjianRedisServiceKey);
	
	public Map getZjianRedisCustomer(String sZjianRedisServiceKey);
	
	
	
}
