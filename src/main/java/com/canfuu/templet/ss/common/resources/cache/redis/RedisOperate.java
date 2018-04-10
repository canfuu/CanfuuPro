package com.canfuu.templet.ss.common.resources.cache.redis;


import com.canfuu.templet.ss.common.entity.Page;
import com.canfuu.templet.ss.common.resources.exceptions.redis.RedisException;
import com.canfuu.templet.ss.common.resources.exceptions.redis.RedisKeyIsNullException;
import com.canfuu.templet.ss.common.resources.exceptions.redis.RedisValueIsNotInstanceofSerializableException;
import com.canfuu.templet.ss.common.resources.exceptions.redis.RedisValueIsNullException;
import redis.clients.jedis.Jedis;

import java.util.Set;
/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0:Redis操作类
 */
public interface RedisOperate{

	/**
	 * 通过sessionid获取identify
	 * @param sessionId
	 * @return identify
	 * @throws RedisKeyIsNullException 若sessionId为空抛出异常
	 */
	String getIdentify(String sessionId) throws RedisKeyIsNullException;

	/**
	 * 通过identify获取存储了 datakey,pageId 的map
	 * @param identify
	 * @return
	 * @throws RedisKeyIsNullException
	 */
	Set<String> getResources(String identify) throws RedisKeyIsNullException;
	/**
	 * 通过identify和datakey直接获取
	 * @param identify
	 * @param datakey
	 * @return
	 * @throws RedisKeyIsNullException
	 */
	Page getPage(String identify, String datakey) throws RedisKeyIsNullException;

	/**
	 * 可进行自定义的数据存储
	 * @return 返回一个Jedis，使用后需要执行jedis.close()方法否则会造成资源浪费
	 */
	Jedis getCli();
	/**
	 *  NX 不存在时 才进行set
	 */
	String NX = "NX";
	/**
	 *  XX 存在时 才进行set
	 */
	String XX = "XX";
	/**
	 *  EX 过期时间单位为秒
	 */
	String EX = "EX";
	/**
	 *  PX 过期时间单位为毫秒
	 */
	String PX = "PX";

	/**
	 * 将sessionId放入缓存，返回一个identify
	 * @param sessionId
	 * @return
	 * @throws RedisKeyIsNullException
	 */
	String addIdentify(String sessionId) throws RedisKeyIsNullException;

	/**
	 * 自定义一个identify与sessionId一起放入缓存
	 * @param sessionId
	 * @param identify
	 * @return
	 * @throws RedisValueIsNotInstanceofSerializableException
	 * @throws RedisValueIsNullException
	 * @throws RedisKeyIsNullException
	 */
	String setIdentify(String sessionId, String identify) throws RedisValueIsNotInstanceofSerializableException, RedisValueIsNullException, RedisKeyIsNullException;


	/**
	 * 一键设置缓存数据，使用了本方法可省略setResources步骤
	 * @param identify
	 * @param datakey
	 * @param page
	 * @return
	 * @throws RedisKeyIsNullException
	 * @throws RedisValueIsNotInstanceofSerializableException
	 * @throws RedisValueIsNullException
	 */
	Page setPage(String identify, String datakey, Page page) throws RedisKeyIsNullException, RedisValueIsNotInstanceofSerializableException, RedisValueIsNullException;

	/**
	 * 一键设置缓存数据，使用了本方法可省略setResources步骤
	 * @param page
	 * @return
	 * @throws RedisKeyIsNullException
	 * @throws RedisValueIsNotInstanceofSerializableException
	 * @throws RedisValueIsNullException
	 */
	Page setPage(Page page) throws RedisKeyIsNullException, RedisValueIsNotInstanceofSerializableException, RedisValueIsNullException;

	/**
	 * 判断pageId是否存在缓存
	 * @param pageId
	 * @return
	 * @throws RedisException
	 */
	boolean containsPage(String pageId) throws RedisException;
	/**
	 * 判断page是否存在缓存
	 * @param page
	 * @return
	 * @throws RedisException
	 */
	boolean containsPage(Page page) throws RedisException;

	/**
	 *
	 * @param identify
	 * @param datakey
	 * @return
	 * @throws RedisException
	 */
	boolean containsPage(String identify, String datakey) throws RedisException;

	String matchSESSIONID(String userId) throws RedisException;

    void addSESSIONID(String userId, String sessionId) throws RedisException;
	static String createPageKey(String identify, String datakey){
		return identify+":"+datakey;
	}
}
