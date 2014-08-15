package zjian.redis.utils;

import org.apache.log4j.Logger;

public class ZjianRedisLog {


	private static Logger log = Logger.getLogger(ZjianRedisLog.class);

	public static void getRedisLogInfo(String clazz,String sMethod, String slog) {
		log = Logger.getLogger(clazz);
		log.info(ZjianResisDateTimeUtil.getDateTime()+","+clazz+","+sMethod+"," + slog + "");
	}

}
