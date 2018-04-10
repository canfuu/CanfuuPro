package com.canfuu.templet.ss.common.resources.exceptions.redis;


import com.canfuu.templet.ss.common.utils.ExceptionUtil;
/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: Redis Exception
 */
public class RedisValueIsNotInstanceofSerializableException extends RedisException{
	public RedisValueIsNotInstanceofSerializableException(){
		super(ExceptionUtil.explainError("Redis存储的value不能必须实现序列化接口Serializable","让该类实现Serializable接口"));
	}
}
