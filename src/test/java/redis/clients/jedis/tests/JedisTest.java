package redis.clients.jedis.tests;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.tests.commands.JedisCommandTestBase;
import redis.clients.util.SafeEncoder;

@SuppressWarnings("resource")
public class JedisTest extends JedisCommandTestBase {
    @Test
    public void useWithoutConnecting() {
	Jedis jedis = new Jedis("192.168.110.128");
	jedis.auth("foobared");
	jedis.dbSize();
    }

    @Test
    public void checkBinaryData() {
	byte[] bigdata = new byte[1777];
	for (int b = 0; b < bigdata.length; b++) {
	    bigdata[b] = (byte) ((byte) b % 255);
	}
	Map<String, String> hash = new HashMap<String, String>();
	hash.put("data", SafeEncoder.encode(bigdata));

	String status = jedis.hmset("foo", hash);
	assertEquals("OK", status);
	assertEquals(hash, jedis.hgetAll("foo"));
    }

    @Test
    public void connectWithShardInfo() {
	JedisShardInfo shardInfo = new JedisShardInfo("127.0.0.1",
		Protocol.DEFAULT_PORT);
	//shardInfo.setPassword("foobared");
	Jedis jedis = new Jedis(shardInfo);
	jedis.get("foo");
	System.out.println(jedis.get("admin"));
    }

    @Test(expected = JedisConnectionException.class)
    public void timeoutConnection() throws Exception {
	jedis = new Jedis("192.168.110.128", 6379, 15000);
	jedis.auth("foobared");
	jedis.configSet("timeout", "1");
	Thread.sleep(2000);
	jedis.hmget("foobar", "foo");
    }

    @Test(expected = JedisDataException.class)
    public void failWhenSendingNullValues() {
	jedis.set("foo", null);
    }

    @Test
    public void shouldReconnectToSameDB() throws IOException {
	jedis.select(1);
	jedis.set("foo", "bar");
	jedis.getClient().getSocket().shutdownInput();
	jedis.getClient().getSocket().shutdownOutput();
	assertEquals("bar", jedis.get("foo"));
    }

    @Test
    public void startWithUrlString() {
	Jedis j = new Jedis("192.168.110.128", 6379);
//	j.auth("foobared");
//	j.select(2);
	j.set("foo1", "bar");
	System.out.println(j.get("admin"));
//	Jedis jedis = new Jedis("redis://:foobared@192.168.110.128:6379/2");
//	assertEquals("PONG", jedis.ping());
//	assertEquals("bar", jedis.get("foo"));
//	assertEquals("PONG", j.ping());
//	assertEquals("bar", j.get("foo"));
    }

   
	@Test
    public void startWithUrl() throws URISyntaxException {
	Jedis j = new Jedis("192.168.110.128", 6379);
	j.auth("foobared");
	j.select(2);
	j.set("foo", "bar");
	Jedis jedis = new Jedis(new URI("redis://:foobared@192.168.110.128:6379/2"));
	assertEquals("PONG", jedis.ping());
	assertEquals("bar", jedis.get("foo"));
    }
    
    @Test
    public void checkCloseable() {
	jedis.close();
	BinaryJedis bj = new BinaryJedis("192.168.110.128");
	bj.connect();
	bj.close();
    }
}