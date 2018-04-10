package com.canfuu.templet.ss.common.resources.cache.redis.impl;

import com.canfuu.templet.ss.common.resources.cache.redis.RedisFactory;
import com.canfuu.templet.ss.common.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0:Redis连接类
 */
public class RedisClient {

	@Autowired
	private RedisFactory redisFactory = (RedisFactory) CommonUtil.WAC.getBean("redisFactory");
	private Jedis jedis;
	private int i = 0;
	private int maxUseNum;
	private Lock lock = new ReentrantLock();
	public RedisClient(int maxUseNum){
		this.maxUseNum=maxUseNum;
	}

	public Jedis getJedis() {
		lock.lock();
		if(jedis==null){
			jedis=redisFactory.getCli();
			i=0;
		}else {
			if (i > maxUseNum) {
				jedis.close();
				jedis=null;
				jedis=redisFactory.getCli();
				i=0;
			}
		}

		lock.unlock();
		return jedis;
	}
}
