package redis.clients.jedis.tests;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Protocol;

public class HostAndPortUtil {
    private static List<HostAndPort> redisHostAndPortList = new ArrayList<HostAndPort>();
    private static List<HostAndPort> sentinelHostAndPortList = new ArrayList<HostAndPort>();
    private static List<HostAndPort> clusterHostAndPortList = new ArrayList<HostAndPort>();

    static {
	redisHostAndPortList.add(new HostAndPort("192.168.218.123", Protocol.DEFAULT_PORT));

	
	sentinelHostAndPortList.add(new HostAndPort("192.168.218.123", Protocol.DEFAULT_SENTINEL_PORT));
	sentinelHostAndPortList.add(new HostAndPort("192.168.218.42", Protocol.DEFAULT_SENTINEL_PORT));

	clusterHostAndPortList.add(new HostAndPort("192.168.218.42", Protocol.DEFAULT_PORT));
//	clusterHostAndPortList.add(new HostAndPort("192.168.110.128", Protocol.DEFAULT_PORT));

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
