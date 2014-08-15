package zjian.redis.common;

public class Constants {

	public static final String REDIS_ERROR = "0";  //接口map返回标志位：失败
	public static final String REDIS_SUCCESS = "1"; //接口map返回标志位：成功
	public static final String REDIS_CUSTOMER_KEYS = "customers"; // key：保存客户对象
	public static final String REDIS_CUSTOMER_KEY = "customer"; // key：保存客户对象key
	public static final String REDIS_SERVICE_KEYS = "services"; // key：保存坐席对象
	public static final String REDIS_SERVICE_KEY = "service"; // key：保存坐席对象key
	public static final String REDIS_CUSTOMER_KEYS_POP = "customers_pop"; // key：保存客户对象(已经pop)
	public static final String REDIS_CUSTOMER_KEY_POP = "customer_pop"; // key：保存客户对象key(已经pop)
	public static final String REDIS_SERVICE_KEYS_LIST = "services_list"; // key(坐席ID+KEY)：保存坐席-客户对象列表
	public static final String REDIS_SERVICE_KEY_LIST = "service_pop"; // key(坐席ID+KEY)：保存坐席-客户对象key列表
	public static final int REDIS_DATABASE = 0;  //默认库
	public static final int REDIS_DATABASE_POP = 1; //pop库 未用
	public static final int REDIS_EXPIRE = 7 * 24 * 60 * 60; // key的有效时间 7天
	public static final int REDIS_TIMEOUT = 100000; // JedisPool超时时间
	
	public static final String MASTER_NAME = "mymaster";   //redis服务器的配置 要配合服务器的redis.conf修改
}
