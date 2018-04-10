package com.canfuu.templet.ss.common.utils;

import com.canfuu.templet.ss.common.resources.cache.CachePool;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0:  通用工具类 会存放一些全局资源静态常量对象
 */

public final class CommonUtil{
	public final static WebApplicationContext WAC = ContextLoader.getCurrentWebApplicationContext();
	public final static CachePool POOL = (CachePool) CommonUtil.WAC.getBean("cachePool");
}
