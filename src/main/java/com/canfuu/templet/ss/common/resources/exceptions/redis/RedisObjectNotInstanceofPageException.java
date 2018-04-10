package com.canfuu.templet.ss.common.resources.exceptions.redis;


import com.canfuu.templet.ss.common.utils.ExceptionUtil;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: Redis Exception
 */
public class RedisObjectNotInstanceofPageException extends RedisException{
	public RedisObjectNotInstanceofPageException(){
		super(ExceptionUtil.explainError("存储Redis时传入对象不为Page变量","请检查入参对象是否合法"));
	}
}
