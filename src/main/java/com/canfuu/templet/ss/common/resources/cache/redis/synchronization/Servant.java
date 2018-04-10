package com.canfuu.templet.ss.common.resources.cache.redis.synchronization;


import com.canfuu.templet.ss.common.config.Constant;
import com.canfuu.templet.ss.common.entity.Page;
import com.canfuu.templet.ss.common.persistence.CommonDao;
import com.canfuu.templet.ss.common.persistence.Dao;
import com.canfuu.templet.ss.common.resources.cache.redis.RedisOperate;
import com.canfuu.templet.ss.common.resources.cache.redis.RedisSynchronize;
import com.canfuu.templet.ss.common.resources.exceptions.redis.RedisException;
import com.canfuu.templet.ss.common.resources.log.CanfuuLogger;
import com.canfuu.templet.ss.common.resources.log.LogLevel;
import com.canfuu.templet.ss.common.utils.CommonUtil;
import com.canfuu.templet.ss.common.utils.ExceptionUtil;
import com.canfuu.templet.ss.common.utils.SerializeUtil;
import com.canfuu.templet.ss.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Set;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: 同步刷新实现类
 */
public class Servant implements RedisSynchronize {

	@Autowired
	private RedisOperate redisOperate;


	/**
	 * 登记r条件
	 *
	 * @param condition
	 */
	@Override
	public void register(Condition condition) throws RedisException {
			if(condition.getPage().getIdentify()==null || redisOperate.getCli().get(RedisOperate.createPageKey(condition.getPage().getIdentify(),condition.getPage().getDatakey()).getBytes())==null){
				throw new RedisException(ExceptionUtil.explainError("入参条件不符合规则","请检查入参条件page对象identify参数是否为空或是否存在该identify"));
			}
		try {
			redisOperate.getCli().sadd(
					createConditionKey(
							condition.getPage().getIdentify(),
							condition.getPage().getDatakey()
					).getBytes()
					,
					SerializeUtil.serialize(condition));
			redisOperate.getCli().expire(createConditionKey(
					condition.getPage().getIdentify(),
					condition.getPage().getDatakey()
					).getBytes(),
					Constant.CACHE_TIMES
			);
		} catch (Exception ignored) {
		}
	}

	/**
	 * 登记cud信息
	 *
	 * @param tab
	 */
	@Override
	public void register(String tab) {
		try {
			redisOperate.getCli().sadd(Constant.TABLE_RESOURCE_CACHE, tab);
		} catch (Exception e) {
			CanfuuLogger.log(e, this, LogLevel.ERROR);
		}
	}

	/**
	 * 清空列表
	 */
	@Scheduled(cron = "0 0/5 * * * ? ")
	@Override
	public void flush() throws RedisException {
		String tab;
		while ((tab = redisOperate.getCli().spop(Constant.TABLE_RESOURCE_CACHE)) != null) {
			Set<String> datakeys = redisOperate.getCli().smembers(tab);
			for (String datakey : datakeys) {
				for (byte[] conditionId : redisOperate.getCli().keys(("*:" + datakey + ":*").getBytes())) {
					Set<byte[]> conditions;
					if((conditions=redisOperate.getCli().smembers(conditionId))!=null){
						for (byte[] c:conditions) {
							try {
								Condition condition = (Condition) SerializeUtil.unserizlize(c);
								String className = redisOperate.getCli().hget("datakey:dao",datakey);
								Page page = CommonDao.invoke(
										(Dao) CommonUtil.WAC.getBean(StringUtil.upCaptureName(className)),
										datakey,
										condition.getPage());
								String identify = new String(conditionId).replace(":condition","");
								Page p = (Page) SerializeUtil.unserizlize(redisOperate.getCli().get(identify.getBytes()));
								if(p!=null) {
									p.copyTo(page);
								}
								redisOperate.setPage(page);
								register(condition);
							} catch (Exception e) {
								CanfuuLogger.log("同步失败 " + new String(conditionId) +e.getMessage(), this, LogLevel.ERROR);
							}
						}
					}
				}
			}
		}
	}



}
