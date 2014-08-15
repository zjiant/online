package zjian.redis.utils;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Protocol;

public class ZjianRedisHostAndPortUtil {

	private static final String CLAZZ = "ZjianRedisHostAndPortUtil";

	private static List<HostAndPort> redisHostAndPortList = new ArrayList<HostAndPort>();
	private static List<HostAndPort> sentinelHostAndPortList = new ArrayList<HostAndPort>();
	private static List<HostAndPort> clusterHostAndPortList = new ArrayList<HostAndPort>();

	static {
		String redis_master_ips = ZjianRedisProperties.getRedis_master_ip();
		try {
			String[] redis_master_ip = redis_master_ips.split(",");
			for (int i = 0; i < redis_master_ip.length; i++) {
				String[] master_ipinfo = redis_master_ip[i].split(":");
				if (master_ipinfo.length == 2) {
					redisHostAndPortList.add(new HostAndPort(master_ipinfo[0],
							Integer.valueOf(master_ipinfo[1])));
				}

			}
		} catch (Exception e) {
			ZjianRedisLog.getRedisLogInfo(CLAZZ, "static",
					"redis_master_ip error:" + redis_master_ips);
			System.exit(-1);
		}

		if (redisHostAndPortList.size() == 0) {
			ZjianRedisLog.getRedisLogInfo(CLAZZ, "static",
					"redisHostAndPortList redis_master_ip error:"
							+ redis_master_ips);
			System.exit(-1);
		}

		String redis_cluster_ips = ZjianRedisProperties.getRedis_cluster_ip();
		try {
			String[] redis_cluster_ip = redis_cluster_ips.split(",");
			for (int j = 0; j < redis_cluster_ip.length; j++) {
				String[] cluster_ipinfo = redis_cluster_ip[j].split(":");
				if (cluster_ipinfo.length == 2) {
					clusterHostAndPortList.add(new HostAndPort(
							cluster_ipinfo[0], Integer
									.valueOf(cluster_ipinfo[1])));
				}

			}
		} catch (Exception e) {
			ZjianRedisLog.getRedisLogInfo(CLAZZ, "static",
					"redis_cluster_ip error:" + redis_master_ips);
			System.exit(-1);
		}

		if (clusterHostAndPortList.size() == 0) {
			ZjianRedisLog.getRedisLogInfo(CLAZZ, "static",
					"clusterHostAndPortList redis_cluster_ip error:"
							+ redis_master_ips);
			System.exit(-1);
		}

		String redis_sentinel_ips = ZjianRedisProperties.getRedis_sentinel_ip();
		try {
			String[] redis_sentinel_ip = redis_sentinel_ips.split(",");
			for (int j = 0; j < redis_sentinel_ip.length; j++) {
				String[] sentinel_ipinfo = redis_sentinel_ip[j].split(":");
				if (sentinel_ipinfo.length == 2) {
					sentinelHostAndPortList.add(new HostAndPort(
							sentinel_ipinfo[0], Integer
									.valueOf(sentinel_ipinfo[1])));
				}

			}
		} catch (Exception e) {
			ZjianRedisLog.getRedisLogInfo(CLAZZ, "static",
					"redis_sentinel_ip error:" + redis_master_ips);
			System.exit(-1);
		}

		if (sentinelHostAndPortList.size() == 0) {
			ZjianRedisLog.getRedisLogInfo(CLAZZ, "static",
					"sentinelHostAndPortList redis_sentinel_ip error:"
							+ redis_master_ips);
			System.exit(-1);
		}

		String envRedisHosts = System.getProperty("redis-hosts");
		String envSentinelHosts = System.getProperty("sentinel-hosts");
		String envClusterHosts = System.getProperty("cluster-hosts");

		redisHostAndPortList = parseHosts(envRedisHosts, redisHostAndPortList);
		sentinelHostAndPortList = parseHosts(envSentinelHosts,
				sentinelHostAndPortList);
		clusterHostAndPortList = parseHosts(envClusterHosts,
				clusterHostAndPortList);
	}

	public static List<HostAndPort> parseHosts(String envHosts,
			List<HostAndPort> existingHostsAndPorts) {

		if (null != envHosts && 0 < envHosts.length()) {

			String[] hostDefs = envHosts.split(",");

			if (null != hostDefs && 2 <= hostDefs.length) {

				List<HostAndPort> envHostsAndPorts = new ArrayList<HostAndPort>(
						hostDefs.length);

				for (String hostDef : hostDefs) {

					String[] hostAndPort = hostDef.split(":");

					if (null != hostAndPort && 2 == hostAndPort.length) {
						String host = hostAndPort[0];
						int port = Protocol.DEFAULT_PORT;

						try {
							port = Integer.parseInt(hostAndPort[1]);
						} catch (final NumberFormatException nfe) {
						}

						envHostsAndPorts.add(new HostAndPort(host, port));
					}
				}

				return envHostsAndPorts;
			}
		}

		return existingHostsAndPorts;
	}

	public static List<HostAndPort> getRedisServers() {
		return redisHostAndPortList;
	}

	public static List<HostAndPort> getSentinelServers() {
		return sentinelHostAndPortList;
	}

	public static List<HostAndPort> getClusterServers() {
		return clusterHostAndPortList;
	}
}
