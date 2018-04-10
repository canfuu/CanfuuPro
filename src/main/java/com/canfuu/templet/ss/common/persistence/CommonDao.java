package com.canfuu.templet.ss.common.persistence;



import com.canfuu.templet.ss.common.entity.POJO;
import com.canfuu.templet.ss.common.entity.Page;
import com.canfuu.templet.ss.common.resources.exceptions.common.PageIsNullException;
import com.canfuu.templet.ss.common.resources.exceptions.redis.RedisException;
import com.canfuu.templet.ss.common.resources.log.CanfuuLogger;
import com.canfuu.templet.ss.common.resources.log.LogLevel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: 夹带缓存模块的通用Dao
 */
public interface CommonDao {




	/**
	 * 本方法用于手动保存页面类进入缓存（Page）
	 * @param identify 用户登陆身份
	 * @param datakey page对应的datakey
	 * @param page page类本身
	 * @return 是否保存成功
	 * @throws RedisException
	 */
	boolean save(String identify, String datakey, Page page) throws RedisException;

	/**
	 * 直接保存一个page类至缓存，要求page中的identify、datakey属性不能为空
	 * @param page
	 * @return
	 * @throws RedisException
	 */
	boolean save(Page page) throws RedisException;

	/**
	 * 保存sessionId
	 * @param sessionId
	 * @return 返回生成的identify
	 * @throws RedisException
	 */
	String save(String userId, String sessionId) throws RedisException;

	/**
	 * 自定义identify保存sessionId
	 * @param sessionId
	 * @param identify
	 * @return
	 * @throws RedisException
	 */
	String save(String userId, String sessionId, String identify) throws RedisException;

	/**
	 * 保存pojo类至数据库
	 * @param dao
	 * @param datakey
	 * @param pojo
	 * @return
	 * @throws RedisException
	 */
	boolean save(Dao dao, String datakey, POJO pojo) throws RedisException;

	/**
	 * 更新pojo类至数据库
	 * @param dao
	 * @param identify
	 * @param pojo
	 * @return
	 * @throws RedisException
	 */
	boolean update(Dao dao, String identify, POJO pojo) throws RedisException;

	/**
	 * 删除pojo类至数据库
	 * @param dao
	 * @param identify
	 * @param pojo
	 * @return
	 * @throws RedisException
	 */
	boolean delete(Dao dao, String identify, POJO pojo) throws RedisException;

	/**
	 * 通过sessionId查询identify
	 * @param sessionId
	 * @return
	 * @throws RedisException
	 */
	String getIdentify(String sessionId) throws RedisException;

	/**
	 * 通过identify查询 datakey、pageId 缓存列表
	 * @param identify
	 * @return
	 * @throws RedisException
	 */
	Set<String> getResources(String identify) throws RedisException;

	/**
	 * 直接获取一个page，传入一个存储着条件的page
	 * @param page
	 * @param dao
	 * @return
	 * @throws RedisException
	 * @throws PageIsNullException
	 */
	Page getPage(Page page, Dao dao) throws RedisException, PageIsNullException;

	String match(String userId);

	static Page invoke(Dao dao, String datakey, Page page){
		try {
			Method method = dao.getClass().getDeclaredMethod(datakey,page.getClass());
			return (Page) method.invoke(dao,page);
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			CanfuuLogger.log(e,CommonDao.class, LogLevel.ERROR);
			e.printStackTrace();
			return null;
		}
	}

}
