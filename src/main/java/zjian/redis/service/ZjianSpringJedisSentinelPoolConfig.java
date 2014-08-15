package zjian.redis.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Service;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;

@SuppressWarnings({ "serial" })
@Service("bZjianSpringJedisSentinelPoolConfig")
public class ZjianSpringJedisSentinelPoolConfig implements Serializable {
	private String name;
	private String requirepass;
	private List<HostAndPort> redisHostAndPortList;
	private List<HostAndPort> clusterHostAndPortList;
	private List<HostAndPort> sentinelHostAndPortList;
	private JedisPoolConfig jedisPoolConfig;

	public ZjianSpringJedisSentinelPoolConfig(String name, String requirepass,
			List<HostAndPort> redisHostAndPortList,
			List<HostAndPort> clusterHostAndPortList,
			List<HostAndPort> sentinelHostAndPortList,
			JedisPoolConfig jedisPoolConfig
			) {
		this.setName(name);
		this.setRequirepass(requirepass);
		this.setRedisHostAndPortList(redisHostAndPortList);
		this.setClusterHostAndPortList(clusterHostAndPortList);
		this.setSentinelHostAndPortList(sentinelHostAndPortList);
		this.setJedisPoolConfig(jedisPoolConfig);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRequirepass() {
		return requirepass;
	}

	public void setRequirepass(String requirepass) {
		this.requirepass = requirepass;
	}

	public List<HostAndPort> getRedisHostAndPortList() {
		return redisHostAndPortList;
	}

	public void setRedisHostAndPortList(List<HostAndPort> redisHostAndPortList) {
		this.redisHostAndPortList = redisHostAndPortList;
	}

	public List<HostAndPort> getSentinelHostAndPortList() {
		return sentinelHostAndPortList;
	}

	public void setSentinelHostAndPortList(
			List<HostAndPort> sentinelHostAndPortList) {
		this.sentinelHostAndPortList = sentinelHostAndPortList;
	}

	public List<HostAndPort> getClusterHostAndPortList() {
		return clusterHostAndPortList;
	}

	public void setClusterHostAndPortList(
			List<HostAndPort> clusterHostAndPortList) {
		this.clusterHostAndPortList = clusterHostAndPortList;
	}

	public JedisPoolConfig getJedisPoolConfig() {
		return jedisPoolConfig;
	}

	public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
		this.jedisPoolConfig = jedisPoolConfig;
	}

}
