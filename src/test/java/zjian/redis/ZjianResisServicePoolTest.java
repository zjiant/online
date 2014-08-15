package zjian.redis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import zjian.redis.common.Constants;
import zjian.redis.model.ZjianRedisCustomer;
import zjian.redis.model.ZjianRedisService;
import zjian.redis.service.ZjianResisService;
import zjian.redis.service.impl.ZjianResisServicePoolImpl;
import zjian.redis.utils.ZjianJedisSentinelPool;
import zjian.redis.utils.ZjianRedisLog;
import zjian.redis.utils.ZjianRedisPoolList;
import zjian.redis.utils.ZjianRedisPoolString;

public class ZjianResisServicePoolTest {

	private static ZjianResisService zrs = new ZjianResisServicePoolImpl();

	@Test
	public void flushDBTest() throws InterruptedException {
		ZjianJedisSentinelPool zjsp = ZjianJedisSentinelPool.getInstance();
		String fl = zjsp.flushDB(0);
		System.out.println(fl);
	}

	@Test
	public void setZjianRedisCustomerTest() throws InterruptedException {
		long a = System.currentTimeMillis();
		long num = 0;
		ZjianJedisSentinelPool zjsp = ZjianJedisSentinelPool.getInstance();
		ZjianRedisPoolList zrpl = ZjianRedisPoolList.getInstance();
		ZjianRedisPoolString zrps = ZjianRedisPoolString.getInstance();
		JedisSentinelPool pool = zjsp.getJedisPool(Constants.REDIS_DATABASE);
		Jedis jedis = pool.getResource();
		for (int i = 1; i < 100000; i++) {
			String returns = Constants.REDIS_ERROR;
			String key = i+"";
			ZjianRedisCustomer zrc = new ZjianRedisCustomer();
			zrc.setId("c"+i);
			zrc.setCreateDate("2014-7-30");
			zrc.setHeartbeatTime(1);
			zrc.setSource("1");
			zrc.setStatus(10);
			zrc.setType("1");
			String returns_c = zrps.setPKeys(pool,jedis,key, zrc,
					Constants.REDIS_DATABASE, Constants.REDIS_EXPIRE);
			if (Constants.REDIS_SUCCESS.equalsIgnoreCase(returns_c)) {
				num = zrpl.rpushPList(pool,jedis,Constants.REDIS_CUSTOMER_KEY, key,
						Constants.REDIS_DATABASE, Constants.REDIS_EXPIRE);
//				num = zrpl.rpushPLists(pool,jedis,Constants.REDIS_CUSTOMER_KEYS,
//						zrc, Constants.REDIS_DATABASE,
//						Constants.REDIS_EXPIRE);
				if (num > 0) {
					returns = Constants.REDIS_SUCCESS;
				}

			}
			HashMap map = new HashMap();
			map.put("key", key);
			map.put("num", num);
			map.put("returns", returns);
//			String key = "c" + i;
//			try {
//				Thread t = new tZjianRedisCustomerThread(i, jedis,pool);
//				t.start();
//				//jedis.set("" + key, "" + key);
//			} catch (Exception e) {
//
//			}
//			ZjianRedisCustomer zrc = new ZjianRedisCustomer();
//			zrc.setId("c"+i);
//			zrc.setCreateDate("2014-7-30");
//			zrc.setHeartbeatTime(1);
//			zrc.setSource("1");
//			zrc.setStatus(10);
//			zrc.setType("1");
//			Map map = zrs.setZjianRedisCustomer(zrc);
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
	public void setZjianRedisServiceTest() throws InterruptedException {

		for (int i = 1; i < 10; i++) {
			ZjianRedisService zrse = new ZjianRedisService();
			zrse.setId("s" + i);
			zrse.setCreateDate("2014-7-30");
			zrse.setMaxReceiveNum(100);
			zrse.setReceiveNum(10);
			zrse.setServiceIp("192.168.1.99");
			zrse.setStatus(10);
			Map map = zrs.setZjianRedisService(zrse);
			String key = (String) map.get("key");
			String returns = (String) map.get("returns");
			System.out.println(key);
			System.out.println(returns);
		}
	}

	@Test
	public void setZjianRedisCustomerHeartbeatTest()
			throws InterruptedException {

		Map map = zrs.setZjianRedisCustomerHeartbeat("c2", 3000);
		String key = (String) map.get("key");
		String heartbeatTimeOld = (String) map.get("heartbeatTimeOld");
		String heartbeatTime = (String) map.get("heartbeatTime");

		ZjianRedisCustomer redisCustomer = (ZjianRedisCustomer) map
				.get("model");
		String returns = (String) map.get("returns");
		System.out.println(key);
		System.out.println(heartbeatTime);
		System.out.println(heartbeatTimeOld);
		System.out.println(returns);
		System.out.println(redisCustomer);
	}

	@Test
	public void getZjianRedisCustomerTest() throws InterruptedException {
		for (int i = 1; i < 100; i++) {

			Map map = zrs.getZjianRedisCustomer("s3");
			String key = (String) map.get("key");
			ZjianRedisCustomer redisCustomer = (ZjianRedisCustomer) map
					.get("model");
			String returns = (String) map.get("returns");
			System.out.println(key);
			System.out.println(returns);
			System.out.println(redisCustomer);
		}

		for (int i = 1; i < 100; i++) {

			Map map = zrs.getZjianRedisCustomer("s1");
			String key = (String) map.get("key");
			ZjianRedisCustomer redisCustomer = (ZjianRedisCustomer) map
					.get("model");
			String returns = (String) map.get("returns");
			System.out.println(key);
			System.out.println(returns);
			System.out.println(redisCustomer);
		}

	}

	@Test
	public void getZjianRedisCustomerListTest() throws InterruptedException {
		for (int i = 1; i < 10; i++) {

			Map map = zrs.getZjianRedisCustomerList("s" + i);
			String key = (String) map.get("key");
			List blist = (List) map.get("list");
			String returns = (String) map.get("returns");
			System.out.println(key);
			System.out.println(returns);
			System.out.println(blist.size());
			if (null != blist && blist.size() > 0) {
				Iterator bit = blist.iterator();
				for (int b = 0; bit.hasNext(); b++) {
					ZjianRedisCustomer pZjianRedisCustomer = (ZjianRedisCustomer) bit
							.next();
					System.out.println("id:" + pZjianRedisCustomer.getId());
				}
			}
		}
	}

	@Test
	public void getZjianRedisServiceTest() throws InterruptedException {
		for (int i = 1; i < 10; i++) {

			Map map = zrs.getZjianRedisService("c" + i);
			String key = (String) map.get("key");
			ZjianRedisService sZjianRedisService = (ZjianRedisService) map
					.get("model");
			String returns = (String) map.get("returns");
			System.out.println(key);
			System.out.println(returns);
			System.out.println(sZjianRedisService);
		}

	}

	public class tZjianRedisCustomerThread extends Thread {
		private int id;
		private Jedis jedis;
		private JedisSentinelPool pool;
		public tZjianRedisCustomerThread(int ids, Jedis j,JedisSentinelPool p) {
			super();
			this.id = ids;
			this.jedis = j;
			this.pool = p;
		}

		public void run() {
			jedis.set("" + id, "" + id);
			//pool.returnResource(jedis);
			// ZjianRedisCustomer zrc = new ZjianRedisCustomer();
			// zrc.setId("c"+id);
			// zrc.setCreateDate("2014-7-30");
			// zrc.setHeartbeatTime(1);
			// zrc.setSource("1");
			// zrc.setStatus(10);
			// zrc.setType("1");
			//
			// Map map = zrs.setZjianRedisCustomer(zrc);
			// String key = (String) map.get("key");
			// String returns = (String) map.get("returns");
			// System.out.println(key);
			// System.out.println(returns);
		}
	}

}
