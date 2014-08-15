package zjian.redis;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StopWatch;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import zjian.redis.service.ZjianSpringJedisSentinelPool;
import zjian.redis.service.ZjianSpringJedisSentinelPoolConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContex*.xml"})  
public class TestEvalPool {
	
	@Autowired
	private  ZjianSpringJedisSentinelPoolConfig bZjianSpringJedisSentinelPoolConfig;

	@Autowired
	private ZjianSpringJedisSentinelPool bZjianSpringJedisSentinelPool;

	
	//static String host = "localhost";
	static int honBaoCount = 1_0_0000;

	static int threadCount = 20;

	static String hongBaoList = "hongBaoList";
	static String hongBaoConsumedList = "hongBaoConsumedList";
	static String hongBaoConsumedMap = "hongBaoConsumedMap";

	static Random random = new Random();

	// -- 函数：尝试获得红包，如果成功，则返回json字符串，如果不成功，则返回空
	// -- 参数：红包队列名， 已消费的队列名，去重的Map名，用户ID
	// -- 返回值：nil 或者 json字符串，包含用户ID：userId，红包ID：id，红包金额：money
	static String tryGetHongBaoScript =
	// "local bConsumed = redis.call('hexists', KEYS[3], KEYS[4]);\n"
	// + "print('bConsumed:' ,bConsumed);\n"
	"if redis.call('hexists', KEYS[3], KEYS[4]) ~= 0 then\n"
			+ "return nil\n"
			+ "else\n"
			+ "local hongBao = redis.call('rpop', KEYS[1]);\n"
			// + "print('hongBao:', hongBao);\n"
			+ "if hongBao then\n" + "local x = cjson.decode(hongBao);\n"
			+ "x['userId'] = KEYS[4];\n" + "local re = cjson.encode(x);\n"
			+ "redis.call('hset', KEYS[3], KEYS[4], KEYS[4]);\n"
			+ "redis.call('lpush', KEYS[2], re);\n" + "return re;\n" + "end\n"
			+ "end\n" + "return nil";
	static StopWatch watch = new StopWatch();

	@Test
	public void testZjianSpringJedisSentinelPool() throws InterruptedException {
		generateTestData();
		testTryGetHongBao();
	}
	

	public void generateTestData() throws InterruptedException {
		JedisSentinelPool pool = bZjianSpringJedisSentinelPool.getJedisPool(0);
		Jedis jedis = pool.getResource();
		jedis.flushAll();
//		jedis.hsetnx(key, field, value);
//		jedis.rpop(key);
//		jedis.lpush(key, strings);
//		jedis.hexists(key, field);
		final CountDownLatch latch = new CountDownLatch(threadCount);
		for (int i = 0; i < threadCount; ++i) {
			final int temp = i;
			//System.out.println("get temp:" + temp);
			Thread thread = new Thread() {
				public void run() {
					JedisSentinelPool pool = bZjianSpringJedisSentinelPool.getJedisPool(0);
					Jedis jedis = pool.getResource();
					int per = honBaoCount / threadCount;
					//System.out.println("get per:" + per);
					JSONObject object = new JSONObject();
					for (int j = temp * per; j < (temp + 1) * per; j++) {
						object.put("id", j);
						object.put("money", j);
						jedis.lpush(hongBaoList, object.toString());
					}
					latch.countDown();
				}
			};
			thread.start();
		}
		latch.await();
	}

	public void testTryGetHongBao() throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(threadCount);
		System.err.println("start:" + System.currentTimeMillis() / 1000);
		watch.start();
		for (int i = 0; i < threadCount; ++i) {
			final int temp = i;
			//System.out.println("get temp:" + temp);
			Thread thread = new Thread() {
				public void run() {
					JedisSentinelPool pool = bZjianSpringJedisSentinelPool.getJedisPool(0);
					Jedis jedis = pool.getResource();
					String sha = jedis.scriptLoad(tryGetHongBaoScript);
					int j = honBaoCount / threadCount * temp;
					System.out.println("get j:" + j);
					while (true) {
						Object object = jedis.eval(tryGetHongBaoScript, 4,
								hongBaoList, hongBaoConsumedList,
								hongBaoConsumedMap, "" + j);
						j++;
						//System.out.println("get j:" + j);
						if (object != null) {
							 //System.out.println("get hongBao:" + object);
						} else {
							// 已经取完了
							if (jedis.llen(hongBaoList) == 0)
								break;
						}
					}
					latch.countDown();
				}
			};
			thread.start();
		}

		latch.await();
		watch.stop();

		System.err.println("time:" + watch.getTotalTimeSeconds());
		System.err
				.println("speed:" + honBaoCount / watch.getTotalTimeSeconds());
		System.err.println("end:" + System.currentTimeMillis() / 1000);
	}
}