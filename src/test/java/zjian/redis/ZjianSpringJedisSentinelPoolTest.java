package zjian.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import zjian.redis.common.Constants;
import zjian.redis.service.ZjianSpringJedisSentinelPool;
import zjian.redis.service.ZjianSpringJedisSentinelPoolConfig;
import zjian.redis.service.ZjianSpringRedisPoolList;
import zjian.redis.service.ZjianSpringRedisPoolString;
import zjian.redis.utils.ZjianRedisLog;

@RunWith(SpringJUnit4ClassRunner.class)    
@ContextConfiguration(locations = { "classpath*:applicationContex*.xml"}) 
public class ZjianSpringJedisSentinelPoolTest {

	@Autowired
	private  ZjianSpringJedisSentinelPoolConfig bZjianSpringJedisSentinelPoolConfig;

	@Autowired
	private ZjianSpringJedisSentinelPool bZjianSpringJedisSentinelPool;

	@Autowired
	private ZjianSpringRedisPoolString bZjianSpringRedisPoolString;
	
	@Autowired
	private ZjianSpringRedisPoolList bZjianSpringRedisPoolList;
	
	@Test
	public void flushDBTest() throws InterruptedException {
		String fl = bZjianSpringJedisSentinelPool.flushDB(0);
		System.out.println(fl);
		System.out.println("----------");
	}
	
	
	@Test
	public void setZjianRedisCustomerTest() throws InterruptedException {
		long a = System.currentTimeMillis();
		long num = 0;
		JedisSentinelPool pool = bZjianSpringJedisSentinelPool.getJedisPool(0);
		Jedis jedis = pool.getResource();
		for (int i = 1; i < 100000; i++) {
			String returns = Constants.REDIS_ERROR;
			String key = i+"";
			jedis.set(key, key);
			ZjianRedisLog.getRedisLogInfo(i + "*************i",
					"setZjianRedisCustomer", "returns:" + returns);
		}
		pool.returnResource(jedis);
		jedis = null;
		pool.destroy();
		System.out.println("\r<br>执行耗时 : " + (System.currentTimeMillis() - a)
				/ 1000f + " 秒 ");
	}
	
	
	@Test
	public void bZjianSpringRedisPoolStringTest() throws InterruptedException {
		long a = System.currentTimeMillis();
		long num = 0;
		JedisSentinelPool pool = bZjianSpringJedisSentinelPool.getJedisPool(0);
		Jedis jedis = pool.getResource();
		for (int i = 1; i < 1000; i++) {
			String returns = Constants.REDIS_ERROR;
			String key = "c_"+i+"";
			bZjianSpringRedisPoolString.setJPKey(pool, jedis, key, key, 0, 0, "string");
			ZjianRedisLog.getRedisLogInfo(i + "*************i",
					"setZjianRedisCustomer", "returns:" + returns);
		}
		pool.returnResource(jedis);
		jedis = null;
		pool.destroy();
		System.out.println("\r<br>执行耗时 : " + (System.currentTimeMillis() - a)
				/ 1000f + " 秒 ");
	}
	
	
	@Test
	public void bZjianSpringRedisPoolListTest() throws InterruptedException {
		long a = System.currentTimeMillis();
		long num = 0;
		JedisSentinelPool pool = bZjianSpringJedisSentinelPool.getJedisPool(0);
		Jedis jedis = pool.getResource();
		for (int i = 1; i < 1000; i++) {
			String returns = Constants.REDIS_ERROR;
			String key = "l_"+i+"";
			bZjianSpringRedisPoolList.rpushPList(pool, jedis, key, key, 0, 0);
			ZjianRedisLog.getRedisLogInfo(i + "*************i",
					"setZjianRedisCustomer", "returns:" + returns);
		}
		pool.returnResource(jedis);
		jedis = null;
		pool.destroy();
		System.out.println("\r<br>执行耗时 : " + (System.currentTimeMillis() - a)
				/ 1000f + " 秒 ");
	}

}
