package com.canfuu.templet.ss.common.resources.cache.redis.impl;


import com.canfuu.templet.ss.common.config.Constant;
import com.canfuu.templet.ss.common.entity.Page;
import com.canfuu.templet.ss.common.resources.cache.redis.RedisOperate;
import com.canfuu.templet.ss.common.resources.exceptions.redis.RedisException;
import com.canfuu.templet.ss.common.resources.exceptions.redis.RedisKeyIsNullException;
import com.canfuu.templet.ss.common.resources.exceptions.redis.RedisValueIsNotInstanceofSerializableException;
import com.canfuu.templet.ss.common.resources.exceptions.redis.RedisValueIsNullException;
import com.canfuu.templet.ss.common.resources.log.CanfuuLogger;
import com.canfuu.templet.ss.common.resources.log.LogLevel;
import com.canfuu.templet.ss.common.utils.CodeUtil;
import com.canfuu.templet.ss.common.utils.ExceptionUtil;
import com.canfuu.templet.ss.common.utils.SerializeUtil;
import redis.clients.jedis.Jedis;

import java.util.Random;
import java.util.Set;


/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: Redis操作实现类
 */
public class RedisOperateImpl implements RedisOperate {
	private RedisClient[] redisClients;
	private Random r;
	private int clientNum;
	public RedisOperateImpl(int clientNum) {
		r = new Random();
		this.clientNum=clientNum;
	}

	public RedisOperateImpl() {
		r = new Random();
		this.clientNum=8;
	}
	private RedisClient[] getRedisClients(int clientNum){
		if(redisClients==null) {
			redisClients = new RedisClient[clientNum];
			for (int i = 0; i < clientNum; i++) {
				redisClients[i] = new RedisClient(100);
			}
		}
		return redisClients;
	}
	/**
	 * 通过sessionId获取identify
	 *
	 * @param sessionId
	 * @return identify
	 * @throws RedisKeyIsNullException 若sessionId为空抛出异常
	 */
	@Override
	public String getIdentify(String sessionId) throws RedisKeyIsNullException {
		if (sessionId==null) {
			return null;
		}
		try {
			getCli().expire(sessionId, Constant.SESSION_REFLUSH_TIMES);
            return getCli().get(sessionId);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 通过identify获取存储了 datakey,pageId 的map
	 *
	 * @param identify
	 * @return
	 * @throws RedisKeyIsNullException
	 */
	@Override
	public Set<String> getResources(String identify) throws RedisKeyIsNullException {

		if (identify==null) {
			return null;
		}
		try {
			getCli().expire(identify, Constant.CACHE_TIMES);
			return getCli().smembers(identify);
		} catch (Exception e) {
			CanfuuLogger.log(e, this, LogLevel.ERROR);
			return null;
		}
	}

	/**
	 * 通过identify和datakey直接获取
	 *
	 * @param identify
	 * @param datakey
	 * @return
	 * @throws RedisKeyIsNullException
	 */
	@Override
	public Page getPage(String identify, String datakey) throws RedisKeyIsNullException {
		if (datakey==null || identify==null) {
			throw new RedisKeyIsNullException("identify",identify,"datakey",datakey);
		}
		try {
			getCli().expire(RedisOperate.createPageKey(identify, datakey).getBytes(), Constant.CACHE_TIMES);
			return (Page) SerializeUtil.unserizlize(getCli().get(RedisOperate.createPageKey(identify, datakey).getBytes()));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 可进行自定义的数据存储
	 *
	 * @return 返回一个Jedis，使用后需要执行jedis.close()方法否则会造成资源浪费
	 */
	@Override
	public Jedis getCli() {
		int index = r.nextInt(getRedisClients(clientNum).length)+1;
		while (index == 15){
            index = r.nextInt(getRedisClients(clientNum).length)+1;
        }
        return getRedisClients(clientNum)[index].getJedis();
	}

	/**
	 * 将sessionId放入缓存，返回一个identify
	 *
	 * @param sessionId
	 * @return
	 * @throws RedisKeyIsNullException
	 */
	@Override
	public String addIdentify(String sessionId) throws RedisKeyIsNullException {
		if (sessionId==null) {
			throw new RedisKeyIsNullException("sessionid",sessionId);
		}
		try {
			String identify;
			if ((identify = getCli().get(sessionId)) == null) {
				identify = CodeUtil.getUUID();
				getCli().set(sessionId, identify);
			}
			return identify;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 自定义一个identify与sessionId一起放入缓存
	 *
	 * @param sessionId
	 * @param identify
	 * @return
	 * @throws RedisValueIsNotInstanceofSerializableException
	 * @throws RedisValueIsNullException
	 * @throws RedisKeyIsNullException
	 */
	@Override
	public String setIdentify(String sessionId, String identify) throws RedisValueIsNotInstanceofSerializableException, RedisValueIsNullException, RedisKeyIsNullException {
		if (sessionId==null || identify==null) {
			throw new RedisKeyIsNullException("sessionid",sessionId,"identify",identify);
		}
		try {
			getCli().set(sessionId, identify);
			getCli().expire(sessionId, Constant.SESSION_REFLUSH_TIMES);
			return identify;
		} catch (Exception e) {
			CanfuuLogger.log(e, this, LogLevel.ERROR);
			return null;
		}
	}


	/**
	 * 一键设置缓存数据，使用了本方法可省略setResources步骤
	 *
	 * @param identify
	 * @param datakey
	 * @param page
	 * @return
	 * @throws RedisKeyIsNullException
	 * @throws RedisValueIsNotInstanceofSerializableException
	 * @throws RedisValueIsNullException
	 */
	@Override
	public Page setPage(String identify, String datakey, Page page) throws RedisKeyIsNullException, RedisValueIsNotInstanceofSerializableException, RedisValueIsNullException {
		if (identify==null && datakey==null) {
			throw new RedisKeyIsNullException("identify",identify,"datakey",datakey);
		}
		try{
			page.setIdentify(identify).setDatakey(datakey);
			return setPage(page);
		} catch(Exception e){
			CanfuuLogger.log(e,this,LogLevel.ERROR);
		    return null;
		}


	}

	/**
	 * 一键设置缓存数据，使用了本方法可省略setResources步骤
	 * @param page
	 * @return
	 * @throws RedisKeyIsNullException
	 * @throws RedisValueIsNotInstanceofSerializableException
	 * @throws RedisValueIsNullException
	 */
	@Override
	public Page setPage(Page page) throws RedisKeyIsNullException, RedisValueIsNotInstanceofSerializableException, RedisValueIsNullException {
		if (page==null) {
			throw new RedisValueIsNotInstanceofSerializableException();
		}
		String identify = page.getIdentify();
		String datakey = page.getDatakey();
		if(identify==null || datakey==null){
			throw new RedisKeyIsNullException();
		}
		try {
			getCli().set(RedisOperate.createPageKey(identify, datakey).getBytes(), SerializeUtil.serialize(page));
			getCli().expire(RedisOperate.createPageKey(identify, datakey).getBytes(), Constant.CACHE_TIMES);
			getCli().sadd(identify, datakey);
			getCli().expire(identify, Constant.CACHE_TIMES);
			getCli().sadd(datakey, identify);
			getCli().expire(datakey, Constant.CACHE_TIMES);
			return page;
		} catch (Exception e) {
			CanfuuLogger.log(e, this, LogLevel.ERROR);
			return null;
		}

	}




	@Override
	protected void finalize() throws Throwable {

		super.finalize();
	}

	/**
	 * 判断page是否存在缓存
	 * @param o
	 * @return
	 * @throws RedisException
	 */
	@Override
	public boolean containsPage(Page o) {
		if(o==null){
			return false;
		}else {
			return containsPage(o.getIdentify(),o.getDatakey());
		}
	}

	/**
	 * @param identify
	 * @param datakey
	 * @return
	 * @throws RedisException
	 */
	@Override
	public boolean containsPage(String identify, String datakey) {
		if(identify==null || datakey==null){
            return false;
		}else {
			return containsPage(RedisOperate.createPageKey(identify,datakey));
		}
	}

    @Override
    public String matchSESSIONID(String userId) throws RedisException {
	    try{
            return getCli().hget(Constant.SESSION_RESOURCE_CACHE,userId);
        }catch (Exception e){
	      throw new RedisException(ExceptionUtil.explainError("SESSIONID设置错误","请检查SESSIONID是否正确"));
        }

    }

    @Override
    public void addSESSIONID(String userId, String sessionId) throws RedisException {
	    try{
			getCli().hset(Constant.SESSION_RESOURCE_CACHE, userId, sessionId);
        }catch (Exception e){
	        throw new RedisException(ExceptionUtil.explainError("SESSIONID设置错误","请检查SESSIONID，userId以及连接是否正确"));
        }
    }

    /**
	 * 判断pageId是否存在缓存
	 * @param pageId
	 * @return
	 * @throws RedisException
	 */
	@Override
	public boolean containsPage(String pageId){
		if(pageId==null){
			return false;
		}else {
			if(getCli().get(pageId.getBytes())==null){
				return false;
			}else {
				return true;
			}
		}
	}


}
