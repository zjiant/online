package zjian.redis.utils;

import java.io.Serializable;

import redis.clients.jedis.Jedis;
import zjian.redis.common.Constants;

@SuppressWarnings({ "serial", "unused" })
public class ZjianRedisString implements Serializable {
	private static final String CLAZZ = "ZjianRedisString";
	private static ZjianRedisString zrs = null;

	public static ZjianRedisString getInstance() {
		if (zrs == null) {
			zrs = new ZjianRedisString();
		}
		return zrs;
	}

	ZjianJedisSentinelPool zjsp = ZjianJedisSentinelPool.getInstance();

	public String getKey(String key, int database, int expire) {
		Jedis jedis = zjsp.getJedis(database);
		jedis.expire(key, expire);
		return jedis.get(key);
	}

	public Object getKeys(String key, int database, int expire) {
		Jedis jedis = zjsp.getJedis(database);
		jedis.expire(key, expire);
		return jedis.get(key.getBytes());
	}

	public String setKey(String key, Object value, int database, int expire) {
		Jedis jedis = zjsp.getJedis(database);
		jedis.set(key.getBytes(), ZjianRedisSerializeUtil.serialize(value));
		jedis.expire(key, expire);
		return jedis.get(key);
	}

	public String setKeys(String key, Object value, int database, int expire) {
		String falg = Constants.REDIS_ERROR;
		Jedis jedis = zjsp.getJedis(database);
		jedis.select(database);
		String rs = jedis.set(key.getBytes(),
				ZjianRedisSerializeUtil.serialize(value));
		jedis.expire(key, expire);
		if ("ok".equalsIgnoreCase(rs)) {
			falg = Constants.REDIS_SUCCESS;
		}
		return falg;
	}

}
