package zjian.redis.service;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import zjian.redis.common.Constants;
import zjian.redis.utils.ZjianRedisProperties;

@SuppressWarnings({ "serial" })
@Service("bZjianSpringJedisSentinelPool")
public class ZjianSpringJedisSentinelPool implements Serializable {

	@Autowired
	private ZjianSpringJedisSentinelPoolConfig bZjianSpringJedisSentinelPoolConfig;

	private static HostAndPort master;
	private static HostAndPort slave;
	private static HostAndPort sentinel;
	private static GenericObjectPoolConfig config;
	private static Jedis sentinelJedis;
	public static Set<String> sentinels = new HashSet<String>();

	@PostConstruct
	public void init() {
		master = bZjianSpringJedisSentinelPoolConfig.getRedisHostAndPortList()
				.get(0);
		slave = bZjianSpringJedisSentinelPoolConfig.getClusterHostAndPortList()
				.get(0);
		sentinel = bZjianSpringJedisSentinelPoolConfig
				.getSentinelHostAndPortList().get(0);
		config = bZjianSpringJedisSentinelPoolConfig.getJedisPoolConfig();
		sentinels.add(sentinel.toString());
		sentinelJedis = new Jedis(sentinel.getHost(), sentinel.getPort());
	}

	public Jedis getJedis(int database) {

		JedisSentinelPool pool = new JedisSentinelPool(Constants.MASTER_NAME,
				sentinels, config, Constants.REDIS_TIMEOUT,
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
		JedisSentinelPool pool = new JedisSentinelPool(Constants.MASTER_NAME,
				sentinels, config, Constants.REDIS_TIMEOUT,
				ZjianRedisProperties.getRedis_master_pwd(), database);
		return pool;
	}

	/**
	 * 通过key删除（字节）
	 * 
	 * @param key
	 */
	public void del(byte[] key, int database) {
		JedisSentinelPool pool = getJedisPool(database);
		Jedis jedis = pool.getResource();
		jedis.del(key);
	}

	/**
	 * 通过key删除
	 * 
	 * @param key
	 */
	public void del(String key, int database) {
		JedisSentinelPool pool = getJedisPool(database);
		Jedis jedis = pool.getResource();
		jedis.del(key);
	}

	/**
	 * 检查key是否已经存在
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(String key, int database) {
		JedisSentinelPool pool = getJedisPool(database);
		Jedis jedis = pool.getResource();
		return jedis.exists(key);
	}

	/**
	 * 清空redis 所有数据
	 * 
	 * @return
	 */
	public String flushDB(int database) {
		JedisSentinelPool pool = getJedisPool(database);
		Jedis jedis = pool.getResource();
		return jedis.flushDB();
	}

	/**
	 * 查看redis里有多少数据
	 */
	public long dbSize(int database) {
		JedisSentinelPool pool = getJedisPool(database);
		Jedis jedis = pool.getResource();
		return jedis.dbSize();
	}

	
}
