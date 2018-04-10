package com.canfuu.templet.ss.common.resources.exceptions.redis;


import com.canfuu.templet.ss.common.utils.ExceptionUtil;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: Redis Exception
 */
public class RedisKeyIsNullException extends RedisException{
	public RedisKeyIsNullException(String... objects){
		super(ExceptionUtil.explainError("存储Redis的key时"
				+
				RedisKeyIsNullException.toString(objects)
				+
				"变量为空","在传入前进行判空处理")
		);

	}
	public static String toString(String... objects){
		String message = "[·";
		for (String s:objects) {
			message= message+s+"·";
		}
		message=message+"]";
		return message;
	}
}
