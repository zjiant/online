package zjian.redis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Transaction;
import zjian.redis.common.Constants;
import zjian.redis.utils.ZjianRedisProperties;

public class JedisTest {

    private static Jedis jedis;
    private static JedisPool jedisPool;
    private static ShardedJedis shardedJedis;
    private static ShardedJedisPool shardedJedisPool;
    private static JedisSentinelPool jedisSentinelPool;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        jedis = new Jedis("192.168.218.42", 6379);
        jedis.auth("foobared");
        //jedisPool = new JedisPool(new JedisPoolConfig(), "192.168.218.42", 6379);
        jedisPool = new JedisPool(new JedisPoolConfig(), "192.168.218.42", 6379, 1000, "foobared");
        List<JedisShardInfo> shards = Arrays.asList(new JedisShardInfo("192.168.218.123", 6379,"foobared"));
        shardedJedis = new ShardedJedis(shards);
        shardedJedisPool = new ShardedJedisPool(new JedisPoolConfig(), shards);

        Set<String> sentinels = new HashSet<String>();
        sentinels.add("192.168.218.123:26379");
        sentinels.add("192.168.218.42:26379");
        jedisSentinelPool = new JedisSentinelPool("mymaster", sentinels,"foobared");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        jedis.disconnect();
        jedisPool.destroy();
        shardedJedis.disconnect();
        shardedJedisPool.destroy();
        jedisSentinelPool.destroy();
    }

    @Test
    public void test1Normal() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            String result = jedis.set("n" + i, "n" + i);
        }
        long end = System.currentTimeMillis();
        System.out.println("Simple SET: " + ((end - start) / 1000.0) + " seconds");
    }

    @Test
    public void test2Trans() {
        long start = System.currentTimeMillis();
        Transaction tx = jedis.multi();
        for (int i = 0; i < 100000; i++) {
            tx.set("t" + i, "t" + i);
        }
        // System.out.println(tx.get("t1000").get());
        List<Object> results = tx.exec();
        long end = System.currentTimeMillis();
        System.out.println("Transaction SET: " + ((end - start) / 1000.0) + " seconds");
    }

    @Test
    public void test3Pipelined() {
        Pipeline pipeline = jedis.pipelined();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            pipeline.set("p" + i, "p" + i);
        }
        // System.out.println(pipeline.get("p1000").get());
        List<Object> results = pipeline.syncAndReturnAll();
        long end = System.currentTimeMillis();
        System.out.println("Pipelined SET: " + ((end - start) / 1000.0) + " seconds");
    }

    @Test
    public void test4combPipelineTrans() {
        long start = System.currentTimeMillis();
        Pipeline pipeline = jedis.pipelined();
        pipeline.multi();
        for (int i = 0; i < 100000; i++) {
            pipeline.set("" + i, "" + i);
        }
        pipeline.exec();
        List<Object> results = pipeline.syncAndReturnAll();
        long end = System.currentTimeMillis();
        System.out.println("Pipelined transaction: " + ((end - start) / 1000.0) + " seconds");
    }

    @Test
    public void testJedisPool() {
        // 从池中获取一个Jedis对象
        Jedis jedis = jedisPool.getResource();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            String result = jedis.set("JedisPool" + i, "JedisPool" + i);
        }
        long end = System.currentTimeMillis();
        System.out.println("Simple SET: " + ((end - start) / 1000.0) + " seconds");

        // 释放对象池
        jedisPool.returnResource(jedis);
    }

//    @Test
//    public void test5shardNormal() {
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < 100000; i++) {
//            String result = shardedJedis.set("sn" + i, "n" + i);
//        }
//        long end = System.currentTimeMillis();
//        System.out.println("Simple@Sharing SET: " + ((end - start) / 1000.0) + " seconds");
//    }

    @Test
    public void test6shardpipelined() {
        long start = System.currentTimeMillis();
        ShardedJedisPipeline pipeline = shardedJedis.pipelined();
        for (int i = 0; i < 100000; i++) {
            pipeline.set("sp" + i, "p" + i);
        }
        List<Object> results = pipeline.syncAndReturnAll();
        long end = System.currentTimeMillis();
        System.out.println("Pipelined@Sharing SET: " + ((end - start) / 1000.0) + " seconds");
    }

    @Test
    public void test7shardSimplePool() {
        ShardedJedis one = shardedJedisPool.getResource();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            String result = one.set("spn" + i, "n" + i);
        }
        long end = System.currentTimeMillis();
        shardedJedisPool.returnResource(one);
        System.out.println("Simple@Pool SET: " + ((end - start) / 1000.0) + " seconds");
    }

    @Test
    public void test8shardPipelinedPool() {
        ShardedJedis one = shardedJedisPool.getResource();

        ShardedJedisPipeline pipeline = one.pipelined();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            pipeline.set("sppn" + i, "n" + i);
        }
        List<Object> results = pipeline.syncAndReturnAll();

        shardedJedisPool.returnResource(one);

        long end = System.currentTimeMillis();
        System.out.println("Pipelined@Pool SET: " + ((end - start) / 1000.0) + " seconds");
    }

    @Test
    public void test9SentinelPool() {
        Jedis jedis = jedisSentinelPool.getResource();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            String result = jedis.set("Sentinel" + i, "Sentinel" + i);
        }
        long end = System.currentTimeMillis();
        System.out.println("Simple SET: " + ((end - start) / 1000.0) + " seconds");

        jedisSentinelPool.returnResource(jedis);
    }

}
