package com.canfuu.templet.ss.common.persistence;


import com.canfuu.templet.ss.common.config.Constant;
import com.canfuu.templet.ss.common.entity.POJO;
import com.canfuu.templet.ss.common.entity.Page;
import com.canfuu.templet.ss.common.resources.cache.redis.RedisOperate;
import com.canfuu.templet.ss.common.resources.cache.redis.RedisSynchronize;
import com.canfuu.templet.ss.common.resources.cache.redis.synchronization.Condition;
import com.canfuu.templet.ss.common.resources.exceptions.common.PageIsNullException;
import com.canfuu.templet.ss.common.resources.exceptions.redis.RedisException;
import com.canfuu.templet.ss.common.resources.log.CanfuuLogger;
import com.canfuu.templet.ss.common.resources.log.LogLevel;
import com.canfuu.templet.ss.common.utils.CommonUtil;
import com.canfuu.templet.ss.common.utils.StringUtil;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: 通用Dao的实现类
 */

public class CommonDaoImpl implements CommonDao{
	@Resource
	protected RedisOperate redisOperate;

	@Resource
	protected RedisSynchronize redisSynchronize;



	public boolean invoke(Dao dao,String datakey,POJO pojo){
		try {
			if(dao==null || datakey==null || pojo==null){
				return false;
			}
            Method method = null;
			for (Method temp:dao.getClass().getMethods()){
			    if(temp.getName().equals(datakey)){
			        method=temp;
			        break;
                }
            }
            if(method==null){
			    throw new NoSuchMethodException();
            }
			method.invoke(dao,pojo);
			CommonUtil.POOL.getThreadCache().getExecutorService().submit(() ->{
				try{
					redisSynchronize.register(StringUtil.upCaptureName(pojo.getClass().getSimpleName()));
					redisSynchronize.flush();
				} catch(Exception e){
					e.printStackTrace();
				}
			});
			return true;
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			CanfuuLogger.log(e,this,LogLevel.ERROR);
			return false;
		}
	}

	/**
	 * 本方法用于手动保存页面类进入缓存（Page）
	 * 本方法会将identify、datakey自动存入page
	 * @param identify 用户登陆身份
	 * @param datakey  page对应的datakey
	 * @param page     page类本身
	 * @return 是否保存成功
	 * @throws RedisException
	 */
	@Override
	public boolean save(String identify, String datakey, Page page) throws RedisException {
		if(!Constant.CACHE){
			return false;
		}
		return redisOperate.setPage(identify, datakey, page) != null;
	}

	/**
	 * 直接保存一个page类至缓存，要求page中的identify、datakey属性不能为空
	 * 本方法会自动同步identify、datakey
	 * @param page
	 * @return
	 * @throws RedisException
	 */
	@Override
	public boolean save(Page page) throws RedisException {
		if(!Constant.CACHE){
			return false;
		}
		return redisOperate.setPage(page) != null;
	}

	/**
	 * 保存sessionId
	 *
	 * @param sessionId
	 * @return 返回生成的identify
	 * @throws RedisException
	 */
	@Override
	public String save(String userId, String sessionId) throws RedisException {
		if(!Constant.SESSION){
			return null;
		}
		redisOperate.addSESSIONID(userId, sessionId);
		return redisOperate.addIdentify(sessionId);
	}

	/**
	 * 自定义identify保存sessionId
	 *
	 * @param sessionId
	 * @param identify
	 * @return
	 * @throws RedisException
	 */
	@Override
	public String save(String userId, String sessionId, String identify) throws RedisException {
		if(!Constant.SESSION){
			return null;
		}
	    redisOperate.addSESSIONID(userId, sessionId);
		return redisOperate.setIdentify(sessionId, identify);
	}

	/**
	 * 保存pojo类至数据库
	 *
	 * @param dao
	 * @param datakey
	 * @param pojo
	 * @return
	 * @throws RedisException
	 */
	@Override
	public boolean save(Dao dao, String datakey, POJO pojo) throws RedisException {
		return invoke(dao, datakey, pojo);
	}

	/**
	 * 更新pojo类至数据库
	 *
	 * @param dao
	 * @param identify
	 * @param pojo
	 * @return
	 * @throws RedisException
	 */
	@Override
	public boolean update(Dao dao, String identify, POJO pojo) throws RedisException {
		return invoke(dao, identify, pojo);
	}

	/**
	 * 删除pojo类至数据库
	 *
	 * @param dao
	 * @param identify
	 * @param pojo
	 * @return
	 * @throws RedisException
	 */
	@Override
	public boolean delete(Dao dao, String identify, POJO pojo) throws RedisException {
		return invoke(dao, identify, pojo);
	}

	/**
	 * 通过sessionId查询identify
	 *
	 * @param sessionId
	 * @return
	 * @throws RedisException
	 */
	@Override
	public String getIdentify(String sessionId) throws RedisException {
		if(!Constant.SESSION){
			return null;
		}
		return redisOperate.getIdentify(sessionId);
	}

	/**
	 * 通过identify查询 datakey、pageId 缓存列表
	 *
	 * @param identify
	 * @return
	 * @throws RedisException
	 */
	@Override
	public Set<String> getResources(String identify) throws RedisException {
		if(!Constant.CACHE){
			return null;
		}
		return redisOperate.getResources(identify);
	}

	/**
	 * 直接获取一个Page
	 *
	 * @param page
	 * @param dao
	 * @return
	 * @throws RedisException
	 */
	@Override
	public Page getPage(Page page, Dao dao) throws RedisException, PageIsNullException {
		Page p;
		if(page==null){
			throw new PageIsNullException();
		}
		String identify=page.getIdentify();
		String datakey=page.getDatakey();
		if(Constant.CACHE && redisOperate.containsPage(identify, datakey)){
			try{
				p = redisOperate.getPage(identify, datakey);
			} catch(RedisException e){
				p  = CommonDao.invoke(dao,datakey, page);
				if (p == null){
					throw new PageIsNullException();
				}
				p.setSESSIONID(page.getSESSIONID());
				p.setIdentify(identify);
				p.setDatakey(page.getDatakey());
				if (Constant.CACHE){
					redisOperate.setPage(identify,datakey,p);
				}
			    throw e;
			}

		} else {
			p  = CommonDao.invoke(dao,datakey, page);
			if (p == null){
			    throw new PageIsNullException();
            }
            p.setSESSIONID(page.getSESSIONID());
			p.setIdentify(identify);
			p.setDatakey(page.getDatakey());
            if (Constant.CACHE){
                redisOperate.setPage(identify,datakey,p);
            }
		}
		CommonUtil.POOL.getThreadCache().getExecutorService().submit(()->{
			try{
				redisSynchronize.register(new Condition(page));
			} catch(Exception e){
			    CanfuuLogger.log(e,this, LogLevel.ERROR);
			}
		});
		return p;
	}

    @Override
    public String match(String userId) {
		if(!Constant.SESSION){
			return null;
		}
        try {
            String SESSIONID = redisOperate.matchSESSIONID(userId);
            if (redisOperate.getIdentify(SESSIONID) == null){
                return null;
            }else {
                return redisOperate.matchSESSIONID(userId);
            }
        } catch (RedisException e) {
            return null;
        }
    }




}
