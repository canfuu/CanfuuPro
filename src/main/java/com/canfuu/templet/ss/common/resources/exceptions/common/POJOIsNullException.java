package com.canfuu.templet.ss.common.resources.exceptions.common;


import com.canfuu.templet.ss.common.utils.ExceptionUtil;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: 通用Exception
 */
public class POJOIsNullException extends Exception{
	public POJOIsNullException(){
		super(ExceptionUtil.explainError("传入了一个NULL的Pojo对象","请传入一个带数据的POJO对象"));
	}
}
