package com.canfuu.templet.ss.common.resources.cache.redis;

import redis.clients.jedis.Jedis;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0:Redis工厂类
 */
public interface RedisFactory {
	/**
	 * 对外提供的执行方法
	 */
	void execute();
	/**
	 * 用于获取自定redis客户端
	 * @return jedis客户端
	 */
	Jedis getCli();

}
