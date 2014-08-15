package zjian.redis.utils;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisConnectionException;
import zjian.redis.common.Constants;

@SuppressWarnings({ "rawtypes", "serial", "static-access" })
public class ZjianRedisPoolString implements Serializable {
	private static final String CLAZZ = "ZjianRedisPoolString";
	private static ZjianRedisPoolString zrps = null;

	public static ZjianRedisPoolString getInstance() {
		if (zrps == null) {
			zrps = new ZjianRedisPoolString();
		}
		return zrps;
	}

	ZjianJedisSentinelPool zjsp = ZjianJedisSentinelPool.getInstance();

	public void returnResourceShouldResetState() {
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxTotal(1);
		config.setBlockWhenExhausted(false);
		JedisSentinelPool pool = new JedisSentinelPool(Constants.MASTER_NAME,
				zjsp.sentinels, config, 1000,
				ZjianRedisProperties.getRedis_master_pwd(), 2);
		Jedis jedis = pool.getResource();

		try {
			jedis.watch("ku");
			Transaction tran = jedis.multi();
			tran.set("ku", "kuv");
			List execList = tran.exec();
			if (null != execList && execList.size() > 0) {
				if ("ok".equalsIgnoreCase((String) execList.get(0))) {
					System.out.println("success");
				} else {
					System.out.println("error");
				}
			}

			pool.returnResource(jedis);
		} catch (JedisConnectionException e) {
			//
			ZjianRedisLog.getRedisLogInfo(CLAZZ,
					"returnResourceShouldResetState", "********ERROR*******"
							+ e.getMessage());
		} finally {
			jedis = null;
			pool.destroy();
		}
	}

	public Object getJPKey(String key, int database, int expire, String p) {
		Object sreturn = null;
		JedisSentinelPool pool = zjsp.getJedisPool(database);
		Jedis jedis = pool.getResource();
		try {
			jedis.expire(key, expire);
			if ("string".equalsIgnoreCase(p)) {
				sreturn = jedis.get(key);
			} else if ("bytes".equalsIgnoreCase(p)) {
				sreturn = jedis.get(key.getBytes());
			}
			pool.returnResource(jedis);
		} catch (JedisConnectionException e) {
			//
			ZjianRedisLog.getRedisLogInfo(CLAZZ, "getJPKey",
					"********ERROR*******" + e.getMessage());
		} finally {
			jedis = null;
			pool.destroy();
		}
		return sreturn;
	}

	public Object setJPKey(String key, Object value, int database, int expire,
			String p) {
		Object sreturn = null;
		JedisSentinelPool pool = zjsp.getJedisPool(database);
		Jedis jedis = pool.getResource();
		try {
			jedis.expire(key, expire);
			if ("string".equalsIgnoreCase(p)) {
				jedis.set(key.getBytes(),
						ZjianRedisSerializeUtil.serialize(value));
				sreturn = key;
			} else if ("bytes".equalsIgnoreCase(p)) {
				sreturn = jedis.set(key.getBytes(),
						ZjianRedisSerializeUtil.serialize(value));
			}
			pool.returnResource(jedis);
		} catch (JedisConnectionException e) {
			//
			ZjianRedisLog.getRedisLogInfo(CLAZZ, "setJPKey",
					"********ERROR*******" + e.getMessage());
		} finally {
			jedis = null;
			pool.destroy();
		}
		return sreturn;
	}

	public Object setJPKey(String key, Object value, int database, int expire,
			String p, boolean lock) {
		Object sreturn = null;
		JedisSentinelPool pool = zjsp.getJedisPool(database);
		Jedis jedis = pool.getResource();
		try {
			jedis.watch(key);
			Transaction tran = jedis.multi();
			jedis.expire(key, expire);
			if ("string".equalsIgnoreCase(p)) {
				jedis.set(key.getBytes(),
						ZjianRedisSerializeUtil.serialize(value));
				sreturn = key;
			} else if ("bytes".equalsIgnoreCase(p)) {
				sreturn = jedis.set(key.getBytes(),
						ZjianRedisSerializeUtil.serialize(value));
			}
			List execList = tran.exec();
			if (null != execList && execList.size() > 0) {
				if ("ok".equalsIgnoreCase((String) execList.get(0))) {
					ZjianRedisLog.getRedisLogInfo(CLAZZ, "setJPKey lock ",
							"********success key*******" + key);
				} else {
					ZjianRedisLog.getRedisLogInfo(CLAZZ, "setJPKey lock ",
							"********error key*******" + key);
				}
			}
			pool.returnResource(jedis);
		} catch (JedisConnectionException e) {
			//
			ZjianRedisLog.getRedisLogInfo(CLAZZ, "setJPKey",
					"********ERROR*******" + e.getMessage());
		} finally {
			jedis = null;
			pool.destroy();
		}
		return sreturn;
	}

	public Object getJPKey(JedisSentinelPool pool,Jedis jedis, String key, int database,
			int expire, String p) {
		Object sreturn = null;
		if (null == pool) {
			pool = zjsp.getJedisPool(database);
		}
		if(null==jedis){
			jedis = pool.getResource();
		}
		try {
			jedis.expire(key, expire);
			if ("string".equalsIgnoreCase(p)) {
				sreturn = jedis.get(key);
			} else if ("bytes".equalsIgnoreCase(p)) {
				sreturn = jedis.get(key.getBytes());
			}
			//pool.returnResource(jedis);
		} catch (JedisConnectionException e) {
			//
			ZjianRedisLog.getRedisLogInfo(CLAZZ, "getJPKey",
					"********ERROR*******" + e.getMessage());
		} finally {
			//jedis = null;
			//pool.destroy();
		}
		return sreturn;
	}

	public Object setJPKey(JedisSentinelPool pool,Jedis jedis, String key, Object value,
			int database, int expire, String p) {
		Object sreturn = null;
		if (null == pool) {
			pool = zjsp.getJedisPool(database);
		}
		if(null==jedis){
			jedis = pool.getResource();
		}
		try {
			jedis.expire(key, expire);
			if ("string".equalsIgnoreCase(p)) {
				jedis.set(key.getBytes(),
						ZjianRedisSerializeUtil.serialize(value));
				sreturn = key;
			} else if ("bytes".equalsIgnoreCase(p)) {
				sreturn = jedis.set(key.getBytes(),
						ZjianRedisSerializeUtil.serialize(value));
			}
			//pool.returnResource(jedis);
		} catch (JedisConnectionException e) {
			//
			ZjianRedisLog.getRedisLogInfo(CLAZZ, "setJPKey",
					"********ERROR*******" + e.getMessage());
		} finally {
			//jedis = null;
			//pool.destroy();
		}
		return sreturn;
	}

	public Object setJPKey(JedisSentinelPool pool,Jedis jedis, String key, Object value,
			int database, int expire, String p, boolean lock) {
		Object sreturn = null;
		if (null == pool) {
			pool = zjsp.getJedisPool(database);
		}
		if(null==jedis){
			jedis = pool.getResource();
		}
		try {
			jedis.watch(key);
			Transaction tran = jedis.multi();
			jedis.expire(key, expire);
			if ("string".equalsIgnoreCase(p)) {
				jedis.set(key.getBytes(),
						ZjianRedisSerializeUtil.serialize(value));
				sreturn = key;
			} else if ("bytes".equalsIgnoreCase(p)) {
				sreturn = jedis.set(key.getBytes(),
						ZjianRedisSerializeUtil.serialize(value));
			}
			List execList = tran.exec();
			if (null != execList && execList.size() > 0) {
				if ("ok".equalsIgnoreCase((String) execList.get(0))) {
					ZjianRedisLog.getRedisLogInfo(CLAZZ, "setJPKey lock ",
							"********success key*******" + key);
				} else {
					ZjianRedisLog.getRedisLogInfo(CLAZZ, "setJPKey lock ",
							"********error key*******" + key);
				}
			}
			//pool.returnResource(jedis);
		} catch (JedisConnectionException e) {
			//
			ZjianRedisLog.getRedisLogInfo(CLAZZ, "setJPKey",
					"********ERROR*******" + e.getMessage());
		} finally {
			//jedis = null;
			//pool.destroy();
		}
		return sreturn;
	}

	public String getPKey(String key, int database, int expire) {
		return (String) getJPKey(key, database, expire, "string");
	}

	public Object getPKeys(String key, int database, int expire) {
		return (Object) getJPKey(key, database, expire, "bytes");
	}

	public String setPKey(String key, Object value, int database, int expire) {
		return (String) setJPKey(key, value, database, expire, "string");
	}

	public String setPKeys(String key, Object value, int database, int expire) {
		String falg = Constants.REDIS_ERROR;
		String rs = (String) setJPKey(key, value, database, expire, "bytes");
		if ("ok".equalsIgnoreCase(rs)) {
			falg = Constants.REDIS_SUCCESS;
		}
		return falg;

	}

	public String setPKeys(String key, Object value, int database, int expire,
			boolean lock) {
		String falg = Constants.REDIS_ERROR;
		String rs = (String) setJPKey(key, value, database, expire, "bytes",
				lock);
		if ("ok".equalsIgnoreCase(rs)) {
			falg = Constants.REDIS_SUCCESS;
		}
		return falg;

	}

	public String getPKey(JedisSentinelPool pool,Jedis jedis, String key, int database,
			int expire) {
		return (String) getJPKey(pool,jedis, key, database, expire, "string");
	}

	public Object getPKeys(JedisSentinelPool pool,Jedis jedis, String key, int database,
			int expire) {
		return (Object) getJPKey(pool,jedis, key, database, expire, "bytes");
	}

	public String setPKey(JedisSentinelPool pool,Jedis jedis, String key, Object value,
			int database, int expire) {
		return (String) setJPKey(pool,jedis, key, value, database, expire, "string");
	}

	public String setPKeys(JedisSentinelPool pool,Jedis jedis, String key, Object value,
			int database, int expire) {
		String falg = Constants.REDIS_ERROR;
		String rs = (String) setJPKey(pool,jedis, key, value, database, expire,
				"bytes");
		if ("ok".equalsIgnoreCase(rs)) {
			falg = Constants.REDIS_SUCCESS;
		}
		return falg;

	}

	public String setPKeys(JedisSentinelPool pool,Jedis jedis, String key, Object value,
			int database, int expire, boolean lock) {
		String falg = Constants.REDIS_ERROR;
		String rs = (String) setJPKey(pool,jedis, key, value, database, expire,
				"bytes", lock);
		if ("ok".equalsIgnoreCase(rs)) {
			falg = Constants.REDIS_SUCCESS;
		}
		return falg;

	}

}
