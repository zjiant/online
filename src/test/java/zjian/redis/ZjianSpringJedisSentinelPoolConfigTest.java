package zjian.redis;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.HostAndPort;
import zjian.redis.service.ZjianSpringJedisSentinelPoolConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContex*.xml"})   
public class ZjianSpringJedisSentinelPoolConfigTest {

	@Autowired
	private ZjianSpringJedisSentinelPoolConfig bZjianSpringJedisSentinelPoolConfig;
	

	@Test
	public void testZjianSpringJedisSentinelPool() {
		String name = bZjianSpringJedisSentinelPoolConfig.getName();
		List list = bZjianSpringJedisSentinelPoolConfig.getSentinelHostAndPortList();
		System.out.println(name);
		Iterator it = list.iterator();
		for(int i = 0;it.hasNext();i++){
			HostAndPort h = (HostAndPort)it.next();
			System.out.println(i);
			System.out.println(h.getHost());
			System.out.println(h.getPort());
		}
	}

}
