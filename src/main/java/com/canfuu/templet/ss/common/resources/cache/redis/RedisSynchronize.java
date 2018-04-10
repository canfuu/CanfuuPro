package com.canfuu.templet.ss.common.resources.cache.redis;

import com.canfuu.templet.ss.common.config.Constant;
import com.canfuu.templet.ss.common.resources.cache.redis.synchronization.Condition;
import com.canfuu.templet.ss.common.resources.exceptions.redis.RedisException;
import com.canfuu.templet.ss.common.resources.log.CanfuuLogger;
import com.canfuu.templet.ss.common.resources.log.LogLevel;
import redis.clients.jedis.Jedis;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: Redis同步类
 */
public interface RedisSynchronize {
	/**
	 * 登记r条件
	 * @param condition
	 */
	void register(Condition condition) throws RedisException;

	/**
	 * 登记cud信息
	 * @param tab
	 */
	void register(String tab) throws RedisException;

	/**
	 * 清空列表
	 */
	void flush() throws RedisException;

	default String createConditionKey(String identify, String datakey){
		return identify+":"+datakey +":condition";
	}
	default String createConditionKey(String pageId){
		return pageId +":condition";
	}
	default void init(){
		if(Constant.FLUSH_CACHE_DB) {
			CanfuuLogger.log("清理缓存",RedisSynchronize.class, LogLevel.DEBUG);
			Jedis jedis = new Jedis("localhost",6379);
			jedis.flushDB();
		}
	}
}
