package com.canfuu.templet.ss.common.resources.exceptions.common;


import com.canfuu.templet.ss.common.utils.ExceptionUtil;

/**
 * In SpringWebTemplet
 *
 * @author ChuTianShu
 * @version v1.0: 通用Exception
 */
public class NoFieldsException extends Exception{
	public NoFieldsException(){
		super(ExceptionUtil.explainError("传入了一个无属性的Page类","请为Page类加入属性"));
	}
}
