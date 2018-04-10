package com.canfuu.templet.ss.common.resources.exceptions.redis;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: Redis Exception
 */
public class RedisException extends Exception{
	public RedisException(String me){
		super(me);
	}
}
