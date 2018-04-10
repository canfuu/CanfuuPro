package com.canfuu.templet.ss.common.resources.exceptions.redis;


import com.canfuu.templet.ss.common.utils.ExceptionUtil;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: Redis Exception
 */
public class RedisValueIsNullException extends RedisException{
	public RedisValueIsNullException(){
		super(ExceptionUtil.explainError("存储Redis的value时value变量为空","在传入前进行判空处理"));
	}
}