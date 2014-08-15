package zjian.redis.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import zjian.redis.common.Constants;
import zjian.redis.model.ZjianRedisCustomer;
import zjian.redis.model.ZjianRedisService;
import zjian.redis.service.ZjianResisService;
import zjian.redis.utils.ZjianJedisSentinelPool;
import zjian.redis.utils.ZjianRedisPoolList;
import zjian.redis.utils.ZjianRedisLog;
import zjian.redis.utils.ZjianRedisSerializeUtil;
import zjian.redis.utils.ZjianRedisPoolString;

@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public class ZjianResisServiceImpl implements ZjianResisService {

	private static final String CLAZZ = "ZjianResisServiceImpl";
	ZjianJedisSentinelPool zjsp = ZjianJedisSentinelPool.getInstance();
	ZjianRedisPoolList zrpl = ZjianRedisPoolList.getInstance();
	ZjianRedisPoolString zrps = ZjianRedisPoolString.getInstance();

	@Override
	public Map setZjianRedisService(ZjianRedisService pZjianRedisService) {
		long num = 0;
		String returns = Constants.REDIS_ERROR;
		String key = pZjianRedisService.getId();
		String returns_s = zrps.setPKeys(key, pZjianRedisService,
				Constants.REDIS_DATABASE, Constants.REDIS_EXPIRE);
		if (Constants.REDIS_SUCCESS.equalsIgnoreCase(returns_s)) {
			num = zrpl.rpushPList(Constants.REDIS_SERVICE_KEY, key,
					Constants.REDIS_DATABASE, Constants.REDIS_EXPIRE);
			num = zrpl.rpushPLists(Constants.REDIS_SERVICE_KEYS,
					pZjianRedisService, Constants.REDIS_DATABASE,
					Constants.REDIS_EXPIRE);
			if (num > 0) {
				returns = Constants.REDIS_SUCCESS;
			}
		}

		HashMap map = new HashMap();
		map.put("key", key);
		map.put("num", num);
		map.put("returns", returns);
		ZjianRedisLog.getRedisLogInfo(CLAZZ, "setZjianRedisService", "returns:"
				+ returns);
		return map;
	}

	@Override
	public Map setZjianRedisCustomer(ZjianRedisCustomer pZjianRedisCustomer) {
		String returns = Constants.REDIS_ERROR;
		long num = 0;
		String key = pZjianRedisCustomer.getId();
		String returns_c = zrps.setPKeys(key, pZjianRedisCustomer,
				Constants.REDIS_DATABASE, Constants.REDIS_EXPIRE);
		if (Constants.REDIS_SUCCESS.equalsIgnoreCase(returns_c)) {
			num = zrpl.rpushPList(Constants.REDIS_CUSTOMER_KEY, key,
					Constants.REDIS_DATABASE, Constants.REDIS_EXPIRE);
			num = zrpl.rpushPLists(Constants.REDIS_CUSTOMER_KEYS,
					pZjianRedisCustomer, Constants.REDIS_DATABASE,
					Constants.REDIS_EXPIRE);
			if (num > 0) {
				returns = Constants.REDIS_SUCCESS;
			}

		}
		HashMap map = new HashMap();
		map.put("key", key);
		map.put("num", num);
		map.put("returns", returns);
		ZjianRedisLog.getRedisLogInfo(CLAZZ, "setZjianRedisCustomer",
				"returns:" + returns);
		return map;
	}

	@Override
	public Map setZjianRedisCustomerHeartbeat(String sZjianRedisCustomerKey,
			int heartbeatTime) {
		byte[] bZjianRedisCustomer = (byte[]) zrps.getPKeys(
				sZjianRedisCustomerKey, Constants.REDIS_DATABASE,
				Constants.REDIS_EXPIRE);
		HashMap map = new HashMap();
		String heartbeatTimeOld = "";
		String heartbeatTimes = "";
		String returns = Constants.REDIS_ERROR;
		ZjianRedisCustomer pZjianRedisCustomer = null;

		if (null != bZjianRedisCustomer) {
			pZjianRedisCustomer = (ZjianRedisCustomer) ZjianRedisSerializeUtil
					.unserialize(bZjianRedisCustomer);
			if (null != pZjianRedisCustomer) {
				heartbeatTimeOld = String.valueOf(pZjianRedisCustomer
						.getHeartbeatTime());
				pZjianRedisCustomer.setHeartbeatTime(heartbeatTime);
				heartbeatTimes = String.valueOf(pZjianRedisCustomer
						.getHeartbeatTime());
				pZjianRedisCustomer.setHeartbeatTime(heartbeatTime);
				returns = zrps.setPKeys(sZjianRedisCustomerKey,
						pZjianRedisCustomer, Constants.REDIS_DATABASE,
						Constants.REDIS_EXPIRE,true);
			}

		}
		map.put("key", sZjianRedisCustomerKey);
		map.put("heartbeatTime", heartbeatTimes);
		map.put("heartbeatTimeOld", heartbeatTimeOld);
		map.put("model", pZjianRedisCustomer);
		map.put("returns", returns);
		ZjianRedisLog.getRedisLogInfo(CLAZZ, "setZjianRedisCustomerHeartbeat",
				"returns:" + returns);
		return map;
	}

	@Override
	public Map getZjianRedisService(String sZjianRedisCustomerKey) {
		String sZjianRedisService = (String) ZjianRedisSerializeUtil
				.unserialize((byte[]) zrps.getPKeys(sZjianRedisCustomerKey + "_"
						+ Constants.REDIS_CUSTOMER_KEY_POP,
						Constants.REDIS_DATABASE, Constants.REDIS_EXPIRE));
		HashMap map = new HashMap();
		String returns = Constants.REDIS_ERROR;
		ZjianRedisService pZjianRedisService = null;
		String bZjianRedisServiceKey = null;
		if (null != sZjianRedisService) {
			byte[] bZjianRedisCustomer = (byte[]) zrps.getPKeys(
					sZjianRedisService, Constants.REDIS_DATABASE,
					Constants.REDIS_EXPIRE);
			if (null != bZjianRedisCustomer) {
				pZjianRedisService = (ZjianRedisService) ZjianRedisSerializeUtil
						.unserialize(bZjianRedisCustomer);
				if (null != pZjianRedisService) {
					returns = Constants.REDIS_SUCCESS;
				}

			}

		}
		map.put("key", sZjianRedisCustomerKey);
		map.put("model", pZjianRedisService);
		map.put("returns", returns);
		ZjianRedisLog.getRedisLogInfo(CLAZZ, "getZjianRedisService", "returns:"
				+ returns);
		return map;
	}

	@Override
	public Map getZjianRedisCustomerList(String sZjianRedisServiceKey) {
		HashMap map = new HashMap();
		String returns = Constants.REDIS_ERROR;
		List list = new ArrayList();
		ZjianRedisCustomer pZjianRedisCustomer = null;
		List<byte[]> blist = (List<byte[]>) zrpl
				.lrangePLists(sZjianRedisServiceKey + "_"
						+ Constants.REDIS_SERVICE_KEYS_LIST,
						Constants.REDIS_DATABASE, Constants.REDIS_EXPIRE);
		if (null != blist && blist.size() > 0) {
			Iterator bit = blist.iterator();
			for (int b = 0; bit.hasNext(); b++) {
				byte[] bZjianRedisCustomer = (byte[]) bit.next();
				if (null != bZjianRedisCustomer) {
					pZjianRedisCustomer = (ZjianRedisCustomer) ZjianRedisSerializeUtil
							.unserialize(bZjianRedisCustomer);
					if (null != pZjianRedisCustomer) {
						list.add(pZjianRedisCustomer);
					}
				}
			}
		}
		if (null != list) {
			returns = Constants.REDIS_SUCCESS;
		}
		map.put("key", sZjianRedisServiceKey);
		map.put("list", list);
		map.put("returns", returns);
		ZjianRedisLog.getRedisLogInfo(CLAZZ, "getZjianRedisCustomerList",
				"returns:" + returns);
		return map;
	}

	@Override
	public Map getZjianRedisCustomer(String sZjianRedisServiceKey) {
		long num = 0;
		ZjianRedisCustomer pZjianRedisCustomer = null;
		HashMap map = new HashMap();
		String returns = Constants.REDIS_ERROR;
		ZjianRedisService pZjianRedisService = null;
		String lpopKey = "";
		byte[] bZjianRedisService = (byte[]) zrps.getPKeys(
				sZjianRedisServiceKey, Constants.REDIS_DATABASE,
				Constants.REDIS_EXPIRE);
		if (null != bZjianRedisService) {
			pZjianRedisService = (ZjianRedisService) ZjianRedisSerializeUtil
					.unserialize(bZjianRedisService);
			if (null != pZjianRedisService) {
				lpopKey = zrpl.lpopPList(Constants.REDIS_CUSTOMER_KEY,
						Constants.REDIS_DATABASE,Constants.REDIS_EXPIRE);
				if (null != lpopKey && !"".equalsIgnoreCase(lpopKey)) {
					byte[] bZjianRedisCustomer = (byte[]) zrps.getPKeys(lpopKey,
							Constants.REDIS_DATABASE, Constants.REDIS_EXPIRE);
					if (null != bZjianRedisCustomer) {
						pZjianRedisCustomer = (ZjianRedisCustomer) ZjianRedisSerializeUtil
								.unserialize(bZjianRedisCustomer);
						if (null != pZjianRedisCustomer) {
							// pop的客户列表
							num = zrpl.rpushPList(
									Constants.REDIS_CUSTOMER_KEY_POP, lpopKey,
									Constants.REDIS_DATABASE,
									Constants.REDIS_EXPIRE);
							num = zrpl.rpushPLists(
									Constants.REDIS_CUSTOMER_KEYS_POP,
									pZjianRedisCustomer,
									Constants.REDIS_DATABASE,
									Constants.REDIS_EXPIRE);

							// key(客户ID+_+KEY)：pop的客户-坐席

							zrps.setPKey(lpopKey + "_"
									+ Constants.REDIS_CUSTOMER_KEY_POP,
									sZjianRedisServiceKey,
									Constants.REDIS_DATABASE,
									Constants.REDIS_EXPIRE);
							zrps.setPKeys(lpopKey + "_"
									+ Constants.REDIS_CUSTOMER_KEYS_POP,
									pZjianRedisService,
									Constants.REDIS_DATABASE,
									Constants.REDIS_EXPIRE);

							// key(坐席ID+_+KEY)：保存坐席-客户对象列表
							num = zrpl.rpushPList(sZjianRedisServiceKey + "_"
									+ Constants.REDIS_SERVICE_KEY_LIST,
									lpopKey, Constants.REDIS_DATABASE,
									Constants.REDIS_EXPIRE);
							num = zrpl.rpushPLists(sZjianRedisServiceKey + "_"
									+ Constants.REDIS_SERVICE_KEYS_LIST,
									pZjianRedisCustomer,
									Constants.REDIS_DATABASE,
									Constants.REDIS_EXPIRE);

							returns = Constants.REDIS_SUCCESS;
						}

					}
				}

			}
		}

		map.put("key", lpopKey);
		map.put("model", pZjianRedisCustomer);
		map.put("returns", returns);
		ZjianRedisLog.getRedisLogInfo(CLAZZ, "getZjianRedisCustomer",
				"returns:" + returns);
		return map;
	}

}
