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

@SuppressWarnings({ "unchecked", "serial", "unused" })
public class ZjianRedisPoolList implements Serializable {
	private static final String CLAZZ = "ZjianRedisPoolList";
	private static ZjianRedisPoolList zrpl = null;

	public static ZjianRedisPoolList getInstance() {
		if (zrpl == null) {
			zrpl = new ZjianRedisPoolList();
		}
		return zrpl;
	}

	ZjianJedisSentinelPool zjsp = ZjianJedisSentinelPool.getInstance();

	public Object getListKey(String key, int database, int expire, String p) {
		Object sreturn = null;
		JedisSentinelPool pool = zjsp.getJedisPool(database);
		Jedis jedis = pool.getResource();
		try {
			jedis.select(database);
			jedis.expire(key, expire);
			if ("lrange".equalsIgnoreCase(p)) {
				sreturn = jedis.lrange(key.getBytes(), 0, -1);
			} else if ("lpop".equalsIgnoreCase(p)) {
				sreturn = jedis.lpop(key);
			}
			pool.returnResource(jedis);
		} catch (JedisConnectionException e) {
			//
			ZjianRedisLog.getRedisLogInfo(CLAZZ, "getListKey",
					"********ERROR*******" + e.getMessage());
		} finally {
			jedis = null;
			pool.destroy();
		}
		return sreturn;
	}

	public Object setListKey(String key, Object value, int database,
			int expire, String p) {
		Object sreturn = null;
		JedisSentinelPool pool = zjsp.getJedisPool(database);
		Jedis jedis = pool.getResource();
		try {
			jedis.select(database);
			jedis.expire(key, expire);
			if ("rpushs".equalsIgnoreCase(p)) {
				sreturn = jedis.rpush(key.getBytes(),
						ZjianRedisSerializeUtil.serialize(value));
			} else if ("rpush".equalsIgnoreCase(p)) {
				sreturn = jedis.rpush(key, (String) value);
			}
			pool.returnResource(jedis);
		} catch (JedisConnectionException e) {
			//
			ZjianRedisLog.getRedisLogInfo(CLAZZ, "setListKey",
					"********ERROR*******" + e.getMessage());
		} finally {
			jedis = null;
			pool.destroy();
		}
		return sreturn;
	}

	public Object getListKey(JedisSentinelPool pool,Jedis jedis,String key, int database, int expire, String p) {
		Object sreturn = null;
		if(null==pool){
			pool = zjsp.getJedisPool(database);
		}
		if(null==jedis){
			jedis = pool.getResource();
		}
		try {
			jedis.select(database);
			jedis.expire(key, expire);
			if ("lrange".equalsIgnoreCase(p)) {
				sreturn = jedis.lrange(key.getBytes(), 0, -1);
			} else if ("lpop".equalsIgnoreCase(p)) {
				sreturn = jedis.lpop(key);
			}
			//pool.returnResource(jedis);
		} catch (JedisConnectionException e) {
			//
			ZjianRedisLog.getRedisLogInfo(CLAZZ, "getListKey",
					"********ERROR*******" + e.getMessage());
		} finally {
			//jedis = null;
			//pool.destroy();
		}
		return sreturn;
	}

	public Object setListKey(JedisSentinelPool pool,Jedis jedis,String key, Object value, int database,
			int expire, String p) {
		Object sreturn = null;
		if(null==pool){
			pool = zjsp.getJedisPool(database);
		}
		if(null==jedis){
			jedis = pool.getResource();
		}
		try {
			jedis.select(database);
			jedis.expire(key, expire);
			if ("rpushs".equalsIgnoreCase(p)) {
				sreturn = jedis.rpush(key.getBytes(),
						ZjianRedisSerializeUtil.serialize(value));
			} else if ("rpush".equalsIgnoreCase(p)) {
				sreturn = jedis.rpush(key, (String) value);
			}
			//pool.returnResource(jedis);
		} catch (JedisConnectionException e) {
			//
			ZjianRedisLog.getRedisLogInfo(CLAZZ, "setListKey",
					"********ERROR*******" + e.getMessage());
		} finally {
			jedis = null;
			//pool.destroy();
		}
		return sreturn;
	}
	
	public Long rpushPLists(String key, Object value, int database, int expire) {
		return (Long) setListKey(key, value, database, expire, "rpushs");
	}

	public List<byte[]> lrangePLists(String key, int database, int expire) {
		List<byte[]> list = (List<byte[]>) getListKey(key, database, expire,
				"lrange");
		return list;
	}

	public Long rpushPList(String key, String value, int database, int expire) {
		return (Long) setListKey(key, value, database, expire, "rpush");
	}

	public String lpopPList(String key, int database,int expire) {
		return (String) getListKey(key, database,expire, "lpop");
	}

	
	
	public Long rpushPLists(JedisSentinelPool pool,Jedis jedis,String key, Object value, int database, int expire) {
		return (Long) setListKey(pool,jedis,key, value, database, expire, "rpushs");
	}

	public List<byte[]> lrangePLists(JedisSentinelPool pool,Jedis jedis,String key, int database, int expire) {
		List<byte[]> list = (List<byte[]>) getListKey(pool,jedis,key, database, expire,
				"lrange");
		return list;
	}

	public Long rpushPList(JedisSentinelPool pool,Jedis jedis,String key, String value, int database, int expire) {
		return (Long) setListKey(pool,jedis,key, value, database, expire, "rpush");
	}

	public String lpopPList(JedisSentinelPool pool,Jedis jedis,String key, int database,int expire) {
		return (String) getListKey(pool,jedis,key, database,expire, "lpop");
	}
}