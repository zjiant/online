package zjian.redis.utils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import zjian.redis.common.Constants;

@SuppressWarnings({ "serial", "unused" ,"static-access"})
public class ZjianJedisSentinelPool implements Serializable {
	private static ZjianJedisSentinelPool zjsp = null;

	public static ZjianJedisSentinelPool getInstance() {
		if (zjsp == null) {
			zjsp = new ZjianJedisSentinelPool();
		}
		return zjsp;
	}

	protected static HostAndPort master = ZjianRedisHostAndPortUtil.getRedisServers()
			.get(0);
	protected static HostAndPort slave = ZjianRedisHostAndPortUtil.getClusterServers()
			.get(0);
	protected static HostAndPort sentinel = ZjianRedisHostAndPortUtil
			.getSentinelServers().get(0);
	protected static Jedis sentinelJedis;
	protected static Set<String> sentinels = new HashSet<String>();

	static {
		sentinels.add(sentinel.toString());
		sentinelJedis = new Jedis(sentinel.getHost(), sentinel.getPort());
	}

	public Jedis getJedis(int database) {
		boolean testOnBorrow = false;
		if (null != ZjianRedisProperties.getRedis_pool_testOnBorrow()
				&& "true".equalsIgnoreCase(ZjianRedisProperties
						.getRedis_pool_testOnBorrow())) {
			testOnBorrow = true;
		}
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxTotal(Integer.parseInt(ZjianRedisProperties
				.getRedis_pool_maxActive()));
		config.setMinIdle(Integer.parseInt(ZjianRedisProperties
				.getRedis_pool_maxIdle()));
		config.setMaxWaitMillis(Integer.parseInt(ZjianRedisProperties
				.getRedis_pool_maxWait()));
		config.setTestOnBorrow(testOnBorrow);
		config.setBlockWhenExhausted(false);

		JedisSentinelPool pool = new JedisSentinelPool(Constants.MASTER_NAME,
				zjsp.sentinels, config, Constants.REDIS_TIMEOUT,
				ZjianRedisProperties.getRedis_master_pwd(), database);
		Jedis jedis = pool.getResource();
		return jedis;
	}

	public Jedis getJedis(JedisSentinelPool pool, int database) {
		Jedis jedis = pool.getResource();
		jedis.select(database);
		return jedis;
	}

	public JedisSentinelPool getJedisPool(int database) {
		boolean testOnBorrow = false;
		if (null != ZjianRedisProperties.getRedis_pool_testOnBorrow()
				&& "true".equalsIgnoreCase(ZjianRedisProperties
						.getRedis_pool_testOnBorrow())) {
			testOnBorrow = true;
		}
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxTotal(Integer.parseInt(ZjianRedisProperties
				.getRedis_pool_maxActive()));
		config.setMinIdle(Integer.parseInt(ZjianRedisProperties
				.getRedis_pool_maxIdle()));
		config.setMaxWaitMillis(Integer.parseInt(ZjianRedisProperties
				.getRedis_pool_maxWait()));
		config.setTestOnBorrow(testOnBorrow);
		config.setBlockWhenExhausted(false);

		JedisSentinelPool pool = new JedisSentinelPool(Constants.MASTER_NAME,
				zjsp.sentinels, config, Constants.REDIS_TIMEOUT,
				ZjianRedisProperties.getRedis_master_pwd(), database);
		return pool;
	}
	
	
	
	/**
	 * 通过key删除（字节）
	 * 
	 * @param key
	 */
	public void del(byte[] key,int database) {
		JedisSentinelPool pool = zjsp.getJedisPool(database);
		Jedis jedis = pool.getResource();
		jedis.del(key);
	}

	/**
	 * 通过key删除
	 * 
	 * @param key
	 */
	public void del(String key,int database) {
		JedisSentinelPool pool = zjsp.getJedisPool(database);
		Jedis jedis = pool.getResource();
		jedis.del(key);
	}
	
	/**
	 * 检查key是否已经存在
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(String key,int database) {
		JedisSentinelPool pool = zjsp.getJedisPool(database);
		Jedis jedis = pool.getResource();
		return jedis.exists(key);
	}

	/**
	 * 清空redis 所有数据
	 * 
	 * @return
	 */
	public String flushDB(int database) {
		JedisSentinelPool pool = zjsp.getJedisPool(database);
		Jedis jedis = pool.getResource();
		return jedis.flushDB();
	}

	/**
	 * 查看redis里有多少数据
	 */
	public long dbSize(int database) {
		JedisSentinelPool pool = zjsp.getJedisPool(database);
		Jedis jedis = pool.getResource();
		return jedis.dbSize();
	}


}
