package zjian.redis.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ZjianRedisProperties{
	private static Properties config = null;
	private static String redis_master_ip;
	private static String redis_master_pwd;
	private static String redis_cluster_ip;
	private static String redis_cluster_pwd;
	private static String redis_sentinel_ip;
	
	private static String redis_pool_maxActive;
	private static String redis_pool_maxIdle;
	private static String redis_pool_maxWait;
	private static String redis_pool_testOnBorrow;

	static {
		try {
			InputStream in = ZjianRedisProperties.class.getClassLoader()
					.getResourceAsStream("zjian_redis.properties");
			config = new Properties();
			config.load(in);

			redis_master_ip = config.getProperty("redis_master_ip");
			redis_master_pwd = config.getProperty("redis_master_pwd");
			redis_cluster_ip = config.getProperty("redis_cluster_ip");
			redis_cluster_pwd = config.getProperty("redis_cluster_pwd");
			redis_sentinel_ip = config.getProperty("redis_sentinel_ip");
			
			redis_pool_maxActive = config.getProperty("redis_pool_maxActive");
			redis_pool_maxIdle = config.getProperty("redis_pool_maxIdle");
			redis_pool_maxWait = config.getProperty("redis_pool_maxWait");
			redis_pool_testOnBorrow = config.getProperty("redis_pool_testOnBorrow");
			

			in.close();
		} catch (IOException e) {
			System.out.println("No show.properties  error");
		}

	}

	public static String getRedis_master_ip() {
		return redis_master_ip;
	}

	public static void setRedis_master_ip(String redis_master_ip) {
		ZjianRedisProperties.redis_master_ip = redis_master_ip;
	}

	public static String getRedis_master_pwd() {
		return redis_master_pwd;
	}

	public static void setRedis_master_pwd(String redis_master_pwd) {
		ZjianRedisProperties.redis_master_pwd = redis_master_pwd;
	}

	public static String getRedis_cluster_ip() {
		return redis_cluster_ip;
	}

	public static void setRedis_cluster_ip(String redis_cluster_ip) {
		ZjianRedisProperties.redis_cluster_ip = redis_cluster_ip;
	}

	public static String getRedis_cluster_pwd() {
		return redis_cluster_pwd;
	}

	public static void setRedis_cluster_pwd(String redis_cluster_pwd) {
		ZjianRedisProperties.redis_cluster_pwd = redis_cluster_pwd;
	}

	public static String getRedis_sentinel_ip() {
		return redis_sentinel_ip;
	}

	public static void setRedis_sentinel_ip(String redis_sentinel_ip) {
		ZjianRedisProperties.redis_sentinel_ip = redis_sentinel_ip;
	}

	public static String getRedis_pool_maxActive() {
		return redis_pool_maxActive;
	}

	public static void setRedis_pool_maxActive(String redis_pool_maxActive) {
		ZjianRedisProperties.redis_pool_maxActive = redis_pool_maxActive;
	}

	public static String getRedis_pool_maxIdle() {
		return redis_pool_maxIdle;
	}

	public static void setRedis_pool_maxIdle(String redis_pool_maxIdle) {
		ZjianRedisProperties.redis_pool_maxIdle = redis_pool_maxIdle;
	}

	public static String getRedis_pool_maxWait() {
		return redis_pool_maxWait;
	}

	public static void setRedis_pool_maxWait(String redis_pool_maxWait) {
		ZjianRedisProperties.redis_pool_maxWait = redis_pool_maxWait;
	}

	public static String getRedis_pool_testOnBorrow() {
		return redis_pool_testOnBorrow;
	}

	public static void setRedis_pool_testOnBorrow(String redis_pool_testOnBorrow) {
		ZjianRedisProperties.redis_pool_testOnBorrow = redis_pool_testOnBorrow;
	}

}
