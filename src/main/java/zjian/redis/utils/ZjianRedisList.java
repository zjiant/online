package zjian.redis.utils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.tests.HostAndPortUtil;
import zjian.redis.common.Constants;

@SuppressWarnings({ "serial", "unused" })
public class ZjianRedisList implements Serializable {
	private static final String CLAZZ = "ZjianRedisList";
	private static ZjianRedisList zrl = null;

	public static ZjianRedisList getInstance() {
		if (zrl == null) {
			zrl = new ZjianRedisList();
		}
		return zrl;
	}

	ZjianJedisSentinelPool zjsp = ZjianJedisSentinelPool.getInstance();

	public Long rpushLists(String key, Object value, int database, int expire) {
		Jedis jedis = zjsp.getJedis(database);
		long rl = jedis.rpush(key.getBytes(),
				ZjianRedisSerializeUtil.serialize(value));
		jedis.expire(key, expire);
		return rl;
	}

	public List<byte[]> lrangeLists(String key, int database, int expire) {
		Jedis jedis = zjsp.getJedis(database);
		List<byte[]> list = jedis.lrange(key.getBytes(), 0, -1);
		jedis.expire(key, expire);
		return list;
	}

	public Long rpushList(String key, String value, int database, int expire) {
		Jedis jedis = zjsp.getJedis(database);
		long rl = jedis.rpush(key, value);
		jedis.expire(key, expire);
		return rl;
	}

	public String lpopList(String key, int database) {
		Jedis jedis = zjsp.getJedis(database);
		String pKey = jedis.lpop(key);
		return pKey;
	}

}
