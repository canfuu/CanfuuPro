package com.canfuu.templet.ss.common.resources.cache.redis.impl;

import com.canfuu.templet.ss.common.resources.cache.redis.RedisFactory;
import redis.clients.jedis.Jedis;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: Redis连接工厂代理类
 */
public interface RedisFactoryProxy extends RedisFactory {

	/**
	 * 本方法用于让第三方程序猿自定义数据处理
	 * @param jedis
	 */
	default void handle(Jedis jedis){

	}

	/**
	 * 用于设置数据处理后缀（处理后应该干的事情）
	 * @param jedis
	 */
	void suffix(Jedis jedis);

}
